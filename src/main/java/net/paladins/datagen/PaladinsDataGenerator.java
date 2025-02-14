package net.paladins.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.datagen.SpellGenerator;

import java.util.concurrent.CompletableFuture;

public class PaladinsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // SoundGen needs support for sounds with multiple file entries "paladins:plate_equip_1","paladins:plate_equip_2","paladins:plate_equip_3"
        // pack.addProvider(SoundGen::new);
        pack.addProvider(SpellGen::new);
    }

    public static class SpellGen extends SpellGenerator {
        public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
//            for (var entry: RogueSpells.entries) {
//                builder.add(entry.id(), entry.spell());
//            }
        }
    }

//    public static class SoundGen extends SimpleSoundGenerator {
//        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
//            super(dataOutput, registryLookup);
//        }
//
//        @Override
//        public void generateSounds(Builder builder) {
//            builder.entries.add(new Entry(PaladinsMod.ID,
//                    PaladinSounds.entries.stream().map(entry -> entry.id().getPath()).toList()));
//        }
//    }
}
