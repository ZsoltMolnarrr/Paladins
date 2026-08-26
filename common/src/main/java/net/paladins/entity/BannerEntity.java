package net.paladins.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.spell_engine.entity.SpellCloud;

public class BannerEntity extends SpellCloud {

    public BannerEntity(EntityType<? extends SpellCloud> entityType, Level world) {
        super(entityType, world);
    }
}
