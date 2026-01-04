package net.paladins.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.item.armor.Armor;

import net.paladins.item.PaladinWeapons;
import net.paladins.item.PaladinShields;
import net.paladins.block.PaladinBlocks;

import java.util.concurrent.CompletableFuture;

/**
 * Generates all crafting recipes for the Paladins mod using Fabric's built-in API.
 * Conditional recipes (BetterNether/BetterEnd) are kept as hand-written JSONs.
 */
public class PaladinRecipes extends FabricRecipeProvider {

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
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.acolyte_wand.item())
                .pattern(" HH")
                .pattern(" SH")
                .pattern("H  ")
                .input('S', Items.STRING)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter);

        // Holy Wand - gold + iron
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.holy_wand.item())
                .pattern(" A")
                .pattern("H ")
                .input('A', Items.GOLD_INGOT)
                .input('H', Items.IRON_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Holy Wand - diamond + gold
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_wand.item())
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.holy_staff.item())
                .pattern(" AA")
                .pattern(" HA")
                .pattern("H  ")
                .input('A', Items.GOLD_INGOT)
                .input('H', Items.IRON_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Holy Staff - diamond + gold
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_staff.item())
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.stone_claymore.item())
                .pattern("  A")
                .pattern("AA ")
                .pattern("HA ")
                .input('A', ItemTags.STONE_TOOL_MATERIALS)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                .offerTo(exporter);

        // Iron Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.iron_claymore.item())
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.IRON_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.golden_claymore.item())
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.GOLD_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Claymore
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.diamond_claymore.item())
                .pattern("  B")
                .pattern("BB ")
                .pattern("HB ")
                .input('B', Items.DIAMOND)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // GREAT HAMMER RECIPES
    // ========================================

    private void generateGreatHammerRecipes(RecipeExporter exporter) {
        // Wooden Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.wooden_great_hammer.item())
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', ItemTags.LOGS_THAT_BURN)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.OAK_LOG), conditionsFromItem(Items.OAK_LOG))
                .offerTo(exporter);

        // Stone Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.stone_great_hammer.item())
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', ItemTags.STONE_TOOL_MATERIALS)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                .offerTo(exporter);

        // Iron Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.iron_great_hammer.item())
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.IRON_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.golden_great_hammer.item())
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.GOLD_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Great Hammer
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.diamond_great_hammer.item())
                .pattern(" BB")
                .pattern(" BB")
                .pattern("H  ")
                .input('B', Items.DIAMOND)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // MACE RECIPES
    // ========================================

    private void generateMaceRecipes(RecipeExporter exporter) {
        // Iron Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.iron_mace.item())
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.IRON_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.golden_mace.item())
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.GOLD_INGOT)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Mace
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinWeapons.diamond_mace.item())
                .pattern(" B")
                .pattern("HB")
                .input('B', Items.DIAMOND)
                .input('H', Items.STICK)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    // ========================================
    // SHIELD RECIPES
    // ========================================

    private void generateShieldRecipes(RecipeExporter exporter) {
        // Iron Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinShields.iron_kite_shield.holder().item)
                .pattern("MLM")
                .pattern("MMM")
                .pattern(" M ")
                .input('L', Items.LEATHER)
                .input('M', Items.IRON_INGOT)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        // Golden Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinShields.golden_kite_shield.holder().item)
                .pattern("MLM")
                .pattern("MMM")
                .pattern(" M ")
                .input('L', Items.LEATHER)
                .input('M', Items.GOLD_INGOT)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        // Diamond Kite Shield
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PaladinShields.diamond_kite_shield.holder().item)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, PaladinBlocks.MONK_WORKBENCH_BLOCK)
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
        offerNetheriteUpgradeRecipe(exporter, PaladinWeapons.diamond_holy_wand.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_wand.item());
        offerNetheriteUpgradeRecipe(exporter, PaladinWeapons.diamond_holy_staff.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_staff.item());
        offerNetheriteUpgradeRecipe(exporter, PaladinWeapons.diamond_claymore.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_claymore.item());
        offerNetheriteUpgradeRecipe(exporter, PaladinWeapons.diamond_great_hammer.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_great_hammer.item());
        offerNetheriteUpgradeRecipe(exporter, PaladinWeapons.diamond_mace.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_mace.item());
        offerNetheriteUpgradeRecipe(exporter, PaladinShields.diamond_kite_shield.holder().item, RecipeCategory.COMBAT, PaladinShields.netherite_kite_shield.holder().item);

        // Crusader armor upgrades
        offerNetheriteUpgradeRecipe(exporter, Armors.paladinArmorSet_t2.head, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.head);
        offerNetheriteUpgradeRecipe(exporter, Armors.paladinArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.chest);
        offerNetheriteUpgradeRecipe(exporter, Armors.paladinArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.legs);
        offerNetheriteUpgradeRecipe(exporter, Armors.paladinArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.feet);

        // Prior robe upgrades
        offerNetheriteUpgradeRecipe(exporter, Armors.priestArmorSet_t2.head, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.head);
        offerNetheriteUpgradeRecipe(exporter, Armors.priestArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.chest);
        offerNetheriteUpgradeRecipe(exporter, Armors.priestArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.legs);
        offerNetheriteUpgradeRecipe(exporter, Armors.priestArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.feet);
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    @Override
    public String getName() {
        return "Paladin Crafting Recipes";
    }
}
