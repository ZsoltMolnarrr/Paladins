package net.paladins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;
import net.spell_engine.entity.SummonedEntity;

public class LightwellEntity extends SummonedEntity {
    public static final Identifier ID = Identifier.of(PaladinsMod.ID, "lightwell");
    public static EntityType<LightwellEntity> TYPE;

    public LightwellEntity(EntityType<? extends LightwellEntity> entityType, World world) {
        super(entityType, world);
    }
}
