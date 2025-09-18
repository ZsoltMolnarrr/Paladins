package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.paladins.client.PaladinsClientMod;

public final class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PaladinsClientMod.init();
    }
}
