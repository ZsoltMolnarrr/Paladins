package net.paladins.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.paladins.PaladinsMod;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.paladins.Platform;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        return entry(null, name, material, item, defaults);
    }

    private static Weapon.Entry entry(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(PaladinsMod.ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString) {
        return ingredient(idString, Items.DIAMOND);
    }

    private static Supplier<Ingredient> ingredient(String idString, Item fallback) {
        var id = new Identifier(idString);
        return () -> { 
            var item = Registry.ITEM.get(id);
            var ingredient = item != null ? item : fallback;
            return Ingredient.ofItems(ingredient); 
        };
    }

    // MARK: Wands

//    private static final float wandAttackDamage = 2;
//    private static final float wandAttackSpeed = -2.4F;
//    private static Weapon.Entry wand(String name, Weapon.CustomMaterial material) {
//        var settings = new Item.Settings().group(Group.PALADINS);
//        var item = new StaffItem(material, settings);
//        return entry(name, material, item, new ItemConfig.Weapon(wandAttackDamage, wandAttackSpeed));
//    }
//
//    public static final Weapon.Entry noviceWand = wand("wand_novice",
//            Weapon.CustomMaterial.matching(ToolMaterials.WOOD, () -> Ingredient.ofItems(Items.STICK)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 1));
//    public static final Weapon.Entry arcaneWand = wand("wand_arcane",
//            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 2));
//    public static final Weapon.Entry fireWand = wand("wand_fire",
//            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 2));
//    public static final Weapon.Entry frostWand = wand("wand_frost",
//            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 2));
//
//    public static final Weapon.Entry netheriteArcaneWand = wand("wand_netherite_arcane",
//            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 3));
//    public static final Weapon.Entry netheriteFireWand = wand("wand_netherite_fire",
//            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 3));
//    public static final Weapon.Entry netheriteFrostWand = wand("wand_netherite_frost",
//            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
//            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 3));
//
//
//    // MARK: Staves
//
//    private static final float staffAttackDamage = 4;
//    private static final float staffAttackSpeed = -3F;
//
    private static Weapon.Entry claymore(String name, Weapon.CustomMaterial material, float damage) {
        return claymore(null, name, material, damage);
    }

    private static Weapon.Entry claymore(String requiredMod, String name, Weapon.CustomMaterial material, float damage) {
        var settings = new Item.Settings().group(Group.PALADINS);
        var item = new SpellSwordItem(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -3F));
    }


    public static final Weapon.Entry stone_claymore = claymore("stone_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.ofItems(Items.COBBLESTONE)), 7.5F)
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));

    public static final Weapon.Entry iron_claymore = claymore("iron_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 8F)
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));

    public static final Weapon.Entry golden_claymore = claymore("golden_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 5F)
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));

    public static final Weapon.Entry diamond_claymore = claymore("diamond_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 9.5F)
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));

    public static final Weapon.Entry netherite_claymore = claymore("netherite_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 11F)
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));

//    // MARK: Register

    public static void register(Map<String, ItemConfig.Weapon> configs) {
//        if (Platform.isModLoaded("betternether")) {
//            staff("betternether", "staff_crystal_arcane",
//                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betterend:aeternium_ingot")))
//                    .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 6));
//        }
//        if (Platform.isModLoaded("betterend")) {
//            staff("betterend", "staff_ruby_fire",
//                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betternether:nether_ruby")))
//                    .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 6));
//            staff("betterend", "staff_smaragdant_frost",
//                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betterend:aeternium_ingot")))
//                    .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 6));
//        }

        Weapon.register(configs, entries);
    }
}
