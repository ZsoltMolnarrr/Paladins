package net.paladins.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

    public PaladinRecipes(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
        @Override
        public void buildRecipes() {
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
            shaped(RecipeCategory.COMBAT, PaladinWeapons.acolyte_wand.item())
                    .pattern(" HH")
                    .pattern(" SH")
                    .pattern("H  ")
                    .define('S', Items.STRING)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                    .save(this.output);

            // Holy Wand - gold + iron
            shaped(RecipeCategory.COMBAT, PaladinWeapons.holy_wand.item())
                    .pattern(" A")
                    .pattern("H ")
                    .define('A', Items.GOLD_INGOT)
                    .define('H', Items.IRON_INGOT)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Holy Wand - diamond + gold
            shaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_wand.item())
                    .pattern(" A")
                    .pattern("H ")
                    .define('A', Items.DIAMOND)
                    .define('H', Items.GOLD_INGOT)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
        }

        // ========================================
        // STAFF RECIPES
        // ========================================

        private void generateStaffRecipes() {
            // Holy Staff - gold + iron
            shaped(RecipeCategory.COMBAT, PaladinWeapons.holy_staff.item())
                    .pattern(" AA")
                    .pattern(" HA")
                    .pattern("H  ")
                    .define('A', Items.GOLD_INGOT)
                    .define('H', Items.IRON_INGOT)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Holy Staff - diamond + gold
            shaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_holy_staff.item())
                    .pattern(" AA")
                    .pattern(" HA")
                    .pattern("H  ")
                    .define('A', Items.DIAMOND)
                    .define('H', Items.GOLD_INGOT)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
        }

        // ========================================
        // CLAYMORE RECIPES
        // ========================================

        private void generateClaymoreRecipes() {
            // Stone Claymore
            shaped(RecipeCategory.COMBAT, PaladinWeapons.stone_claymore.item())
                    .pattern("  A")
                    .pattern("AA ")
                    .pattern("HA ")
                    .define('A', ItemTags.STONE_TOOL_MATERIALS)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
                    .save(this.output);

            // Iron Claymore
            shaped(RecipeCategory.COMBAT, PaladinWeapons.iron_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .define('B', Items.IRON_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                    .save(this.output);

            // Golden Claymore
            shaped(RecipeCategory.COMBAT, PaladinWeapons.golden_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .define('B', Items.GOLD_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Claymore
            shaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_claymore.item())
                    .pattern("  B")
                    .pattern("BB ")
                    .pattern("HB ")
                    .define('B', Items.DIAMOND)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
        }

        // ========================================
        // GREAT HAMMER RECIPES
        // ========================================

        private void generateGreatHammerRecipes() {
            // Wooden Great Hammer
            shaped(RecipeCategory.COMBAT, PaladinWeapons.wooden_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .define('B', ItemTags.LOGS_THAT_BURN)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.OAK_LOG), has(Items.OAK_LOG))
                    .save(this.output);

            // Stone Great Hammer
            shaped(RecipeCategory.COMBAT, PaladinWeapons.stone_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .define('B', ItemTags.STONE_TOOL_MATERIALS)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
                    .save(this.output);

            // Iron Great Hammer
            shaped(RecipeCategory.COMBAT, PaladinWeapons.iron_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .define('B', Items.IRON_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                    .save(this.output);

            // Golden Great Hammer
            shaped(RecipeCategory.COMBAT, PaladinWeapons.golden_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .define('B', Items.GOLD_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Great Hammer
            shaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_great_hammer.item())
                    .pattern(" BB")
                    .pattern(" BB")
                    .pattern("H  ")
                    .define('B', Items.DIAMOND)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
        }

        // ========================================
        // MACE RECIPES
        // ========================================

        private void generateMaceRecipes() {
            // Iron Mace
            shaped(RecipeCategory.COMBAT, PaladinWeapons.iron_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .define('B', Items.IRON_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                    .save(this.output);

            // Golden Mace
            shaped(RecipeCategory.COMBAT, PaladinWeapons.golden_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .define('B', Items.GOLD_INGOT)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Mace
            shaped(RecipeCategory.COMBAT, PaladinWeapons.diamond_mace.item())
                    .pattern(" B")
                    .pattern("HB")
                    .define('B', Items.DIAMOND)
                    .define('H', Items.STICK)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
        }

        // ========================================
        // SHIELD RECIPES
        // ========================================

        private void generateShieldRecipes() {
            // Iron Kite Shield
            shaped(RecipeCategory.COMBAT, PaladinShields.iron_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .define('L', Items.LEATHER)
                    .define('M', Items.IRON_INGOT)
                    .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                    .save(this.output);

            // Golden Kite Shield
            shaped(RecipeCategory.COMBAT, PaladinShields.golden_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .define('L', Items.LEATHER)
                    .define('M', Items.GOLD_INGOT)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                    .save(this.output);

            // Diamond Kite Shield
            shaped(RecipeCategory.COMBAT, PaladinShields.diamond_kite_shield.item())
                    .pattern("MLM")
                    .pattern("MMM")
                    .pattern(" M ")
                    .define('L', Items.LEATHER)
                    .define('M', Items.DIAMOND)
                    .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                    .save(this.output);
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
            shaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("ICI")
                    .pattern("I I")
                    .define('C', primary)
                    .define('I', secondary)
                    .unlockedBy(getHasName(primary), has(primary))
                    .save(this.output);

            // Chestplate - pattern: "I I" / "ICI" / "III"
            shaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("I I")
                    .pattern("ICI")
                    .pattern("III")
                    .define('C', primary)
                    .define('I', secondary)
                    .unlockedBy(getHasName(primary), has(primary))
                    .save(this.output);

            // Leggings - pattern: "CCC" / "I I" / "I I"
            shaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("CCC")
                    .pattern("I I")
                    .pattern("I I")
                    .define('C', primary)
                    .define('I', secondary)
                    .unlockedBy(getHasName(primary), has(primary))
                    .save(this.output);

            // Boots - pattern: "I I" / "C C"
            shaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("I I")
                    .pattern("C C")
                    .define('C', primary)
                    .define('I', secondary)
                    .unlockedBy(getHasName(primary), has(primary))
                    .save(this.output);
        }

        /**
         * Generate Crusader armor set (uses gold, ghast tear, and iron)
         */
        private void generateCrusaderArmorSet(Armor.Set armorSet, Item gold, Item tear, Item iron) {
            // Helmet - pattern: "GTG" / "I I" / "III"
            shaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("GTG")
                    .pattern("I I")
                    .pattern("III")
                    .define('G', gold)
                    .define('T', tear)
                    .define('I', iron)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Chestplate - pattern: "I I" / "GTG" / "IGI"
            shaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("I I")
                    .pattern("GTG")
                    .pattern("IGI")
                    .define('G', gold)
                    .define('T', tear)
                    .define('I', iron)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Leggings - pattern: "GTG" / "I I" / "G G"
            shaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("GTG")
                    .pattern("I I")
                    .pattern("G G")
                    .define('G', gold)
                    .define('T', tear)
                    .define('I', iron)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Boots - pattern: "I I" / "G G"
            shaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("I I")
                    .pattern("G G")
                    .define('G', gold)
                    .define('I', iron)
                    .unlockedBy(getHasName(gold), has(gold))
                    .save(this.output);
        }

        /**
         * Generate simple robe set (chain + wool)
         */
        private void generateRobeSet(Armor.Set armorSet, Item specialIngredient) {
            // Helmet - pattern: "W W" / "WLW"
            shaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("W W")
                    .pattern("WLW")
                    .define('L', specialIngredient)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(specialIngredient), has(specialIngredient))
                    .save(this.output);

            // Chestplate - pattern: "L L" / "WWW" / "WWW"
            shaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("L L")
                    .pattern("WWW")
                    .pattern("WWW")
                    .define('L', specialIngredient)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(specialIngredient), has(specialIngredient))
                    .save(this.output);

            // Leggings - pattern: "LLL" / "W W" / "W W"
            shaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("LLL")
                    .pattern("W W")
                    .pattern("W W")
                    .define('L', specialIngredient)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(specialIngredient), has(specialIngredient))
                    .save(this.output);

            // Boots - pattern: "L L" / "W W"
            shaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("L L")
                    .pattern("W W")
                    .define('L', specialIngredient)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(specialIngredient), has(specialIngredient))
                    .save(this.output);
        }

        /**
         * Generate Prior robe set (gold + ghast_tear + wool)
         */
        private void generatePriorRobeSet(Armor.Set armorSet, Item gold, Item tear) {
            // Helmet - pattern: "G G" / "WTW"
            shaped(RecipeCategory.COMBAT, armorSet.head)
                    .pattern("G G")
                    .pattern("WTW")
                    .define('G', gold)
                    .define('T', tear)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Chestplate - pattern: "G G" / "WTW" / "WWW"
            shaped(RecipeCategory.COMBAT, armorSet.chest)
                    .pattern("G G")
                    .pattern("WTW")
                    .pattern("WWW")
                    .define('G', gold)
                    .define('T', tear)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Leggings - pattern: "GTG" / "W W" / "W W"
            shaped(RecipeCategory.COMBAT, armorSet.legs)
                    .pattern("GTG")
                    .pattern("W W")
                    .pattern("W W")
                    .define('G', gold)
                    .define('T', tear)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(tear), has(tear))
                    .save(this.output);

            // Boots - pattern: "G G" / "W W"
            shaped(RecipeCategory.COMBAT, armorSet.feet)
                    .pattern("G G")
                    .pattern("W W")
                    .define('G', gold)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(gold), has(gold))
                    .save(this.output);
        }

        // ========================================
        // OTHER RECIPES
        // ========================================

        private void generateOtherRecipes() {
            // Monk Workbench
            shaped(RecipeCategory.MISC, PaladinBlocks.MONK_WORKBENCH_BLOCK)
                    .pattern("GTG")
                    .pattern(" # ")
                    .pattern("###")
                    .define('G', Items.GOLD_INGOT)
                    .define('T', Items.GHAST_TEAR)
                    .define('#', Items.POLISHED_ANDESITE)
                    .unlockedBy(getHasName(Items.GHAST_TEAR), has(Items.GHAST_TEAR))
                    .save(this.output);
        }

        // ========================================
        // NETHERITE UPGRADE RECIPES
        // ========================================

        private void generateNetheriteUpgrades() {
            // Weapon upgrades
            netheriteSmithing(PaladinWeapons.diamond_holy_wand.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_wand.item());
            netheriteSmithing(PaladinWeapons.diamond_holy_staff.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_holy_staff.item());
            netheriteSmithing(PaladinWeapons.diamond_claymore.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_claymore.item());
            netheriteSmithing(PaladinWeapons.diamond_great_hammer.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_great_hammer.item());
            netheriteSmithing(PaladinWeapons.diamond_mace.item(), RecipeCategory.COMBAT, PaladinWeapons.netherite_mace.item());
            netheriteSmithing(PaladinShields.diamond_kite_shield.item(), RecipeCategory.COMBAT, PaladinShields.netherite_kite_shield.item());

            // Crusader armor upgrades
            netheriteSmithing(Armors.paladinArmorSet_t2.head, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.head);
            netheriteSmithing(Armors.paladinArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.chest);
            netheriteSmithing(Armors.paladinArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.legs);
            netheriteSmithing(Armors.paladinArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.paladinArmorSet_t3.feet);

            // Prior robe upgrades
            netheriteSmithing(Armors.priestArmorSet_t2.head, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.head);
            netheriteSmithing(Armors.priestArmorSet_t2.chest, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.chest);
            netheriteSmithing(Armors.priestArmorSet_t2.legs, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.legs);
            netheriteSmithing(Armors.priestArmorSet_t2.feet, RecipeCategory.COMBAT, Armors.priestArmorSet_t3.feet);
        }



        };
    }

    @Override
    public String getName() {
        return "Paladin Crafting Recipes";
    }
}
