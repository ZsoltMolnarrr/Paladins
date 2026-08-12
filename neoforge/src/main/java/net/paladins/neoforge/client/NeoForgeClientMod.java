package net.paladins.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
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

        // Batched barrier rendering, replayed after the particle pass (see BarrierEntityRenderer).
        // Game-bus event, subscribed here since this class is on the mod bus.
        // NOTE: must be AFTER_PARTICLES, not AFTER_TRANSLUCENT_BLOCKS. Vanilla renders particles
        // *after* translucent terrain, so AFTER_TRANSLUCENT_BLOCKS fires before particles and any
        // particle would paint over the barrier model. AFTER_PARTICLES matches where Fabric's
        // WorldRenderEvents.AFTER_TRANSLUCENT injects (just before clouds, after particles).
        NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, render -> {
            if (render.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                BarrierEntityRenderer.renderAfterTranslucent(render.getPoseStack(), render.getCamera(),
                        render.getPartialTick().getTickDelta(true));
            }
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