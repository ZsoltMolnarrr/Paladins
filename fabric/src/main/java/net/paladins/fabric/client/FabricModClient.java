package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
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
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinBlocks.MONK_WORKBENCH, RenderLayer.getCutout());

        // Batched barrier rendering, replayed after translucent terrain (see BarrierEntityRenderer).
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context ->
                BarrierEntityRenderer.renderAfterTranslucent(context.matrixStack(), context.camera(),
                        context.tickCounter().getTickDelta(true)));
    }
}
