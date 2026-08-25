package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockRenderLayer;
import net.paladins.block.PaladinBlocks;
import net.paladins.client.PaladinsClientMod;
import net.paladins.client.entity.BannerEntityRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.client.entity.BattleBannerEntityModel;
import net.paladins.client.entity.LightwellEntityModel;
import net.paladins.client.entity.LightwellEntityRenderer;
import net.paladins.entity.PaladinEntities;

public final class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PaladinsClientMod.init();

        // Entity model layers + renderers (Fabric API)
        EntityRendererRegistry.register(PaladinEntities.BARRIER.type, BarrierEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BattleBannerEntityModel.LAYER, BattleBannerEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(PaladinEntities.BANNER.type, BannerEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(LightwellEntityModel.LAYER, LightwellEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(PaladinEntities.LIGHTWELL.type, LightwellEntityRenderer::new);

        // Fabric-specific render layer registration
        BlockRenderLayerMap.putBlock(PaladinBlocks.MONK_WORKBENCH, BlockRenderLayer.CUTOUT);

        // Batched barrier rendering, replayed after translucent terrain (see BarrierEntityRenderer).
        // 1.21.9+ split the world render events into extraction/main; AFTER_TRANSLUCENT is gone,
        // END_MAIN is the equivalent injection point (SpellEngine's beams use the same one).
        WorldRenderEvents.END_MAIN.register(context ->
                BarrierEntityRenderer.renderAfterTranslucent(context.matrices(), context.gameRenderer().getCamera(),
                        MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(true)));
    }
}
