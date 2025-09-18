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
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType type) {
        var entry = new Weapon.Entry(PaladinsMod.ID, name, material, factory, defaults, type);
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
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

    // MARK: Claymores

    private static final float claymoreHealing = 0;

    private static Weapon.Entry claymore(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, -3F), Equipment.WeaponType.CLAYMORE);
    }

    public static final Weapon.Entry stone_claymore = claymore("stone_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.ofItems(Items.COBBLESTONE)), 6.8F)
            .loot(Equipment.LootProperties.of(0));
    public static final Weapon.Entry iron_claymore = claymore("iron_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 8.3F)
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry golden_claymore = claymore("golden_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 5.2F)
            .loot(Equipment.LootProperties.of("golden_weapon"));
    public static final Weapon.Entry diamond_claymore = claymore("diamond_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 9.9F)
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_claymore = claymore("netherite_claymore",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 11.5F)
            .loot(Equipment.LootProperties.of(3));

    // MARK: Hammers

    private static final float hammerHealing = 0;

    private static Weapon.Entry hammer(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, SpellWeaponItem::new, new WeaponConfig(damage, -3.2F), Equipment.WeaponType.HAMMER);
    }

    public static final Weapon.Entry wooden_great_hammer = hammer("wooden_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.WOOD, () -> Ingredient.fromTag(ItemTags.PLANKS)), 6.6F)
            .loot(Equipment.LootProperties.of(0));
    public static final Weapon.Entry stone_great_hammer = hammer("stone_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.STONE, () -> Ingredient.fromTag(ItemTags.STONE_TOOL_MATERIALS)), 8.5F)
            .loot(Equipment.LootProperties.of(0));
    public static final Weapon.Entry iron_great_hammer = hammer("iron_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 10.3F)
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry golden_great_hammer = hammer("golden_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 6.6F)
            .loot(Equipment.LootProperties.of("golden_weapon"));
    public static final Weapon.Entry diamond_great_hammer = hammer("diamond_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 12.2F)
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_great_hammer = hammer("netherite_great_hammer",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 14.1F)
            .loot(Equipment.LootProperties.of(3));


    // MARK: Maces

    private static final float maceHealing = 0;

    private static Weapon.Entry mace(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, SpellWeaponItem::new, new WeaponConfig(damage, -2.8F), Equipment.WeaponType.MACE);
    }

    public static final Weapon.Entry iron_mace = mace("iron_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 7F)
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry golden_mace = mace("golden_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 4.3F)
            .loot(Equipment.LootProperties.of("golden_weapon"));
    public static final Weapon.Entry diamond_mace = mace("diamond_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 8.3F)
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_mace = mace("netherite_mace",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 9.6F)
            .loot(Equipment.LootProperties.of(3));

    // MARK: Wands

    private static final float wandAttackDamage = 2;
    private static final float wandAttackSpeed = -2.4F;
    private static Weapon.Entry wand(String name, Weapon.CustomMaterial material) {
        return entry(name, material, StaffItem::new, new WeaponConfig(wandAttackDamage, wandAttackSpeed), Equipment.WeaponType.HEALING_WAND);
    }

    public static final Weapon.Entry acolyte_wand = wand("acolyte_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.WOOD, () -> Ingredient.ofItems(Items.STICK)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 3))
            .loot(Equipment.LootProperties.of(0));
    public static final Weapon.Entry holy_wand = wand("holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 3.5F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry diamond_holy_wand = wand("diamond_holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 4F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_holy_wand = wand("netherite_holy_wand",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 4.5F))
            .loot(Equipment.LootProperties.of(3));

    // MARK: Staves

    private static final float staffAttackDamage = 4;
    private static final float staffAttackSpeed = -3F;

    private static Weapon.Entry staff(String name, Weapon.CustomMaterial material) {
        return entry(name, material, StaffItem::new, new WeaponConfig(staffAttackDamage, staffAttackSpeed), Equipment.WeaponType.HEALING_STAFF);
    }

    public static final Weapon.Entry holy_staff = staff("holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.GOLD_INGOT)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 4))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry diamond_holy_staff = staff("diamond_holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 5))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_holy_staff = staff("netherite_holy_staff",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)))
            .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 6))
            .loot(Equipment.LootProperties.of(3));

    // MARK: Register

    public static void register(Map<String, WeaponConfig> configs) {
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_NETHER)) {
            var repair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            staff("ruby_holy_staff",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair))
                    .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 7))
                    .loot(Equipment.LootProperties.of(4));
            claymore("ruby_claymore", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 13F)
                    .loot(Equipment.LootProperties.of(4));
            hammer("ruby_great_hammer", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 16F)
                    .loot(Equipment.LootProperties.of(4));
            mace("ruby_mace", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 11F)
                    .loot(Equipment.LootProperties.of(4));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(BETTER_END)) {
            var repair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_END), Items.NETHERITE_INGOT);
            claymore("aeternium_claymore", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 13F)
                    .loot(Equipment.LootProperties.of(4));
            hammer("aeternium_great_hammer", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 16F)
                    .loot(Equipment.LootProperties.of(4));
            mace("aeternium_mace", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 11F)
                    .loot(Equipment.LootProperties.of(4));
        }
        if (PaladinsMod.tweaksConfig.value.ignore_items_required_mods || FabricLoader.getInstance().isModLoaded(AETHER)) {
            var repair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            staff("aether_holy_staff",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair))
                    .attribute(AttributeModifier.bonus(SpellSchools.HEALING.id, 7))
                    .loot(Equipment.LootProperties.of("aether"));
            claymore("aether_claymore", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 13F)
                    .loot(Equipment.LootProperties.of("aether"));
            hammer("aether_great_hammer", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 16F)
                    .loot(Equipment.LootProperties.of("aether"));
            mace("aether_mace", Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair), 11F)
                    .loot(Equipment.LootProperties.of("aether"));
        }

        Weapon.register(configs, entries, Group.KEY);
    }
}
