package net.paladins.fabric;

import net.fabricmc.api.ModInitializer;

import net.paladins.PaladinsMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PaladinsMod.init();
        PaladinsMod.registerSounds();
        PaladinsMod.registerBlocks();
        PaladinsMod.registerItems();
        PaladinsMod.registerEffects();
        PaladinsMod.registerPOI();
        PaladinsMod.registerVillagers();
    }
}
