package net.paladins.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
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
        ModelLayerRegistry.registerModelLayer(BattleBannerEntityModel.LAYER, BattleBannerEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(PaladinEntities.BANNER.type, BannerEntityRenderer::new);
        ModelLayerRegistry.registerModelLayer(LightwellEntityModel.LAYER, LightwellEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(PaladinEntities.LIGHTWELL.type, LightwellEntityRenderer::new);

        // 26.1 derives the chunk-section layer from the block model, which already declares
        // `"render_type": "minecraft:cutout"` — `BlockRenderLayerMap` is gone and not needed.

        // Batched barrier rendering (see BarrierEntityRenderer). 26.2 removed `MultiBufferSource`, so the
        // geometry is submitted instead of drawn: COLLECT_SUBMITS fires at the end of
        // `LevelRenderer#submitFeatures` (after the entity submits that fill the batch), and carries the
        // submit node collector. SpellEngine's beams use the same hook.
        LevelRenderEvents.COLLECT_SUBMITS.register(context ->
                BarrierEntityRenderer.submit(context.poseStack(), context.submitNodeCollector(),
                        context.gameRenderer().mainCamera(),
                        Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true)));
    }
}
