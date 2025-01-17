package net.paladins.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;
import net.paladins.util.SoundHelper;
import net.spell_engine.api.effect.EntityImmunity;
import net.spell_engine.api.entity.SpellSpawnedEntity;
import net.spell_engine.api.entity.TwoWayCollisionChecker;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.utils.SoundPlayerWorld;
import net.spell_engine.utils.TargetHelper;
import org.jetbrains.annotations.Nullable;

public class BarrierEntity extends Entity implements SpellSpawnedEntity {
    public static EntityType<BarrierEntity> TYPE;

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
    public void onCreatedFromSpell(LivingEntity owner, Identifier spellId, Spell.Impact.Action.Spawn spawn) {
        this.spellId = spellId;
        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.ownerId = owner.getId();
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
        this.timeToLive = spawn.time_to_live_seconds * 20;
        this.getDataTracker().set(TIME_TO_LIVE_TRACKER, this.timeToLive);
    }

    @Override
    public boolean isCollidable() {
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

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.getWorld().playSoundFromEntity(null, this, SoundHelper.holy_barrier_impact.sound(), SoundCategory.PLAYERS, 1F, 1F);
        return super.damage(source, amount);
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
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.spellId = Identifier.of(nbt.getString(NBTKey.SPELL_ID.key));
        this.ownerId = nbt.getInt(NBTKey.OWNER_ID.key);
        this.timeToLive = nbt.getInt(NBTKey.TIME_TO_LIVE.key);

        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString(NBTKey.SPELL_ID.key, this.spellId.toString());
        nbt.putInt(NBTKey.OWNER_ID.key, this.ownerId);
        nbt.putInt(NBTKey.TIME_TO_LIVE.key, this.timeToLive);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

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
        var world = this.getWorld();
        if (world.isClient()) {
            // Client
            if (!idleSoundFired) {
                ((SoundPlayerWorld)world).playSoundFromEntity(this, SoundHelper.holy_barrier_idle.sound(), SoundCategory.PLAYERS, 1F, 1F);
                idleSoundFired = true;
            }
        } else {
            // Server
            if (this.age > this.timeToLive) {
                this.kill();
            }
            if (this.age % checkInterval == 0) {
                var entities = getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.1F));
                for (var entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (isProtected(livingEntity)) {
                            EntityImmunity.setImmune(livingEntity, EntityImmunity.Type.AREA_EFFECT, checkInterval + 1);
                            EntityImmunity.setImmune(livingEntity, EntityImmunity.Type.EXPLOSION, checkInterval + 1);
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
                this.getWorld().playSoundFromEntity(null, this, SoundHelper.holy_barrier_deactivate.sound(), SoundCategory.PLAYERS, 1F, 1F);
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
        var relation = TargetHelper.getRelation(owner, other);
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
        return SpellRegistry.from(this.getWorld()).getEntry(this.spellId).orElse(null);
    }

    private LivingEntity cachedOwner = null;
    @Nullable
    public LivingEntity getOwner() {
        if (cachedOwner != null) {
            return cachedOwner;
        }
        var owner = this.getWorld().getEntityById(this.ownerId);
        if (owner instanceof LivingEntity livingOwner) {
            cachedOwner = livingOwner;
            return livingOwner;
        }
        return null;
    }
}
