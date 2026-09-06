package net.paladins.item;

import net.spell_engine.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.rpg_series.config.ShieldConfig;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Shield;
import net.spell_engine.rpg_series.item.Shields;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class PaladinShields {
    /// Loader seam for the shield item factory.
    ///
    /// On 1.20.1 ShieldAPI is a **Fabric-only** artifact (`maven.modrinth:shield-api:1.0.0+1.20.1-fabric`;
    /// Modrinth publishes no Forge build), so `net.fabric_extras.shield_api.item.CustomShieldItem` may not be
    /// referenced from `common`. The Fabric entrypoint installs `CustomShieldItem::new` here before
    /// `PaladinsMod.registerItems()`; on Forge the factory stays null and no kite shields are registered
    /// (their data files are excluded from the Forge jar — see forge/build.gradle).
    @Nullable public static Shield.ShieldFactory factory = null;

    /// Whether kite shields exist on this loader. False on Forge until a 1.20.1 ShieldAPI Forge build exists.
    public static boolean available() {
        return factory != null;
    }

    public static final ArrayList<Shield.Entry> entries = new ArrayList<>();

    private static Shield.Entry add(Shield.Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = new Identifier(idString);
        if (requirement) {
            return () -> Ingredient.ofItems(fallback);
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    // MARK: Shields

    public static Shield.Entry iron_kite_shield = add(Shields.createStandard(PaladinsMod.ID, "iron_kite_shield", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT), PaladinSounds.shield_equip.entry()).translatedName("Iron Kite Shield"));
    public static Shield.Entry golden_kite_shield = add(Shields.createStandard(PaladinsMod.ID, "golden_kite_shield", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT), PaladinSounds.shield_equip.entry()).translatedName("Golden Kite Shield"));
    public static Shield.Entry diamond_kite_shield = add(Shields.createStandard(PaladinsMod.ID, "diamond_kite_shield", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND), PaladinSounds.shield_equip.entry()).translatedName("Diamond Kite Shield"));
    public static Shield.Entry netherite_kite_shield = add(Shields.createStandard(PaladinsMod.ID, "netherite_kite_shield", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT), PaladinSounds.shield_equip.entry()).translatedName("Netherite Kite Shield"));

    public static void register(Map<String, ShieldConfig> configs) {
        if (factory == null) {
            // No ShieldAPI on this loader — skip shield registration entirely.
            return;
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", Platform.util().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            add(Shields.createStandard(PaladinsMod.ID, "ruby_kite_shield", Equipment.Tier.TIER_4, repair, PaladinSounds.shield_equip.entry()));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", Platform.util().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            add(Shields.createStandard(PaladinsMod.ID, "aeternium_kite_shield", Equipment.Tier.TIER_4, repair, PaladinSounds.shield_equip.entry()));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            var repair = ingredient("aether:ambrosium_shard", Platform.util().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            add(Shields.createStandard(PaladinsMod.ID, "aether_kite_shield", Equipment.Tier.TIER_4, repair, PaladinSounds.shield_equip.entry())
                    .loot(-1, "aether"));
        }
        Shield.register(configs, entries, Group.KEY, factory);
    }
}
