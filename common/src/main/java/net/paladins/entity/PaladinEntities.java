package net.paladins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.spell.summon.SummonedEntities;
import net.spell_engine.api.spell.summon.SummonedEntityConfig;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PaladinEntities {

    /// Pairs a custom entity's type with its display name (for lang datagen) and, optionally, its
    /// summoned-entity attribute defaults. Mirrors the {@code Effects.Entry} pattern: the name lives
    /// next to the registration so it can't drift or be forgotten (as {@code lightwell} previously was).
    public static class Entry<T extends Entity> {
        public final Identifier id;
        /// English display name, emitted as {@code entity.<namespace>.<path>} by lang datagen.
        public final String name;
        public final EntityType<T> type;
        /// Attribute defaults for summoned entities (seeded into config/spell_engine/summoned_entities.json).
        /// Null for entities that aren't spell-power-scaled summons (e.g. barrier, banner).
        @Nullable public final SummonedEntityConfig.Entry summonConfig;

        public Entry(Identifier id, String name, EntityType<T> type) {
            this(id, name, type, null);
        }
        public Entry(Identifier id, String name, EntityType<T> type, @Nullable SummonedEntityConfig.Entry summonConfig) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.summonConfig = summonConfig;
        }
    }

    public static final List<Entry<?>> entries = new ArrayList<>();
    private static <T extends Entity> Entry<T> add(Entry<T> entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry<BarrierEntity> BARRIER = add(new Entry<>(
            Identifier.of(PaladinsMod.ID, "barrier"),
            "Barrier",
            EntityType.Builder.<BarrierEntity>create(BarrierEntity::new, SpawnGroup.MISC)
                    // was fixed(); vanilla builder only yields `changing`, which is equivalent
                    // here since this entity carries no GENERIC_SCALE attribute.
                    .dimensions(1F, 1F)
                    .makeFireImmune()
                    .maxTrackingRange(128)
                    .trackingTickInterval(20)
                    .build()));

    public static final Entry<BannerEntity> BANNER = add(new Entry<>(
            Identifier.of(PaladinsMod.ID, "battle_banner"),
            "Battle Banner",
            EntityType.Builder.<BannerEntity>create(BannerEntity::new, SpawnGroup.MISC)
                    .dimensions(6F, 0.5F) // dimensions in Minecraft units of the render; changing
                    .makeFireImmune()
                    .maxTrackingRange(128)
                    .trackingTickInterval(20)
                    .build()));

    public static final Entry<LightwellEntity> LIGHTWELL = add(new Entry<>(
            Identifier.of(PaladinsMod.ID, "lightwell"),
            "Lightwell",
            EntityType.Builder.<LightwellEntity>create(LightwellEntity::new, SpawnGroup.MISC)
                    .dimensions(0.9F, 1.4F)
                    .makeFireImmune()
                    .maxTrackingRange(64)
                    .trackingTickInterval(3)
                    .build(),
            lightwellDefaults()));

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
        for (var entry : entries) {
            Registry.register(Registries.ENTITY_TYPE, entry.id, entry.type);
            if (entry.summonConfig != null) {
                // Only summoned (living) entities carry a config; safe by construction.
                @SuppressWarnings("unchecked")
                var livingType = (EntityType<? extends LivingEntity>) entry.type;
                // Type and attributes registered together — no ordering requirement (see summons docs §3.3).
                SummonedEntities.registerAttributes(entry.id, livingType, entry.summonConfig);
            }
        }
    }
}
