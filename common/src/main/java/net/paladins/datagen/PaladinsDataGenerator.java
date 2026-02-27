package net.paladins.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSpells;
import net.paladins.item.PaladinShields;
import net.paladins.item.PaladinWeapons;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.datagen.WeaponAttributeGenerator;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellTags;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PaladinsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // SoundGen needs support for sounds with multiple file entries "paladins:plate_equip_1","paladins:plate_equip_2","paladins:plate_equip_3"
        // pack.addProvider(SoundGen::new);
        pack.addProvider(SpellGen::new);
        pack.addProvider(SpellTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(UnsmeltGenerator::new);
        pack.addProvider(PaladinRecipes::new);
        pack.addProvider(WeaponGen::new);
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

    public static class SpellTagGenerator extends FabricTagProvider<Spell> {
        public SpellTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, SpellRegistry.KEY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var namespace = PaladinsMod.ID;
            var treasureTagBuilder = getOrCreateTagBuilder(SpellTags.TREASURE);
            var processedBooks = new HashSet<PaladinSpells.Book>();
            PaladinSpells.entries.forEach(entry -> {
                if (entry.book() != null) {
                    var bookTagKey = SpellTags.spellBook(namespace, entry.book().toString().toLowerCase());
                    getOrCreateTagBuilder(bookTagKey).addOptional(entry.id());
                    var scrollTagKey = SpellTags.spellScroll(namespace, entry.book().toString().toLowerCase());
                    getOrCreateTagBuilder(scrollTagKey).addOptional(entry.id());
                    if (processedBooks.add(entry.book())) {
                        treasureTagBuilder.addOptionalTag(scrollTagKey);
                    }
                }
                for (var group : entry.weaponGroups()) {
                    var weaponGroupTagKey = SpellTags.weapon(namespace, group.toString().toLowerCase());
                    getOrCreateTagBuilder(weaponGroupTagKey).addOptional(entry.id());
                }
            });
        }
    }

    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            generateWeaponTags(PaladinWeapons.entries);
            generateArmorTags(
                    Armors.entries.stream().filter(entry -> entry.name().contains("armor")).toList(),
                    RPGSeriesItemTags.ArmorMetaType.MELEE
            );
            generateArmorTags(
                    Armors.entries.stream().filter(entry -> entry.name().contains("robe")).toList(),
                    RPGSeriesItemTags.ArmorMetaType.MAGIC
            );

            var shieldEntries = PaladinShields.entries.stream().map(entry ->
                    new RPGSeriesDataGen.ShieldEntry(entry.id(), entry.lootProperties)
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
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("gold"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(exporter,
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("iron"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.IRON_NUGGET);
//            disassemble(exporter,
//                    Weapons.entries.stream()
//                            .filter(entry -> entry.id().getPath().contains("diamond"))
//                            .map(entry -> (ItemConvertible) entry.item()).toList(),
//                    Items.DIAM);
            disassemble(exporter,
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);

            disassemble(exporter,
                    List.of(PaladinWeapons.holy_staff.item(), PaladinWeapons.holy_wand.item()),
                    Items.GOLD_NUGGET);

            disassemble(exporter,
                    List.of(PaladinShields.iron_kite_shield.item()),
                    Items.IRON_NUGGET);
            disassemble(exporter,
                    List.of(PaladinShields.golden_kite_shield.item()),
                    Items.GOLD_NUGGET);
            disassemble(exporter,
                    List.of(PaladinShields.netherite_kite_shield.item()),
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

    public static class WeaponGen extends WeaponAttributeGenerator {
        public WeaponGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateWeaponAttributes(Builder builder) {
            PaladinWeapons.entries.forEach(entry -> {
                if (entry.weaponAttributesPreset != null && !entry.weaponAttributesPreset.isEmpty()) {
                    builder.entries.add(new Entry(entry.id(), entry.weaponAttributesPreset));
                }
            });
        }
    }
}
