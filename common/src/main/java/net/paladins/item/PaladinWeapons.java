package net.paladins.item;

import net.spell_engine.Platform;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSpells;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.rpg_series.item.Weapons;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class PaladinWeapons {
    private static final String NAMESPACE = PaladinsMod.ID;
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry add(Weapon.Entry entry) {
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

    /// `Ingredient.fromTag` is gone since 1.21.2 and repair is the `minecraft:repairable` component,
    /// which SpellEngine's `Weapon.CustomMaterial` resolves from the supplier while the item is being
    /// constructed — long before tags are bound (`Registries.ITEM.getOrThrow(TagKey)` throws
    /// "Tags not bound" there). Returning an empty ingredient makes SpellEngine skip the override, so
    /// the item keeps the `repairable` component the vanilla `ToolMaterial` already installed:
    /// `wooden_tool_materials` for `Tier.WOODEN` (a superset of planks) and `stone_tool_materials`
    /// for `Tier.TIER_0` — i.e. exactly what the 1.21.1 tag ingredients expressed.
    /// (`null`, not an empty `Ingredient`: `Ingredient.ofItems()` throws "Ingredients can't be empty".)
    private static final Supplier<Ingredient> MATERIAL_DEFAULT_REPAIR = () -> null;

    private static final String AETHER = "aether";
    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";

    // MARK: Claymores

    public static final Weapon.Entry stone_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "stone_claymore", Equipment.Tier.TIER_0, () -> Ingredient.ofItems(Items.COBBLESTONE))
            .translatedName("Stone Claymore"));
    public static final Weapon.Entry iron_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "iron_claymore", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT))
            .translatedName("Iron Claymore"));
    public static final Weapon.Entry golden_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "golden_claymore", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .translatedName("Golden Claymore"));
    public static final Weapon.Entry diamond_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "diamond_claymore", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .translatedName("Diamond Claymore"));
    public static final Weapon.Entry netherite_claymore = add(Weapons.claymoreWithSkill(
            NAMESPACE, "netherite_claymore", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Claymore"));

    // MARK: Hammers

    public static final Weapon.Entry wooden_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "wooden_great_hammer", Equipment.Tier.WOODEN, MATERIAL_DEFAULT_REPAIR)
            .translatedName("Wooden Great Hammer"));
    public static final Weapon.Entry stone_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "stone_great_hammer", Equipment.Tier.TIER_0, MATERIAL_DEFAULT_REPAIR)
            .translatedName("Stone Great Hammer"));
    public static final Weapon.Entry iron_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "iron_great_hammer", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT))
            .translatedName("Iron Great Hammer"));
    public static final Weapon.Entry golden_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "golden_great_hammer", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .translatedName("Golden Great Hammer"));
    public static final Weapon.Entry diamond_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "diamond_great_hammer", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .translatedName("Diamond Great Hammer"));
    public static final Weapon.Entry netherite_great_hammer = add(Weapons.hammerWithSkill(
            NAMESPACE, "netherite_great_hammer", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Great Hammer"));

    // MARK: Maces

    public static final Weapon.Entry iron_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "iron_mace", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.IRON_INGOT))
            .translatedName("Iron Mace"));
    public static final Weapon.Entry golden_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "golden_mace", Equipment.Tier.GOLDEN, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .translatedName("Golden Mace"));
    public static final Weapon.Entry diamond_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "diamond_mace", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .translatedName("Diamond Mace"));
    public static final Weapon.Entry netherite_mace = add(Weapons.maceWithSkill(
            NAMESPACE, "netherite_mace", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .translatedName("Netherite Mace"));

    // MARK: Wands

    public static final Weapon.Entry acolyte_wand = add(Weapons.healingWand(
            NAMESPACE, "acolyte_wand", Equipment.Tier.TIER_0, () -> Ingredient.ofItems(Items.STICK))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Acolyte Wand"));
    public static final Weapon.Entry holy_wand = add(Weapons.healingWand(
            NAMESPACE, "holy_wand", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Holy Wand"));
    public static final Weapon.Entry diamond_holy_wand = add(Weapons.healingWand(
            NAMESPACE, "diamond_holy_wand", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Diamond Holy Wand"));
    public static final Weapon.Entry netherite_holy_wand = add(Weapons.healingWand(
            NAMESPACE, "netherite_holy_wand", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HEAL.id()))
            .translatedName("Netherite Holy Wand"));

    // MARK: Staves

    public static final Weapon.Entry holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "holy_staff", Equipment.Tier.TIER_1, () -> Ingredient.ofItems(Items.GOLD_INGOT))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Holy Staff"));
    public static final Weapon.Entry diamond_holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "diamond_holy_staff", Equipment.Tier.TIER_2, () -> Ingredient.ofItems(Items.DIAMOND))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Diamond Holy Staff"));
    public static final Weapon.Entry netherite_holy_staff = add(Weapons.healingStaff(
            NAMESPACE, "netherite_holy_staff", Equipment.Tier.TIER_3, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
            .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
            .translatedName("Netherite Holy Staff"));

    // MARK: Register

    public static void register(Map<String, WeaponConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", Platform.util().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            add(Weapons.healingStaff(NAMESPACE, "ruby_holy_staff", Equipment.Tier.TIER_4, repair)
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id())));
            add(Weapons.claymoreWithSkill(NAMESPACE, "ruby_claymore", Equipment.Tier.TIER_4, repair));
            add(Weapons.hammerWithSkill(NAMESPACE, "ruby_great_hammer", Equipment.Tier.TIER_4, repair));
            add(Weapons.maceWithSkill(NAMESPACE, "ruby_mace", Equipment.Tier.TIER_4, repair));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", Platform.util().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            add(Weapons.claymoreWithSkill(NAMESPACE, "aeternium_claymore", Equipment.Tier.TIER_4, repair));
            add(Weapons.hammerWithSkill(NAMESPACE, "aeternium_great_hammer", Equipment.Tier.TIER_4, repair));
            add(Weapons.maceWithSkill(NAMESPACE, "aeternium_mace", Equipment.Tier.TIER_4, repair));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || Platform.util().isModLoaded(AETHER)) {
            var repair = ingredient("aether:ambrosium_shard", Platform.util().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            add(Weapons.healingStaff(NAMESPACE, "aether_holy_staff", Equipment.Tier.TIER_4, repair)
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(PaladinSpells.HOLY_SHOCK.id()))
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.claymoreWithSkill(NAMESPACE, "aether_claymore", Equipment.Tier.TIER_4, repair)
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.hammerWithSkill(NAMESPACE, "aether_great_hammer", Equipment.Tier.TIER_4, repair)
                    .loot(Equipment.LootProperties.of("aether")));
            add(Weapons.maceWithSkill(NAMESPACE, "aether_mace", Equipment.Tier.TIER_4, repair)
                    .loot(Equipment.LootProperties.of("aether")));
        }

        Weapon.register(configs, entries, Group.KEY);
    }
}
