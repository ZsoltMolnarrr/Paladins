package net.paladins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.spell_engine.entity.SummonedEntity;

public class LightwellEntity extends SummonedEntity {

    public LightwellEntity(EntityType<? extends LightwellEntity> entityType, World world) {
        super(entityType, world);
    }
}
