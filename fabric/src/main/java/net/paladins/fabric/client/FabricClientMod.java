package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.paladins.client.PaladinsClientMod;

public class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PaladinsClientMod.initialize();
    }
}
