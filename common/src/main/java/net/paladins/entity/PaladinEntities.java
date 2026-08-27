package net.paladins.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.paladins.PaladinsMod;
import net.spell_engine.api.spell.summon.SummonedEntities;
import net.spell_engine.api.spell.summon.SummonedEntityConfig;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import net.tiny_config.ConfigManager;
import org.jspecify.annotations.Nullable;

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
        /// Attribute defaults for summoned entities (seeded into Paladins' own config/paladins/summoned_entities.json).
        /// Null for entities that aren't spell-power-scaled summons (e.g. barrier, banner).
        public final SummonedEntityConfig.@Nullable Entry summonConfig;

        public Entry(Identifier id, String name, EntityType<T> type) {
            this(id, name, type, null);
        }
        public Entry(Identifier id, String name, EntityType<T> type, SummonedEntityConfig.@Nullable Entry summonConfig) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.summonConfig = summonConfig;
        }
    }

    private static ResourceKey<EntityType<?>> entityKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(PaladinsMod.ID, path));
    }

    public static final List<Entry<?>> entries = new ArrayList<>();
    private static <T extends Entity> Entry<T> add(Entry<T> entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry<BarrierEntity> BARRIER = add(new Entry<>(
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "barrier"),
            "Barrier",
            EntityType.Builder.<BarrierEntity>of(BarrierEntity::new, MobCategory.MISC)
                    // was fixed(); vanilla builder only yields `changing`, which is equivalent
                    // here since this entity carries no GENERIC_SCALE attribute.
                    .sized(1F, 1F)
                    .fireImmune()
                    .clientTrackingRange(128)
                    .updateInterval(20)
                    // Vanilla build(RegistryKey) — the no-arg build() is a Fabric API interface-injected
                    // default (FabricEntityType.Builder) absent on NeoForge at runtime.
                    .build(entityKey("barrier"))));

    public static final Entry<BannerEntity> BANNER = add(new Entry<>(
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "battle_banner"),
            "Battle Banner",
            EntityType.Builder.<BannerEntity>of(BannerEntity::new, MobCategory.MISC)
                    .sized(6F, 0.5F) // dimensions in Minecraft units of the render; changing
                    .fireImmune()
                    .clientTrackingRange(128)
                    .updateInterval(20)
                    .build(entityKey("battle_banner"))));

    public static final Entry<LightwellEntity> LIGHTWELL = add(new Entry<>(
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "lightwell"),
            "Lightwell",
            EntityType.Builder.<LightwellEntity>of(LightwellEntity::new, MobCategory.MISC)
                    .sized(0.9F, 1.4F)
                    .fireImmune()
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build(entityKey("lightwell")),
            lightwellDefaults()));

    // Base attributes for the Lightwell summon, seeded into Paladins' OWN config file
    // (config/paladins/summoned_entities.json), versioned independently. Its heal scales off its OWN
    // healing spell power (summons don't use the owner's), so a base value is granted here and topped up
    // from the owner via the summon's attribute_scaling.
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

    /// Paladins' own summoned-entity config file, seeded from the per-entity defaults above and versioned
    /// independently (bump `schemaVersion` to reset users' files after a defaults change). Declared after
    /// the entity constants so {@link #entries} is fully populated when the defaults are collected.
    public static final ConfigManager<SummonedEntityConfig> summonConfig = new ConfigManager<>
            ("summoned_entities", seededDefaults())
            .builder()
            .setDirectory(PaladinsMod.ID)
            .schemaVersion(1)
            .sanitize(true)
            .build();

    private static SummonedEntityConfig seededDefaults() {
        var config = new SummonedEntityConfig();
        for (var entry : entries) {
            if (entry.summonConfig != null) {
                config.entries.put(entry.id.toString(), entry.summonConfig);
            }
        }
        return config;
    }

    public static void register() {
        summonConfig.refresh(); // load (or write) Paladins' own config file before reading values from it
        for (var entry : entries) {
            Registry.register(BuiltInRegistries.ENTITY_TYPE, entry.id, entry.type);
            if (entry.summonConfig != null) {
                // Only summoned (living) entities carry a config; safe by construction.
                @SuppressWarnings("unchecked")
                var livingType = (EntityType<? extends LivingEntity>) entry.type;
                // Type and attributes registered together — no ordering requirement (see summons docs §3.3).
                // Inject Paladins' config as the attribute source — a plain Function<Identifier, Entry>.
                SummonedEntities.registerAttributes(entry.id, livingType, summonConfig.value::entryFor);
            }
        }
    }
}
