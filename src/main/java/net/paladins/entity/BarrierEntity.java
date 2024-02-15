package net.paladins.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellSpawnedEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.TargetHelper;
import org.jetbrains.annotations.Nullable;

public class BarrierEntity extends Entity implements SpellSpawnedEntity {
    public static EntityType<BarrierEntity> TYPE;
    private Identifier spellId;
    private int ownerId;

    public int maxLifeTime = 200; // TODO: From spell json TTL

    public BarrierEntity(EntityType<? extends BarrierEntity> entityType, World world) {
        super(entityType, world);
    }

    public void onCreatedFromSpell(LivingEntity owner, Identifier spellId) {
        this.spellId = spellId;
        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.ownerId = owner.getId();


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


    public boolean collidesWith(Entity other) {
        var owner = this.getOwner();
        if (owner == null) {
            return super.collidesWith(other);
        }
        if (other instanceof LivingEntity otherLiving) {
            var relation = TargetHelper.getRelation(otherLiving, this.getOwner());
            System.out.println("Relation to " + otherLiving.getEntityName() + " is " + relation);
            if (relation == TargetHelper.Relation.FRIENDLY) {
                System.out.println("Collides with " + other.getEntityName() + ": false");
                return false;
            }
        }
        var collides = super.collidesWith(other);
        System.out.println("Collides with " + other.getEntityName() + ": " + collides);
        return super.collidesWith(other);
    }

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
    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(SPELL_ID_TRACKER, "");
        this.getDataTracker().startTracking(OWNER_ID_TRACKER, 0);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        var rawSpellId = this.getDataTracker().get(SPELL_ID_TRACKER);
        if (rawSpellId != null && !rawSpellId.isEmpty()) {
            this.spellId = new Identifier(rawSpellId);
        }
        this.calculateDimensions();
    }

        @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        System.out.println("BarrierEntity.tick");
        super.tick();
        if (this.age > this.maxLifeTime) {
            this.kill();
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
