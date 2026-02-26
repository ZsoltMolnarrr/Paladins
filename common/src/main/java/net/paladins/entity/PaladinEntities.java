package net.paladins.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PaladinEntities {
    public static final Identifier BARRIER_ID = Identifier.of(PaladinsMod.ID, "barrier");
    public static final Identifier BANNER_ID = Identifier.of(PaladinsMod.ID, "battle_banner");
    public static void register() {
        BarrierEntity.TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                BARRIER_ID,
                FabricEntityTypeBuilder.<BarrierEntity>create(SpawnGroup.MISC, BarrierEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
        BannerEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                BANNER_ID,
                FabricEntityTypeBuilder.<BannerEntity>create(SpawnGroup.MISC, BannerEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F)) // dimensions in Minecraft units of the render
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
    }
}
