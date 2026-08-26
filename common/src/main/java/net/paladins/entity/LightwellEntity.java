package net.paladins.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.spell_engine.entity.SummonedEntity;

public class LightwellEntity extends SummonedEntity {

    public LightwellEntity(EntityType<? extends LightwellEntity> entityType, Level world) {
        super(entityType, world);
    }
}
