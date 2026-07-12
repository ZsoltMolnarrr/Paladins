package net.paladins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.spell.summon.SummonedEntities;
import net.spell_engine.api.spell.summon.SummonedEntityConfig;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

public class PaladinEntities {
    public static final Identifier BARRIER_ID = Identifier.of(PaladinsMod.ID, "barrier");
    public static final Identifier BANNER_ID = Identifier.of(PaladinsMod.ID, "battle_banner");

    // Base attributes for the Lightwell summon, seeded into config/spell_engine/summoned_entities.json.
    // Its heal scales off its OWN healing spell power (summons don't use the owner's), so a base value
    // is granted here and topped up from the owner via the summon's attribute_scaling.
    public static SummonedEntityConfig.Entry lightwellDefaults() {
        var e = new SummonedEntityConfig.Entry();
        e.common = new SummonedEntityConfig.CommonAttributes(20, 0.0, 0); // health, speed (stationary), attack
        e.common.follow_range = 16;
        e.custom.add(new SummonedEntityConfig.CustomAttribute(SpellSchools.HEALING.id.toString(), 1));
        // Seed Haste at the neutral baseline (100 = 1.0x). Required so getHaste has an attribute to read
        // (without it the well's haste would be 0 -> effectively infinite cooldown); the summon's
        // attribute_scaling then mirrors the owner's Healing Haste on top (see PaladinSummons.lightwell).
        e.custom.add(new SummonedEntityConfig.CustomAttribute(
                SpellPowerMechanics.HASTE.id.toString(), SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE));
        return e;
    }

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
        LightwellEntity.TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                LightwellEntity.ID,
                EntityType.Builder.<LightwellEntity>create(LightwellEntity::new, SpawnGroup.MISC)
                        .dimensions(0.9F, 1.4F)
                        .makeFireImmune()
                        .maxTrackingRange(64)
                        .trackingTickInterval(3)
                        .build()
        );
        // Type and attributes registered together — no ordering requirement (see summons docs §3.3).
        SummonedEntities.registerAttributes(LightwellEntity.ID, LightwellEntity.TYPE, lightwellDefaults());
    }
}
