package net.paladins.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;
import net.paladins.util.TwoWayCollisionChecker;
import net.spell_engine.api.entity.SpellSpawnedEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.TargetHelper;
import org.jetbrains.annotations.Nullable;

public class BarrierEntity extends Entity implements SpellSpawnedEntity {
    public static EntityType<BarrierEntity> TYPE;
    public static final Identifier idleSoundId = new Identifier(PaladinsMod.ID, "holy_barrier_idle");
    public static final SoundEvent idleSound = SoundEvent.of(idleSoundId);

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
            var relation = TargetHelper.getRelation(this.getOwner(), otherLiving);
            switch (relation) {
                case FRIENDLY, SEMI_FRIENDLY -> {
                    return false;
                }
                case NEUTRAL, MIXED, HOSTILE -> {
                    return true;
                }
            }
        }
        return super.collidesWith(other);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var spell = getSpell();
        if (spell != null) {
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
    protected void initDataTracker() {
        this.getDataTracker().startTracking(SPELL_ID_TRACKER, "");
        this.getDataTracker().startTracking(OWNER_ID_TRACKER, 0);
        this.getDataTracker().startTracking(TIME_TO_LIVE_TRACKER, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        var rawSpellId = this.getDataTracker().get(SPELL_ID_TRACKER);
        if (rawSpellId != null && !rawSpellId.isEmpty()) {
            this.spellId = new Identifier(rawSpellId);
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
        this.spellId = new Identifier(nbt.getString(NBTKey.SPELL_ID.key));
        this.ownerId = nbt.getInt(NBTKey.OWNER_ID.key);
        this.timeToLive = nbt.getInt(NBTKey.TIME_TO_LIVE.key);
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

    private boolean soundAssigned = false;

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            // Client
            if (!soundAssigned) {
                var clientWorld = (ClientWorld) this.getWorld();
                var player = MinecraftClient.getInstance().player;
                clientWorld.playSoundFromEntity(player, this, idleSound, SoundCategory.PLAYERS, 1F, 1F);
                soundAssigned = true;
            }
        } else {
            // Server
            if (this.age > this.timeToLive) {
                this.kill();
            }
        }
    }

    public Spell getSpell() {
        return SpellRegistry.getSpell(spellId);
    }

    @Nullable
    public LivingEntity getOwner() {
        return (LivingEntity) this.getWorld().getEntityById(this.ownerId);
    }
}
