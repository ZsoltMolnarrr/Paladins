package net.paladins.item;

import net.spell_engine.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.paladins.item.shield.VanillaShields;
import net.spell_engine.rpg_series.config.ShieldConfig;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Shield;
import net.spell_engine.rpg_series.item.Shields;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class PaladinShields {
    public static final ArrayList<Shield.Entry> entries = new ArrayList<>();

    private static Shield.Entry add(Shield.Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> Ingredient.ofItems(fallback);
        } else {
            return () -> {
                // `Registries.ITEM` is defaulted, so `get(Identifier)` never returns null — it returns AIR
                // for an unknown id. Ask for the optional value instead and fall back explicitly.
                var item = Registries.ITEM.getOptionalValue(id).orElse(fallback);
                return Ingredient.ofItems(item);
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
        Shield.register(configs, entries, Group.KEY, VanillaShields::create);
    }
}
