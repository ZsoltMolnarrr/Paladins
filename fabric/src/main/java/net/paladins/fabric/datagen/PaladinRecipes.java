package net.paladins.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.paladins.item.armor.Armors;
import net.spell_engine.rpg_series.item.Armor;

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
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
        @Override
        public void generate() {
            generateWandRecipes();
            generateStaffRecipes();
            generateClaymoreRecipes();
            generateGreatHammerRecipes();
            generateMaceRecipes();
            generateShieldRecipes();
            generateArmorRecipes();
            generateOtherRecipes();
            generateNetheriteUpgrades();
        }

        // ========================================
        // WAND RECIPES
        // ========================================

        private void generateWandRecipes() {
            // Acolyte Wand - string + sticks
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.acolyte_wand.item())
                    .pattern(" HH")
                    .pattern(" SH")
                    .pattern("H  ")
                    .input('S', Items.STRING)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                    .offerTo(this.exporter);

            // Holy Wand - gold + iron
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.holy_wand.item())
                    .pattern(" A")
                    .pattern("H ")
                    .input('A', Items.GOLD_INGOT)
                    .input('H', Items.IRON_INGOT)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Holy Wand - diamond + gold
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_wand.item())
                    .pattern(" A")
                    .pattern("H ")
                    .input('A', Items.DIAMOND)
                    .input('H', Items.GOLD_INGOT)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // STAFF RECIPES
        // ========================================

        private void generateStaffRecipes() {
            // Holy Staff - gold + iron
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.holy_staff.item())
                    .pattern(" AA")
                    .pattern(" HA")
                    .pattern("H  ")
                    .input('A', Items.GOLD_INGOT)
                    .input('H', Items.IRON_INGOT)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Holy Staff - diamond + gold
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_staff.item())
                    .pattern(" AA")
                    .pattern(" HA")
                    .pattern("H  ")
                    .input('A', Items.DIAMOND)
                    .input('H', Items.GOLD_INGOT)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // CLAYMORE RECIPES
        // ========================================

        private void generateClaymoreRecipes() {
            // Stone Claymore
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.stone_claymore.item())
                    .pattern("  A")
                    .pattern("AA ")
                    .pattern("HA ")
                    .input('A', ItemTags.STONE_TOOL_MATERIALS)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                    .offerTo(this.exporter);

            // Iron Claymore
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.iron_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .input('B', Items.IRON_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(this.exporter);

            // Golden Claymore
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.golden_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .input('B', Items.GOLD_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Claymore
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .input('B', Items.DIAMOND)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // GREAT HAMMER RECIPES
        // ========================================

        private void generateGreatHammerRecipes() {
            // Wooden Great Hammer
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.wooden_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .input('B', ItemTags.LOGS_THAT_BURN)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.OAK_LOG), conditionsFromItem(Items.OAK_LOG))
                    .offerTo(this.exporter);

            // Stone Great Hammer
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.stone_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .input('B', ItemTags.STONE_TOOL_MATERIALS)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                    .offerTo(this.exporter);

            // Iron Great Hammer
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.iron_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .input('B', Items.IRON_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(this.exporter);

            // Golden Great Hammer
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.golden_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .input('B', Items.GOLD_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Great Hammer
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .input('B', Items.DIAMOND)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // MACE RECIPES
        // ========================================

        private void generateMaceRecipes() {
            // Iron Mace
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.iron_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .input('B', Items.IRON_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(this.exporter);

            // Golden Mace
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.golden_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .input('B', Items.GOLD_INGOT)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Mace
            createShaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .input('B', Items.DIAMOND)
                    .input('H', Items.STICK)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // SHIELD RECIPES
        // ========================================

        private void generateShieldRecipes() {
            // Iron Kite Shield
            createShaped(RecipeCategory.COMBAT, PaladinShields.iron_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .input('L', Items.LEATHER)
                    .input('M', Items.IRON_INGOT)
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(this.exporter);

            // Golden Kite Shield
            createShaped(RecipeCategory.COMBAT, PaladinShields.golden_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .input('L', Items.LEATHER)
                    .input('M', Items.GOLD_INGOT)
                    .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(this.exporter);

            // Diamond Kite Shield
            createShaped(RecipeCategory.COMBAT, PaladinShields.diamond_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .input('L', Items.LEATHER)
                    .input('M', Items.DIAMOND)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(this.exporter);
        }

        // ========================================
        // ARMOR RECIPES
        // ========================================

        private void generateArmorRecipes() {
            // Paladin Armor - copper + iron
            generatePaladinArmorSet(Armors.paladinArmorSet_t1, Items.COPPER_INGOT, Items.IRON_INGOT);

            // Crusader Armor - gold + ghast_tear + iron
            generateCrusaderArmorSet(Armors.paladinArmorSet_t2, Items.GOLD_INGOT, Items.GHAST_TEAR, Items.IRON_INGOT);

            // Priest Robe - chain + wool
            generateRobeSet(Armors.priestArmorSet_t1, Items.IRON_CHAIN);

            // Prior Robe - gold + ghast_tear + wool
            generatePriorRobeSet(Armors.priestArmorSet_t2, Items.GOLD_INGOT, Items.GHAST_TEAR);
        }

        /**
         * Generate Paladin armor set (simple pattern with two materials)
         */
        private void generatePaladinArmorSet(Armor.Set armorSet, Item primary, Item secondary) {
            // Helmet - pattern: "ICI" / "I I"
            createShaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("ICI")
                    .pattern("I I")
                    .input('C', primary)
                    .input('I', secondary)
                    .criterion(hasItem(primary), conditionsFromItem(primary))
                    .offerTo(this.exporter);

            // Chestplate - pattern: "I I" / "ICI" / "III"
            createShaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("I I")
                    .pattern("ICI")
                    .pattern("III")
                    .input('C', primary)
                    .input('I', secondary)
                    .criterion(hasItem(primary), conditionsFromItem(primary))
                    .offerTo(this.exporter);

            // Leggings - pattern: "CCC" / "I I" / "I I"
            createShaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("CCC")
                    .pattern("I I")
                    .pattern("I I")
                    .input('C', primary)
                    .input('I', secondary)
                    .criterion(hasItem(primary), conditionsFromItem(primary))
                    .offerTo(this.exporter);

            // Boots - pattern: "I I" / "C C"
            createShaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("I I")
                    .pattern("C C")
                    .input('C', primary)
                    .input('I', secondary)
                    .criterion(hasItem(primary), conditionsFromItem(primary))
                    .offerTo(this.exporter);
        }

        /**
         * Generate Crusader armor set (uses gold, ghast tear, and iron)
         */
        private void generateCrusaderArmorSet(Armor.Set armorSet, Item gold, Item tear, Item iron) {
            // Helmet - pattern: "GTG" / "I I" / "III"
            createShaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("GTG")
                    .pattern("I I")
                    .pattern("III")
                    .input('G', gold)
                    .input('T', tear)
                    .input('I', iron)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Chestplate - pattern: "I I" / "GTG" / "IGI"
            createShaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("I I")
                    .pattern("GTG")
                    .pattern("IGI")
                    .input('G', gold)
                    .input('T', tear)
                    .input('I', iron)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Leggings - pattern: "GTG" / "I I" / "G G"
            createShaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("GTG")
                    .pattern("I I")
                    .pattern("G G")
                    .input('G', gold)
                    .input('T', tear)
                    .input('I', iron)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Boots - pattern: "I I" / "G G"
            createShaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("I I")
                    .pattern("G G")
                    .input('G', gold)
                    .input('I', iron)
                    .criterion(hasItem(gold), conditionsFromItem(gold))
                    .offerTo(this.exporter);
        }

        /**
         * Generate simple robe set (chain + wool)
         */
        private void generateRobeSet(Armor.Set armorSet, Item specialIngredient) {
            // Helmet - pattern: "W W" / "WLW"
            createShaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("W W")
                    .pattern("WLW")
                    .input('L', specialIngredient)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                    .offerTo(this.exporter);

            // Chestplate - pattern: "L L" / "WWW" / "WWW"
            createShaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("L L")
                    .pattern("WWW")
                    .pattern("WWW")
                    .input('L', specialIngredient)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                    .offerTo(this.exporter);

            // Leggings - pattern: "LLL" / "W W" / "W W"
            createShaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("LLL")
                    .pattern("W W")
                    .pattern("W W")
                    .input('L', specialIngredient)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                    .offerTo(this.exporter);

            // Boots - pattern: "L L" / "W W"
            createShaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("L L")
                    .pattern("W W")
                    .input('L', specialIngredient)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(specialIngredient), conditionsFromItem(specialIngredient))
                    .offerTo(this.exporter);
        }

        /**
         * Generate Prior robe set (gold + ghast_tear + wool)
         */
        private void generatePriorRobeSet(Armor.Set armorSet, Item gold, Item tear) {
            // Helmet - pattern: "G G" / "WTW"
            createShaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("G G")
                    .pattern("WTW")
                    .input('G', gold)
                    .input('T', tear)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Chestplate - pattern: "G G" / "WTW" / "WWW"
            createShaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("G G")
                    .pattern("WTW")
                    .pattern("WWW")
                    .input('G', gold)
                    .input('T', tear)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Leggings - pattern: "GTG" / "W W" / "W W"
            createShaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("GTG")
                    .pattern("W W")
                    .pattern("W W")
                    .input('G', gold)
                    .input('T', tear)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(tear), conditionsFromItem(tear))
                    .offerTo(this.exporter);

            // Boots - pattern: "G G" / "W W"
            createShaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("G G")
                    .pattern("W W")
                    .input('G', gold)
                    .input('W', ItemTags.WOOL)
                    .criterion(hasItem(gold), conditionsFromItem(gold))
                    .offerTo(this.exporter);
        }

        // ========================================
        // OTHER RECIPES
        // ========================================

        private void generateOtherRecipes() {
            // Monk Workbench
            createShaped(RecipeCategory.MISC, PaladinBlocks.MONK_WORKBENCH_BLOCK)
                    .pattern("GTG")
                    .pattern(" # ")
                    .pattern("###")
                    .input('G', Items.GOLD_INGOT)
                    .input('T', Items.GHAST_TEAR)
                    .input('#', Items.POLISHED_ANDESITE)
                    .criterion(hasItem(Items.GHAST_TEAR), conditionsFromItem(Items.GHAST_TEAR))
                    .offerTo(this.exporter);
        }

        // ========================================
        // NETHERITE UPGRADE RECIPES
        // ========================================

        private void generateNetheriteUpgrades() {
            // Weapon upgrades
            offerNetheriteUpgradeRecipe(PaladinWeapons.diamond_holy_wand.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_wand.item());
            offerNetheriteUpgradeRecipe(PaladinWeapons.diamond_holy_staff.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_staff.item());
            offerNetheriteUpgradeRecipe(PaladinWeapons.diamond_claymore.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_claymore.item());
            offerNetheriteUpgradeRecipe(PaladinWeapons.diamond_great_hammer.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_great_hammer.item());
            offerNetheriteUpgradeRecipe(PaladinWeapons.diamond_mace.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_mace.item());
            offerNetheriteUpgradeRecipe(PaladinShields.diamond_kite_shield.item(), RecipeCategory.COMBAT, PaladinShields.netherite_kite_shield.item());

            // Crusader armor upgrades
            offerNetheriteUpgradeRecipe(Armors.paladinArmorSet_t2.head, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.head);
            offerNetheriteUpgradeRecipe(Armors.paladinArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.chest);
            offerNetheriteUpgradeRecipe(Armors.paladinArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.legs);
            offerNetheriteUpgradeRecipe(Armors.paladinArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.feet);

            // Prior robe upgrades
            offerNetheriteUpgradeRecipe(Armors.priestArmorSet_t2.head, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.head);
            offerNetheriteUpgradeRecipe(Armors.priestArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.chest);
            offerNetheriteUpgradeRecipe(Armors.priestArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.legs);
            offerNetheriteUpgradeRecipe(Armors.priestArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.feet);
        }



        };
    }

    @Override
    public String getName() {
        return "Paladin Crafting Recipes";
    }
}
