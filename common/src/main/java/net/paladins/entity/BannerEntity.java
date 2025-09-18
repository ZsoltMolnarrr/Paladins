package net.paladins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.spell_engine.entity.SpellCloud;

public class BannerEntity extends SpellCloud {
    public static EntityType<BannerEntity> ENTITY_TYPE;

    public BannerEntity(EntityType<? extends SpellCloud> entityType, World world) {
        super(entityType, world);
    }
}
