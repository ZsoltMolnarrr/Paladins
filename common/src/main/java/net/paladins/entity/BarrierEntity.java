package net.paladins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.api.entity.LivingEntityImmunity;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.api.entity.TwoWayCollisionChecker;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_engine.utils.SoundPlayerWorld;
import org.jetbrains.annotations.Nullable;

public class BarrierEntity extends Entity implements SpellEntity.Spawned {

    private Identifier spellId;
    private int ownerId;
    private int timeToLive = 20;
    public BarrierEntity(EntityType<? extends BarrierEntity> entityType, World world) {
        super(entityType, world);
        ((TwoWayCollisionChecker)this).setReverseCollisionChecker(entity -> {
            return this.collidesWith(entity)
                    ? TwoWayCollisionChecker.CollisionResult.COLLIDE
                    : TwoWayCollisionChecker.CollisionResult.PASS;
        });
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        var owner = args.owner();
        var spellId = args.spell().getKey().get().getValue();
        var spawn = args.spawnData();
        this.spellId = spellId;
        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.ownerId = owner.getId();
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
        this.timeToLive = spawn.time_to_live_seconds * 20;
        this.getDataTracker().set(TIME_TO_LIVE_TRACKER, this.timeToLive);
    }

    @Override
    public boolean isCollidable(@Nullable Entity entity) {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return this.isAlive();
    }

    @Override
    public boolean collidesWith(Entity other) {
        var owner = this.getOwner();
        if (owner == null) {
            return super.collidesWith(other);
        }
        if (other instanceof LivingEntity otherLiving) {
            return !isProtected(otherLiving);
        }
        return super.collidesWith(other);
    }

    /// `Entity#damage` is abstract since 1.21.2 (there is no inheritable no-op any more). The barrier
    /// takes no damage — hitting it only plays the impact sound, which is what the 1.21.1 code did too
    /// (the old `Entity#damage` base always returned false for a non-living entity).
    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        this.getEntityWorld().playSoundFromEntity(null, this, PaladinSounds.holy_barrier_impact.soundEvent(), SoundCategory.PLAYERS, 1F, 1F);
        return false;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var spellEntry = getSpellEntry();
        if (spellEntry != null) {
            var spell = spellEntry.value();
            var width = spell.range * 2;
            var height = spell.range;
            return EntityDimensions.changing(width, height);
        } else {
            return super.getDimensions(pose);
        }
    }

    private static final TrackedData<String> SPELL_ID_TRACKER  = DataTracker.registerData(BarrierEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> OWNER_ID_TRACKER  = DataTracker.registerData(BarrierEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_TO_LIVE_TRACKER  = DataTracker.registerData(BarrierEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SPELL_ID_TRACKER, "");
        builder.add(OWNER_ID_TRACKER, 0);
        builder.add(TIME_TO_LIVE_TRACKER, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        var rawSpellId = this.getDataTracker().get(SPELL_ID_TRACKER);
        if (rawSpellId != null && !rawSpellId.isEmpty()) {
            this.spellId = Identifier.of(rawSpellId);
        }
        this.timeToLive = this.getDataTracker().get(TIME_TO_LIVE_TRACKER);
        this.calculateDimensions();
    }

    private enum NBTKey {
        OWNER_ID("OwnerId"),
        SPELL_ID("SpellId"),
        TIME_TO_LIVE("TTL"),
        ;

        public final String key;
        NBTKey(String key) {
            this.key = key;
        }
    }


    @Override
    protected void readCustomData(ReadView view) {
        this.spellId = Identifier.of(view.getString(NBTKey.SPELL_ID.key, ""));
        this.ownerId = view.getInt(NBTKey.OWNER_ID.key, 0);
        this.timeToLive = view.getInt(NBTKey.TIME_TO_LIVE.key, 0);

        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putString(NBTKey.SPELL_ID.key, this.spellId.toString());
        view.putInt(NBTKey.OWNER_ID.key, this.ownerId);
        view.putInt(NBTKey.TIME_TO_LIVE.key, this.timeToLive);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    private static final TagKey<DamageType> BARRIER_PROTECTS = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("paladins", "barrier_protects"));

    private boolean idleSoundFired = false;
    private static final int checkInterval = 4;
    
    @Override
    public void tick() {
        super.tick();
        var spellEntry = getSpellEntry();
        if (spellEntry == null) {
            return;
        }
        var spell = spellEntry.value();
        var world = this.getEntityWorld();
        if (world.isClient()) {
            // Client
            if (!idleSoundFired) {
                ((SoundPlayerWorld)world).playSoundFromEntity(this, PaladinSounds.holy_barrier_idle.soundEvent(), SoundCategory.PLAYERS, 1F, 1F);
                idleSoundFired = true;
            }
        } else if (world instanceof ServerWorld serverWorld) {
            // Server
            if (this.age > this.timeToLive) {
                this.kill(serverWorld);
            }
            if (this.age % checkInterval == 0) {
                var entities = getEntityWorld().getOtherEntities(this, this.getBoundingBox().expand(0.1F));
                for (var entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (isProtected(livingEntity)) {
                            LivingEntityImmunity.apply(livingEntity, null, BARRIER_PROTECTS, null, true, checkInterval + 1);
                        } else {
                            livingEntity.takeKnockback(PaladinsMod.tweaksConfig.value.barrier_knockback_strength,
                                    this.getX() - livingEntity.getX(), this.getZ() - livingEntity.getZ());
                            if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
                                serverPlayer.networkHandler.send(
                                        new EntityVelocityUpdateS2CPacket(serverPlayer.getId(), serverPlayer.getVelocity()),
                                        null
                                );
                            }
                        }
                    }
                }
            }
            if (this.age == (this.timeToLive - expirationDuration())) {
                this.getEntityWorld().playSoundFromEntity(null, this, PaladinSounds.holy_barrier_deactivate.soundEvent(), SoundCategory.PLAYERS, 1F, 1F);
            }
        }
    }

    public int expirationDuration() {
        return 20;
    }

    public boolean isExpiring() {
        return this.age >= (this.timeToLive - expirationDuration());
    }

    public boolean isProtected(Entity other) {
        var owner = this.getOwner();
        if (owner == null) {
            return false;
        }
        var relation = EntityRelations.getRelation(owner, other);
        switch (relation) {
            case ALLY, FRIENDLY -> {
                return true;
            }
            case NEUTRAL, MIXED, HOSTILE -> {
                return false;
            }
        }
        return false;
    }

    @Nullable public RegistryEntry<Spell> getSpellEntry() {
        return SpellRegistry.from(this.getEntityWorld()).getEntry(this.spellId).orElse(null);
    }

    private LivingEntity cachedOwner = null;
    @Nullable
    public LivingEntity getOwner() {
        if (cachedOwner != null) {
            return cachedOwner;
        }
        var owner = this.getEntityWorld().getEntityById(this.ownerId);
        if (owner instanceof LivingEntity livingOwner) {
            cachedOwner = livingOwner;
            return livingOwner;
        }
        return null;
    }
}
