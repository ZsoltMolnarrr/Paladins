package net.paladins.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.paladins.content.PaladinSpells;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.PaladinEntities;
import net.paladins.item.PaladinItemTags;
import net.paladins.item.PaladinShields;
import net.paladins.item.PaladinWeapons;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.datagen.NamespacedLangGenerator;
import net.spell_engine.api.datagen.SimpleSoundGeneratorV2;
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

        pack.addProvider(SoundGen::new);
        pack.addProvider(SpellGen::new);
        pack.addProvider(SpellTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(UnsmeltGenerator::new);
        pack.addProvider(PaladinRecipes::new);
        pack.addProvider(WeaponGen::new);
        pack.addProvider(PaladinsAdvancements::new);
        pack.addProvider(LangGen::new);
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
            var treasureTagBuilder = builder(SpellTags.TREASURE);
            var processedBooks = new HashSet<PaladinSpells.Book>();
            PaladinSpells.entries.forEach(entry -> {
                if (entry.book() != null) {
                    var bookTagKey = SpellTags.spellBook(namespace, entry.book().toString().toLowerCase());
                    builder(bookTagKey).addOptional(RegistryKey.of(SpellRegistry.KEY, entry.id()));
                    var scrollTagKey = SpellTags.spellScroll(namespace, entry.book().toString().toLowerCase());
                    builder(scrollTagKey).addOptional(RegistryKey.of(SpellRegistry.KEY, entry.id()));
                    if (processedBooks.add(entry.book())) {
                        treasureTagBuilder.addOptionalTag(scrollTagKey);
                    }
                }
                for (var group : entry.weaponGroups()) {
                    var weaponGroupTagKey = SpellTags.weapon(namespace, group.toString().toLowerCase());
                    builder(weaponGroupTagKey).addOptional(RegistryKey.of(SpellRegistry.KEY, entry.id()));
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

            // Anvil repair tags (`minecraft:repairable`), one per material
            for (var repair: PaladinItemTags.REPAIR_TAGS) {
                var tag = builder(repair.tag());
                repair.required().forEach(id -> tag.add(RegistryKey.of(RegistryKeys.ITEM, id)));
                repair.optional().forEach(id -> tag.addOptional(RegistryKey.of(RegistryKeys.ITEM, id)));
            }
        }
    }

    public static class SoundGen extends SimpleSoundGeneratorV2 {
        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(PaladinsMod.ID,
                            PaladinSounds.entries.stream()
                                    .map(entry -> new SoundEntry(
                                            entry.id().getPath(),
                                            SoundEntry.withVariants(entry.soundFile(), entry.variants()).variants()
                                    ))
                                    .toList()
                    )
            );
        }
    }

    public static class UnsmeltGenerator extends FabricRecipeProvider {
        public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        public static int UNSMELT_TIME = 300;

        @Override
        public String getName() {
            return "Paladin Unsmelting Recipes";
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
            return new RecipeGenerator(registryLookup, exporter) {
                @Override
                public void generate() {
            disassembleArmor(Armors.paladinArmorSet_t1, Items.IRON_NUGGET);
            disassembleArmor(Armors.paladinArmorSet_t2, Items.GOLD_NUGGET);
            disassembleArmor(Armors.paladinArmorSet_t3, Items.NETHERITE_SCRAP);
            disassembleArmor(Armors.priestArmorSet_t1, Items.IRON_NUGGET);
            disassembleArmor(Armors.priestArmorSet_t2, Items.GOLD_NUGGET);
            disassembleArmor(Armors.priestArmorSet_t3, Items.NETHERITE_SCRAP);

            disassemble(
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("gold"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.GOLD_NUGGET);
            disassemble(
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("iron"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.IRON_NUGGET);
//            disassemble(
//                    Weapons.entries.stream()
//                            .filter(entry -> entry.id().getPath().contains("diamond"))
//                            .map(entry -> (ItemConvertible) entry.item()).toList(),
//                    Items.DIAM);
            disassemble(
                    PaladinWeapons.entries.stream()
                            .filter(entry -> entry.id().getPath().contains("netherite"))
                            .map(entry -> (ItemConvertible) entry.item()).toList(),
                    Items.NETHERITE_SCRAP);

            disassemble(
                    List.of(PaladinWeapons.holy_staff.item(), PaladinWeapons.holy_wand.item()),
                    Items.GOLD_NUGGET);

            disassemble(
                    List.of(PaladinShields.iron_kite_shield.item()),
                    Items.IRON_NUGGET);
            disassemble(
                    List.of(PaladinShields.golden_kite_shield.item()),
                    Items.GOLD_NUGGET);
            disassemble(
                    List.of(PaladinShields.netherite_kite_shield.item()),
                    Items.NETHERITE_SCRAP);
        }

        private void disassembleArmor(Armor.Set<?> armorSet, Item output) {
            offerSmelting(
                    List.<ItemConvertible>copyOf(armorSet.pieces()),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            offerBlasting(
                    List.<ItemConvertible>copyOf(armorSet.pieces()),
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }

        private void disassemble(List<ItemConvertible> items, Item output) {
            offerSmelting(
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME,
                    "disassemble"
            );
            offerBlasting(
                    items,
                    RecipeCategory.MISC,
                    output,
                    0.1f,
                    UNSMELT_TIME / 2,
                    "disassemble"
            );
        }
            };
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

    /**
     * Generates the {@code en_us.json} language file from the in-code content definitions
     * (spells, status effects, weapons, shields, armor, spell books) plus the advancement tree and a
     * number of ad-hoc strings (creative tab, summoned entities, villager, workbench) that have no
     * dedicated content entry.
     */
    public static class LangGen extends NamespacedLangGenerator {
        public LangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup, PaladinsMod.ID);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, FabricLanguageProvider.TranslationBuilder builder) {
            var namespace = PaladinsMod.ID;

            // Creative tab
            builder.add("itemGroup." + namespace + ".general", "Paladins & Priests");

            // Spell books & scrolls (one generated item per book)
            for (var book : PaladinSpells.Book.values()) {
                var key = book.name().toLowerCase();
                builder.add("item." + namespace + ".spell_book/" + key, book.bookName);
                builder.add("item." + namespace + ".spell_scroll/" + key, book.scrollName);
                builder.add("item." + namespace + ".spell_book/" + key + ".spell_binding.description", book.bindingDescription);
            }

            // Spells (only those given a display name in code)
            for (var entry : PaladinSpells.entries) {
                if (entry.title() == null || entry.title().isEmpty()) {
                    continue;
                }
                var path = entry.id().getPath();
                builder.add("spell." + namespace + "." + path + ".name", entry.title());
                builder.add("spell." + namespace + "." + path + ".description", entry.description());
            }

            // Status effects
            for (var entry : PaladinEffects.entries) {
                var path = entry.id.getPath();
                builder.add("effect." + namespace + "." + path, entry.title);
                builder.add("effect." + namespace + "." + path + ".description", entry.description);
            }

            // Weapons and shields — code-sourced display names
            PaladinWeapons.entries.forEach(entry -> addItemName(builder, entry.id(), entry.translatedName()));
            PaladinShields.entries.forEach(entry -> addItemName(builder, entry.id(), entry.translatedName()));
            // Conditional weapons/shields are only registered when their host mod is present, so they are
            // absent from the entry lists at data-gen time. Their names are provided directly.
            builder.add("item." + namespace + ".aeternium_claymore", "Aeternium Claymore");
            builder.add("item." + namespace + ".ruby_claymore", "Ruby Claymore");
            builder.add("item." + namespace + ".aether_claymore", "Holy Claymore");
            builder.add("item." + namespace + ".aeternium_great_hammer", "Aeternium Great Hammer");
            builder.add("item." + namespace + ".ruby_great_hammer", "Ruby Great Hammer");
            builder.add("item." + namespace + ".aether_great_hammer", "Valkyrie Great Hammer");
            builder.add("item." + namespace + ".stone_mace", "Stone Mace");
            builder.add("item." + namespace + ".aeternium_mace", "Aeternium Mace");
            builder.add("item." + namespace + ".ruby_mace", "Ruby Mace");
            builder.add("item." + namespace + ".aether_mace", "Sun's Mace");
            builder.add("item." + namespace + ".ruby_holy_staff", "Ruby Holy Staff");
            builder.add("item." + namespace + ".aether_holy_staff", "Silver Staff of the Valkyrie");
            builder.add("item." + namespace + ".ruby_kite_shield", "Ruby Kite Shield");
            builder.add("item." + namespace + ".aeternium_kite_shield", "Aeternium Kite Shield");
            builder.add("item." + namespace + ".aether_kite_shield", "Valkyrie Bulwark");

            // Armor sets (per piece)
            for (var entry : Armors.entries) {
                var set = entry.armorSet();
                addItemName(builder, set.idOf(set.head), set.headTranslation);
                addItemName(builder, set.idOf(set.chest), set.chestTranslation);
                addItemName(builder, set.idOf(set.legs), set.legsTranslation);
                addItemName(builder, set.idOf(set.feet), set.feetTranslation);
            }

            // Custom entities — code-sourced display names (paired with the type in PaladinEntities.Entry)
            for (var entry : PaladinEntities.entries) {
                builder.add("entity." + namespace + "." + entry.id.getPath(), entry.name);
            }

            // Monk villager (several key formats are referenced across versions) + workbench
            builder.add("entity.minecraft.villager.monk", "Monk");
            builder.add("entity.minecraft.villager." + namespace + ".monk", "Monk");
            builder.add("entity.minecraft.villager." + namespace + ":monk", "Monk");
            builder.add("block." + namespace + ".monk_workbench", "Monk Workbench");
            builder.add("block." + namespace + ".monk_workbench.hint", "Workbench for Monk Villagers.");

            // Advancements (generated alongside the rpg_series advancement JSONs)
            for (var advancement : PaladinsAdvancements.entries()) {
                builder.add(advancement.titleKey(), advancement.title());
                builder.add(advancement.descriptionKey(), advancement.description());
            }
            // Advancement whose definition is provided elsewhere in the RPG Series, but whose
            // translation historically ships with Paladins.
            builder.add("advancements.rpg_series.obtain_healing_rune.title", "Path of Healing");
            builder.add("advancements.rpg_series.obtain_healing_rune.description", "Obtain a Healing Rune");
        }

        private static void addItemName(FabricLanguageProvider.TranslationBuilder builder, Identifier id, String name) {
            if (name == null || name.isEmpty()) {
                return;
            }
            builder.add("item." + id.getNamespace() + "." + id.getPath(), name);
        }
    }
}
