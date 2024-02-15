package net.paladins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellSpawnedEntity;

public class BarrierEntity extends Entity implements SpellSpawnedEntity {
    public static EntityType<BarrierEntity> TYPE;

    public int maxLifeTime = 100;

    public BarrierEntity(EntityType<? extends BarrierEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void onCreatedFromSpell(LivingEntity caster, Identifier spellId) {

    }

    @Override
    public void tick() {
        System.out.println("BarrierEntity.tick");
        super.tick();
        if (this.age > this.maxLifeTime) {
            this.kill();
        }
    }
}
