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

import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.spell_engine.rpg_series.config.AttributeModifier;

import java.util.ArrayList;
import java.util.List;
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

    /// Standard tier attributes for kite shields.
    ///
    /// Deliberately *not* `Shields.standardAttributes(tier)`: SpellEngine still spells these attribute
    /// ids the 1.21.1 way (`minecraft:generic.armor_toughness`, `generic.max_health`), and 1.21.2
    /// dropped the `generic.` prefix — those ids no longer resolve (`ConfigUtil` logs
    /// "Failed to resolve EntityAttribute"). Values are identical to SpellEngine's.
    private static List<AttributeModifier> standardAttributes(Equipment.Tier tier) {
        return switch (tier) {
            case WOODEN, TIER_0, GOLDEN -> List.of();
            case TIER_1, TIER_2 -> List.of(toughness(1), health(2));
            case TIER_3 -> List.of(toughness(1), health(4));
            case TIER_4, TIER_5 -> List.of(toughness(1), health(6));
        };
    }

    private static AttributeModifier toughness(float value) {
        return new AttributeModifier(EntityAttributes.ARMOR_TOUGHNESS.getIdAsString(), value, Operation.ADD_VALUE);
    }

    private static AttributeModifier health(float value) {
        return new AttributeModifier(EntityAttributes.MAX_HEALTH.getIdAsString(), value, Operation.ADD_VALUE);
    }

    private static Shield.Entry create(String name, Equipment.Tier tier, Supplier<Ingredient> repairIngredient) {
        return Shields.create(PaladinsMod.ID, name, tier, repairIngredient,
                standardAttributes(tier), PaladinSounds.shield_equip.entry());
    }

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    // MARK: Shields

    public static Shield.Entry iron_kite_shield = add(create("iron_kite_shield", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT)).translatedName("Iron Kite Shield"));
    public static Shield.Entry golden_kite_shield = add(create("golden_kite_shield", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT)).translatedName("Golden Kite Shield"));
    public static Shield.Entry diamond_kite_shield = add(create("diamond_kite_shield", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND)).translatedName("Diamond Kite Shield"));
    public static Shield.Entry netherite_kite_shield = add(create("netherite_kite_shield", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)).translatedName("Netherite Kite Shield"));

    public static void register(Map<String, ShieldConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", Platform.util().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            add(create("ruby_kite_shield", Equipment.Tier.TIER_4, repair));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", Platform.util().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            add(create("aeternium_kite_shield", Equipment.Tier.TIER_4, repair));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            var repair = ingredient("aether:ambrosium_shard", Platform.util().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            add(create("aether_kite_shield", Equipment.Tier.TIER_4, repair)
                    .loot(-1, "aether"));
        }
        Shield.register(configs, entries, Group.KEY, VanillaShields::create);
    }
}
