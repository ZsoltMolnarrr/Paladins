package net.paladins.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.paladins.content.PaladinSpells;
import net.paladins.item.Shields;
import net.paladins.item.Weapons;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PaladinsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // SoundGen needs support for sounds with multiple file entries "paladins:plate_equip_1","paladins:plate_equip_2","paladins:plate_equip_3"
        // pack.addProvider(SoundGen::new);
        pack.addProvider(SpellGen::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(UnsmeltGenerator::new);
    }

    public static class SpellGen extends SpellGenerator {
        public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: PaladinSpells.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }

    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            generateWeaponTags(Weapons.entries);
            generateArmorTags(Armors.entries);
            var shieldEntries = Shields.ENTRIES.stream().map(entry ->
                    new RPGSeriesDataGen.ShieldEntry(entry.id(), entry.lootProperties())
            ).toList();
            generateShieldTags(shieldEntries);
        }
    }

//    public static class SoundGen extends SimpleSoundGeneratorV2 {
//        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
//            super(dataOutput, registryLookup);
//        }
//
//        @Override
//        public void generateSounds(Builder builder) {
//            builder.entries.add(new Entry(PaladinsMod.ID,
//                            PaladinSounds.entries.stream()
//                                    .map(entry -> SoundEntry.withVariants(entry.id().getPath(), entry.variants()))
//                                    .toList()
//                    )
//            );
//        }
//    }

    public static class UnsmeltGenerator extends FabricRecipeProvider {
        public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        public static int UNSMELT_TIME = 300;

        @Override
        public void generate(RecipeExporter exporter) {
            disassembleArmor(exporter, Armors.paladinArmorSet_t1, Items.IRON_NUGGET);
            disassembleArmor(exporter, Armors.paladinArmorSet_t2, Items.GOLD_NUGGET);
            disassembleArmor(exporter, Armors.paladinArmorSet_t3, Items.NETHERITE_SCRAP);
            disassembleArmor(exporter, Armors.priestArmorSet_t1, Items.IRON_NUGGET);
            disassembleArmor(exporter, Armors.priestArmorSet_t2, Items.GOLD_NUGGET);
            disassembleArmor(exporter, Armors.priestArmorSet_t3, Items.NETHERITE_SCRAP);

            disassemble(exporter,
                    Weapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("gold"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(exporter,
                    Weapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("iron"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.IRON_NUGGET);
//            disassemble(exporter,
//                    Weapons.entries.stream()
//                            .filter(entry -> entry.id().getPath().contains("diamond"))
//                            .map(entry -> (ItemConvertible) entry.item()).toList(),
//                    Items.DIAM);
            disassemble(exporter,
                    Weapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);

            disassemble(exporter,
                    List.of(Weapons.holy_staff.item(), Weapons.holy_wand.item()),
                    Items.GOLD_NUGGET);

            disassemble(exporter,
                    List.of(Shields.iron_kite_shield.holder().item),
                    Items.IRON_NUGGET);
            disassemble(exporter,
                    List.of(Shields.golden_kite_shield.holder().item),
                    Items.IRON_NUGGET);
            disassemble(exporter,
                    List.of(Shields.netherite_kite_shield.holder().item),
                    Items.NETHERITE_SCRAP);
        }

        private static void disassembleArmor(RecipeExporter exporter, Armor.Set armorSet, Item output) {
            FabricRecipeProvider.offerSmelting(exporter,
                    armorSet.pieces(),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            FabricRecipeProvider.offerBlasting(exporter,
                    armorSet.pieces(),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }

        private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
            FabricRecipeProvider.offerSmelting(exporter,
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            FabricRecipeProvider.offerBlasting(exporter,
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }
    }
}
