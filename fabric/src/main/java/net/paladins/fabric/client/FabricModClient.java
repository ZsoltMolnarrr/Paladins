package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.paladins.block.PaladinBlocks;
import net.paladins.client.PaladinsClientMod;

public final class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PaladinsClientMod.init();

        // Fabric-specific render layer registration
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinBlocks.MONK_WORKBENCH, RenderLayer.getCutout());
    }
}
