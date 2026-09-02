package net.paladins.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.api.entity.LivingEntityImmunity;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.target.EntityRelations;
import org.jspecify.annotations.Nullable;

public class BarrierEntity extends Entity implements SpellEntity.Spawned {

    private Identifier spellId;
    private int ownerId;
    private int timeToLive = 20;
    public BarrierEntity(EntityType<? extends BarrierEntity> entityType, Level world) {
        super(entityType, world);
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        var owner = args.owner();
        var spellId = args.spell().unwrapKey().get().identifier();
        var spawn = args.spawnData();
        this.spellId = spellId;
        this.getEntityData().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.ownerId = owner.getId();
        this.getEntityData().set(OWNER_ID_TRACKER, this.ownerId);
        this.timeToLive = spawn.time_to_live_seconds * 20;
        this.getEntityData().set(TIME_TO_LIVE_TRACKER, this.timeToLive);
    }

    /// Decides who the barrier is solid to. Vanilla asks the *target* — `Entity#canCollideWith(other)`
    /// is `other.canBeCollidedWith(mover)` — so this single override is what makes protected entities
    /// walk through the barrier while everyone else is stopped by it. (Before 26.1 the same rule was
    /// installed as a SpellEngine `TwoWayCollisionChecker`, which no longer exists.)
    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        // `null` is `EntitySelector.CAN_BE_COLLIDED_WITH` asking without a mover (source-less collision
        // queries): the barrier is not solid on its own, only against the entities it holds back.
        if (other == null) {
            return false;
        }
        if (this.getOwner() == null) {
            return false;
        }
        return other instanceof LivingEntity otherLiving && !isProtected(otherLiving);
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
    public boolean canCollideWith(Entity other) {
        var owner = this.getOwner();
        if (owner == null) {
            return super.canCollideWith(other);
        }
        if (other instanceof LivingEntity otherLiving) {
            return !isProtected(otherLiving);
        }
        return super.canCollideWith(other);
    }

    /// `Entity#damage` is abstract since 1.21.2 (there is no inheritable no-op any more). The barrier
    /// takes no damage — hitting it only plays the impact sound, which is what the 1.21.1 code did too
    /// (the old `Entity#damage` base always returned false for a non-living entity).
    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        this.level().playSound(null, this, PaladinSounds.holy_barrier_impact.soundEvent(), SoundSource.PLAYERS, 1F, 1F);
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        var spellEntry = getSpellEntry();
        if (spellEntry != null) {
            var spell = spellEntry.value();
            var width = spell.range * 2;
            var height = spell.range;
            return EntityDimensions.scalable(width, height);
        } else {
            return super.getDimensions(pose);
        }
    }

    private static final EntityDataAccessor<String> SPELL_ID_TRACKER  = SynchedEntityData.defineId(BarrierEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> OWNER_ID_TRACKER  = SynchedEntityData.defineId(BarrierEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIME_TO_LIVE_TRACKER  = SynchedEntityData.defineId(BarrierEntity.class, EntityDataSerializers.INT);
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPELL_ID_TRACKER, "");
        builder.define(OWNER_ID_TRACKER, 0);
        builder.define(TIME_TO_LIVE_TRACKER, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        var rawSpellId = this.getEntityData().get(SPELL_ID_TRACKER);
        if (rawSpellId != null && !rawSpellId.isEmpty()) {
            this.spellId = Identifier.parse(rawSpellId);
        }
        this.timeToLive = this.getEntityData().get(TIME_TO_LIVE_TRACKER);
        this.refreshDimensions();
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
    protected void readAdditionalSaveData(ValueInput view) {
        this.spellId = Identifier.parse(view.getStringOr(NBTKey.SPELL_ID.key, ""));
        this.ownerId = view.getIntOr(NBTKey.OWNER_ID.key, 0);
        this.timeToLive = view.getIntOr(NBTKey.TIME_TO_LIVE.key, 0);

        this.getEntityData().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.getEntityData().set(OWNER_ID_TRACKER, this.ownerId);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        view.putString(NBTKey.SPELL_ID.key, this.spellId.toString());
        view.putInt(NBTKey.OWNER_ID.key, this.ownerId);
        view.putInt(NBTKey.TIME_TO_LIVE.key, this.timeToLive);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    private static final TagKey<DamageType> BARRIER_PROTECTS = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("paladins", "barrier_protects"));

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
        var world = this.level();
        if (world.isClientSide()) {
            // Client
            if (!idleSoundFired) {
                world.playLocalSound(this, PaladinSounds.holy_barrier_idle.soundEvent(), SoundSource.PLAYERS, 1F, 1F);
                idleSoundFired = true;
            }
        } else if (world instanceof ServerLevel serverWorld) {
            // Server
            if (this.tickCount > this.timeToLive) {
                this.kill(serverWorld);
            }
            if (this.tickCount % checkInterval == 0) {
                var entities = level().getEntities(this, this.getBoundingBox().inflate(0.1F));
                for (var entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (isProtected(livingEntity)) {
                            LivingEntityImmunity.apply(livingEntity, null, BARRIER_PROTECTS, null, true, checkInterval + 1);
                        } else {
                            // 26.2: the 3-arg `knockback` is gone; the terminal overload takes the damage
                            // source and dealt damage (unused by the vanilla body, they exist for the
                            // NeoForge event and for mixins). The barrier pushes without dealing damage, so
                            // pass a magic source with 0 damage and `comesFromEffect = true` — this push
                            // comes from a spell effect, not from an attack.
                            livingEntity.knockback(PaladinsMod.tweaksConfig.value.barrier_knockback_strength,
                                    this.getX() - livingEntity.getX(), this.getZ() - livingEntity.getZ(),
                                    serverWorld.damageSources().magic(), 0F, true);
                            if (livingEntity instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.send(
                                        new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()),
                                        null
                                );
                            }
                        }
                    }
                }
            }
            if (this.tickCount == (this.timeToLive - expirationDuration())) {
                this.level().playSound(null, this, PaladinSounds.holy_barrier_deactivate.soundEvent(), SoundSource.PLAYERS, 1F, 1F);
            }
        }
    }

    public int expirationDuration() {
        return 20;
    }

    public boolean isExpiring() {
        return this.tickCount >= (this.timeToLive - expirationDuration());
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

    @Nullable public Holder<Spell> getSpellEntry() {
        return SpellRegistry.from(this.level()).get(this.spellId).orElse(null);
    }

    private LivingEntity cachedOwner = null;
    @Nullable
    public LivingEntity getOwner() {
        if (cachedOwner != null) {
            return cachedOwner;
        }
        var owner = this.level().getEntity(this.ownerId);
        if (owner instanceof LivingEntity livingOwner) {
            cachedOwner = livingOwner;
            return livingOwner;
        }
        return null;
    }
}
