package net.paladins.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.item.armor.Armor;

import java.util.concurrent.CompletableFuture;

/**
 * Generates all crafting recipes for the Paladins mod using Fabric's built-in API.
 * Conditional recipes (BetterNether/BetterEnd) are kept as hand-written JSONs.
 */
public class PaladinRecipes extends FabricRecipeProvider {

    private static final TagKey<Item> WOOD_STICKS = TagKey.of(Registries.ITEM.getKey(), Identifier.of("c", "wood_sticks"));

    public PaladinRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        generateWandRecipes(exporter);
        generateStaffRecipes(exporter);
        generateClaymoreRecipes(exporter);
        generateGreatHammerRecipes(exporter);
        generateMaceRecipes(exporter);
        generateShieldRecipes(exporter);
        generateArmorRecipes(exporter);
        generateOtherRecipes(exporter);
        generateNetheriteUpgrades(exporter);
    }

    // ========================================
    // WAND RECIPES
    // ========================================

    private void generateWandRecipes(RecipeExporter exporter) {
        // Acolyte Wand - string + sticks
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("acolyte_wand"))
                .pattern(" HH")
                .pattern(" SH")
                .pattern("H  ")
                .input('S', Items.STRING)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter);

        // Holy Wand - gold + iron
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("holy_wand"))
                .pattern(" A")
                .pattern("H ")
                .input('A', Items.GOLD_INGOT)
                .input('H', Items.IRON_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Holy Wand - diamond + gold
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_holy_wand"))
                .pattern(" A")
                .pattern("H ")
                .input('A', Items.DIAMOND)
                .input('H', Items.GOLD_INGOT)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // STAFF RECIPES
    // ========================================

    private void generateStaffRecipes(RecipeExporter exporter) {
        // Holy Staff - gold + iron
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("holy_staff"))
                .pattern(" AA")
                .pattern(" HA")
                .pattern("H  ")
                .input('A', Items.GOLD_INGOT)
                .input('H', Items.IRON_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Holy Staff - diamond + gold
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_holy_staff"))
                .pattern(" AA")
                .pattern(" HA")
                .pattern("H  ")
                .input('A', Items.DIAMOND)
                .input('H', Items.GOLD_INGOT)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // CLAYMORE RECIPES
    // ========================================

    private void generateClaymoreRecipes(RecipeExporter exporter) {
        // Stone Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("stone_claymore"))
                .pattern("  A")
                .pattern("AA ")
                .pattern("HA ")
                .input('A', ItemTags.STONE_TOOL_MATERIALS)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                .offerTo(exporter);

        // Iron Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("iron_claymore"))
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.IRON_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("golden_claymore"))
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.GOLD_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_claymore"))
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.DIAMOND)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // GREAT HAMMER RECIPES
    // ========================================

    private void generateGreatHammerRecipes(RecipeExporter exporter) {
        // Wooden Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("wooden_great_hammer"))
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', ItemTags.LOGS_THAT_BURN)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.OAK_LOG), conditionsFromItem(Items.OAK_LOG))
                .offerTo(exporter);

        // Stone Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("stone_great_hammer"))
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', ItemTags.STONE_TOOL_MATERIALS)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                .offerTo(exporter);

        // Iron Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("iron_great_hammer"))
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.IRON_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("golden_great_hammer"))
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.GOLD_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_great_hammer"))
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.DIAMOND)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // MACE RECIPES
    // ========================================

    private void generateMaceRecipes(RecipeExporter exporter) {
        // Iron Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("iron_mace"))
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.IRON_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("golden_mace"))
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.GOLD_INGOT)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_mace"))
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.DIAMOND)
                .input('H', WOOD_STICKS)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // SHIELD RECIPES
    // ========================================

    private void generateShieldRecipes(RecipeExporter exporter) {
        // Iron Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("iron_kite_shield"))
                .pattern("MLM")
                .pattern("MMM")
                .pattern(" M ")
                .input('L', Items.LEATHER)
                .input('M', Items.IRON_INGOT)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("golden_kite_shield"))
                .pattern("MLM")
                .pattern("MMM")
                .pattern(" M ")
                .input('L', Items.LEATHER)
                .input('M', Items.GOLD_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, item("diamond_kite_shield"))
                .pattern("MLM")
                .pattern("MMM")
                .pattern(" M ")
                .input('L', Items.LEATHER)
                .input('M', Items.DIAMOND)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // ARMOR RECIPES
    // ========================================

    private void generateArmorRecipes(RecipeExporter exporter) {
        // Paladin Armor - copper + iron
        generatePaladinArmorSet(exporter, Armors.paladinArmorSet_t1, Items.COPPER_INGOT, Items.IRON_INGOT);

        // Crusader Armor - gold + ghast_tear + iron
        generateCrusaderArmorSet(exporter, Armors.paladinArmorSet_t2, Items.GOLD_INGOT, Items.GHAST_TEAR, Items.IRON_INGOT);

        // Priest Robe - chain + wool
        generateRobeSet(exporter, Armors.priestArmorSet_t1, Items.CHAIN);

        // Prior Robe - gold + ghast_tear + wool
        generatePriorRobeSet(exporter, Armors.priestArmorSet_t2, Items.GOLD_INGOT, Items.GHAST_TEAR);
    }

    /**
     * Generate Paladin armor set (simple pattern with two materials)
     */
    private void generatePaladinArmorSet(RecipeExporter exporter, Armor.Set armorSet, Item primary, Item secondary) {
        // Helmet - pattern: "ICI" / "I I"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("ICI")
                .pattern("I I")
                .input('C', primary)
                .input('I', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter);

        // Chestplate - pattern: "I I" / "ICI" / "III"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("I I")
                .pattern("ICI")
                .pattern("III")
                .input('C', primary)
                .input('I', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter);

        // Leggings - pattern: "CCC" / "I I" / "I I"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("CCC")
                .pattern("I I")
                .pattern("I I")
                .input('C', primary)
                .input('I', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter);

        // Boots - pattern: "I I" / "C C"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("I I")
                .pattern("C C")
                .input('C', primary)
                .input('I', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter);
    }

    /**
     * Generate Crusader armor set (uses gold, ghast tear, and iron)
     */
    private void generateCrusaderArmorSet(RecipeExporter exporter, Armor.Set armorSet, Item gold, Item tear, Item iron) {
        // Helmet - pattern: "GTG" / "I I" / "III"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("GTG")
                .pattern("I I")
                .pattern("III")
                .input('G', gold)
                .input('T', tear)
                .input('I', iron)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Chestplate - pattern: "I I" / "GTG" / "IGI"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("I I")
                .pattern("GTG")
                .pattern("IGI")
                .input('G', gold)
                .input('T', tear)
                .input('I', iron)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Leggings - pattern: "GTG" / "I I" / "G G"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("GTG")
                .pattern("I I")
                .pattern("G G")
                .input('G', gold)
                .input('T', tear)
                .input('I', iron)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Boots - pattern: "I I" / "G G"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("I I")
                .pattern("G G")
                .input('G', gold)
                .input('I', iron)
                .criterion(hasItem(gold), conditionsFromItem(gold))
                .offerTo(exporter);
    }

    /**
     * Generate simple robe set (chain + wool)
     */
    private void generateRobeSet(RecipeExporter exporter, Armor.Set armorSet, Item specialIngredient) {
        // Helmet - pattern: "W W" / "WLW"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("W W")
                .pattern("WLW")
                .input('L', specialIngredient)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);

        // Chestplate - pattern: "L L" / "WWW" / "WWW"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("L L")
                .pattern("WWW")
                .pattern("WWW")
                .input('L', specialIngredient)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);

        // Leggings - pattern: "LLL" / "W W" / "W W"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("LLL")
                .pattern("W W")
                .pattern("W W")
                .input('L', specialIngredient)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);

        // Boots - pattern: "L L" / "W W"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("L L")
                .pattern("W W")
                .input('L', specialIngredient)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                .offerTo(exporter);
    }

    /**
     * Generate Prior robe set (gold + ghast_tear + wool)
     */
    private void generatePriorRobeSet(RecipeExporter exporter, Armor.Set armorSet, Item gold, Item tear) {
        // Helmet - pattern: "G G" / "WTW"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.head)
                .pattern("G G")
                .pattern("WTW")
                .input('G', gold)
                .input('T', tear)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Chestplate - pattern: "G G" / "WTW" / "WWW"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.chest)
                .pattern("G G")
                .pattern("WTW")
                .pattern("WWW")
                .input('G', gold)
                .input('T', tear)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Leggings - pattern: "GTG" / "W W" / "W W"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.legs)
                .pattern("GTG")
                .pattern("W W")
                .pattern("W W")
                .input('G', gold)
                .input('T', tear)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(tear), conditionsFromItem(tear))
                .offerTo(exporter);

        // Boots - pattern: "G G" / "W W"
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, armorSet.feet)
                .pattern("G G")
                .pattern("W W")
                .input('G', gold)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(gold), conditionsFromItem(gold))
                .offerTo(exporter);
    }

    // ========================================
    // OTHER RECIPES
    // ========================================

    private void generateOtherRecipes(RecipeExporter exporter) {
        // Monk Workbench
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, item("monk_workbench"))
                .pattern("GTG")
                .pattern(" # ")
                .pattern("###")
                .input('G', Items.GOLD_INGOT)
                .input('T', Items.GHAST_TEAR)
                .input('#', Items.POLISHED_ANDESITE)
                .criterion(hasItem(Items.GHAST_TEAR), conditionsFromItem(Items.GHAST_TEAR))
                .offerTo(exporter);
    }

    // ========================================
    // NETHERITE UPGRADE RECIPES
    // ========================================

    private void generateNetheriteUpgrades(RecipeExporter exporter) {
        // Weapon upgrades
        offerNetheriteUpgradeRecipe(exporter, item("diamond_holy_wand"), RecipeCategory.COMBAT, item("netherite_holy_wand"));
        offerNetheriteUpgradeRecipe(exporter, item("diamond_holy_staff"), RecipeCategory.COMBAT, item("netherite_holy_staff"));
        offerNetheriteUpgradeRecipe(exporter, item("diamond_claymore"), RecipeCategory.COMBAT, item("netherite_claymore"));
        offerNetheriteUpgradeRecipe(exporter, item("diamond_great_hammer"), RecipeCategory.COMBAT, item("netherite_great_hammer"));
        offerNetheriteUpgradeRecipe(exporter, item("diamond_mace"), RecipeCategory.COMBAT, item("netherite_mace"));
        offerNetheriteUpgradeRecipe(exporter, item("diamond_kite_shield"), RecipeCategory.COMBAT, item("netherite_kite_shield"));

        // Crusader armor upgrades
        offerNetheriteUpgradeRecipe(exporter, item("crusader_armor_head"), RecipeCategory.COMBAT, item("netherite_crusader_armor_head"));
        offerNetheriteUpgradeRecipe(exporter, item("crusader_armor_chest"), RecipeCategory.COMBAT, item("netherite_crusader_armor_chest"));
        offerNetheriteUpgradeRecipe(exporter, item("crusader_armor_legs"), RecipeCategory.COMBAT, item("netherite_crusader_armor_legs"));
        offerNetheriteUpgradeRecipe(exporter, item("crusader_armor_feet"), RecipeCategory.COMBAT, item("netherite_crusader_armor_feet"));

        // Prior robe upgrades
        offerNetheriteUpgradeRecipe(exporter, item("prior_robe_head"), RecipeCategory.COMBAT, item("netherite_prior_robe_head"));
        offerNetheriteUpgradeRecipe(exporter, item("prior_robe_chest"), RecipeCategory.COMBAT, item("netherite_prior_robe_chest"));
        offerNetheriteUpgradeRecipe(exporter, item("prior_robe_legs"), RecipeCategory.COMBAT, item("netherite_prior_robe_legs"));
        offerNetheriteUpgradeRecipe(exporter, item("prior_robe_feet"), RecipeCategory.COMBAT, item("netherite_prior_robe_feet"));
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    /**
     * Get a paladin mod item by name
     */
    private Item item(String name) {
        return Registries.ITEM.get(Identifier.of(PaladinsMod.ID, name));
    }

    @Override
    public String getName() {
        return "Paladin Crafting Recipes";
    }
}
