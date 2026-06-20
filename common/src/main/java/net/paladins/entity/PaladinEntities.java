package net.paladins.entity;

import net.minecraft.entity.EntityType;
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
                EntityType.Builder.<BarrierEntity>create(BarrierEntity::new, SpawnGroup.MISC)
                        // was fixed(); vanilla builder only yields `changing`, which is equivalent
                        // here since this entity carries no GENERIC_SCALE attribute.
                        .dimensions(1F, 1F)
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build()
        );
        BannerEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                BANNER_ID,
                EntityType.Builder.<BannerEntity>create(BannerEntity::new, SpawnGroup.MISC)
                        .dimensions(6F, 0.5F) // dimensions in Minecraft units of the render; changing
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build()
        );
    }
}
