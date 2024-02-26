package net.paladins.item;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

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
            var item = Registries.ITEM.get(id);
            var ingredient = item != null ? item : fallback;
            return Ingredient.ofItems(ingredient);
        };
    }

    // MARK: Claymores

    private static final float claymoreHealing = 0;

    private static Weapon.Entry claymore(String name, Weapon.CustomMaterial material, float damage) {
        return claymore(null, name, material, damage);
    }

    private static Weapon.Entry claymore(String requiredMod, String name, Weapon.CustomMaterial material, float damage) {
        var settings = new Item.Settings();
        var item = new SpellSwordItem(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -3F));
    }

    public static final Weapon.Entry stone_claymore = claymore("stone_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.ofItems(Items.COBBLESTONE)), 7.5F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));
    public static final Weapon.Entry iron_claymore = claymore("iron_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 8F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));
    public static final Weapon.Entry golden_claymore = claymore("golden_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 5F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));
    public static final Weapon.Entry diamond_claymore = claymore("diamond_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 9.5F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));
    public static final Weapon.Entry netherite_claymore = claymore("netherite_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 11F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));

    // MARK: Hammers

    private static final float hammerHealing = 0;

    private static Weapon.Entry hammer(String name, Weapon.CustomMaterial material, float damage) {
        return hammer(null, name, material, damage);
    }

    private static Weapon.Entry hammer(String requiredMod, String name, Weapon.CustomMaterial material, float damage) {
        var settings = new Item.Settings();
        var item = new SpellWeaponItem(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -3.2F));
    }

    public static final Weapon.Entry wooden_great_hammer = hammer("wooden_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.WOOD, () -> Ingredient.fromTag(ItemTags.PLANKS)), 5F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
    public static final Weapon.Entry stone_great_hammer = hammer("stone_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.fromTag(ItemTags.STONE_TOOL_MATERIALS)), 8F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
    public static final Weapon.Entry iron_great_hammer = hammer("iron_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 10F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
    public static final Weapon.Entry golden_great_hammer = hammer("golden_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 10F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
    public static final Weapon.Entry diamond_great_hammer = hammer("diamond_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 11F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
    public static final Weapon.Entry netherite_great_hammer = hammer("netherite_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 13F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));


    // MARK: Maces

    private static final float maceHealing = 0;

    private static Weapon.Entry mace(String name, Weapon.CustomMaterial material, float damage) {
        return mace(null, name, material, damage);
    }

    private static Weapon.Entry mace(String requiredMod, String name, Weapon.CustomMaterial material, float damage) {
        var settings = new Item.Settings();
        var item = new SpellWeaponItem(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -2.8F));
    }

//    public static final Weapon.Entry stone_mace = mace("stone_mace",
//            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.ofItems(Items.COBBLESTONE)), 5F)
//            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));
    public static final Weapon.Entry iron_mace = mace("iron_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 6F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), maceHealing));
    public static final Weapon.Entry golden_mace = mace("golden_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 5F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), maceHealing));
    public static final Weapon.Entry diamond_mace = mace("diamond_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 8F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), maceHealing));
    public static final Weapon.Entry netherite_mace = mace("netherite_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 9F)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), maceHealing));

    // MARK: Wands

    private static final float wandAttackDamage = 2;
    private static final float wandAttackSpeed = -2.4F;
    private static Weapon.Entry wand(String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new StaffItem(material, settings);
        return entry(name, material, item, new ItemConfig.Weapon(wandAttackDamage, wandAttackSpeed));
    }

    public static final Weapon.Entry acolyte_wand = wand("acolyte_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.WOOD, () -> Ingredient.ofItems(Items.STICK)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 1));
    public static final Weapon.Entry holy_wand = wand("holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2));
    public static final Weapon.Entry diamond_holy_wand = wand("diamond_holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 2.5F));
    public static final Weapon.Entry netherite_holy_wand = wand("netherite_holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 3));

    // MARK: Staves

    private static final float staffAttackDamage = 4;
    private static final float staffAttackSpeed = -3F;

    private static Weapon.Entry staff(String name, Weapon.CustomMaterial material) {
        return staff(null, name, material);
    }

    private static Weapon.Entry staff(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new StaffItem(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }

    public static final Weapon.Entry holy_staff = staff("holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 3));
    public static final Weapon.Entry diamond_holy_staff = staff("diamond_holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 4));
    public static final Weapon.Entry netherite_holy_staff = staff("netherite_holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 5));

    // MARK: Register

    public static void register(Map<String, ItemConfig.Weapon> configs) {
        if (FabricLoader.getInstance().isModLoaded("betternether")) {
            staff("betternether", "ruby_holy_staff",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betternether:nether_ruby")))
                    .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), 6));
        }
        if (FabricLoader.getInstance().isModLoaded("betterend")) {
            claymore("betterend", "aeternium_claymore",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betterend:aeternium_ingot")), 12)
                    .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), claymoreHealing));
            hammer("betterend", "aeternium_great_hammer",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, ingredient("betterend:aeternium_ingot")), 14)
                    .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.HEALING), hammerHealing));
        }

        Weapon.register(configs, entries, Group.KEY);
    }
}
