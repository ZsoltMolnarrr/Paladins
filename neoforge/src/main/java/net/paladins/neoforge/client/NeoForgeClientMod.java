package net.paladins.neoforge.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.paladins.PaladinsMod;
import net.paladins.client.PaladinsClientMod;
import net.paladins.client.entity.BannerEntityRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.client.entity.BattleBannerEntityModel;
import net.paladins.client.entity.LightwellEntityModel;
import net.paladins.client.entity.LightwellEntityRenderer;
import net.paladins.entity.PaladinEntities;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = PaladinsMod.ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PaladinsClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));

        // Batched barrier rendering (see BarrierEntityRenderer). Game-bus event, subscribed here since this
        // class is on the mod bus.
        // 26.2: `MultiBufferSource` is gone, so the barrier geometry is submitted rather than drawn, and the
        // render-stage events no longer apply. `SubmitCustomGeometryEvent` is posted from
        // `LevelRenderer#submitFeatures` after the entity submits that fill the batch — the NeoForge twin of
        // Fabric's `LevelRenderEvents.COLLECT_SUBMITS`, and what SpellEngine's BeamRenderer hook uses.
        // Camera / tick progress are still not carried by the event — they come from the client.
        NeoForge.EVENT_BUS.addListener(SubmitCustomGeometryEvent.class, render -> {
            var client = Minecraft.getInstance();
            BarrierEntityRenderer.submit(render.getPoseStack(), render.getSubmitNodeCollector(),
                    client.gameRenderer.mainCamera(),
                    client.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BattleBannerEntityModel.LAYER, BattleBannerEntityModel::getTexturedModelData);
        event.registerLayerDefinition(LightwellEntityModel.LAYER, LightwellEntityModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PaladinEntities.BARRIER.type, BarrierEntityRenderer::new);
        event.registerEntityRenderer(PaladinEntities.BANNER.type, BannerEntityRenderer::new);
        event.registerEntityRenderer(PaladinEntities.LIGHTWELL.type, LightwellEntityRenderer::new);
    }
}