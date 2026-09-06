package net.paladins.forge.client;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.paladins.client.PaladinsClientMod;
import net.paladins.client.entity.BannerEntityRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.client.entity.BattleBannerEntityModel;
import net.paladins.client.entity.LightwellEntityModel;
import net.paladins.client.entity.LightwellEntityRenderer;
import net.paladins.entity.PaladinEntities;
import net.spell_engine.client.gui.ConfigMenuScreen;

/// Client-side Forge wiring. Only ever touched behind `Dist.CLIENT` (see `ForgeMod`), so there is no
/// `@EventBusSubscriber` — listeners are registered explicitly from {@link #register(IEventBus)}.
public class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterLayerDefinitions.class,
                ForgeClientMod::onRegisterLayerDefinitions);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class,
                ForgeClientMod::onRegisterRenderers);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        PaladinsClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new ConfigMenuScreen(parent)));

        // Batched barrier rendering, replayed after the particle pass (see BarrierEntityRenderer).
        // NOTE: must be AFTER_PARTICLES, not AFTER_TRANSLUCENT_BLOCKS. Vanilla renders particles
        // *after* translucent terrain, so AFTER_TRANSLUCENT_BLOCKS fires before particles and any
        // particle would paint over the barrier model. AFTER_PARTICLES matches where Fabric's
        // WorldRenderEvents.AFTER_TRANSLUCENT injects (just before clouds, after particles).
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderLevelStageEvent.class, render -> {
            if (render.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                BarrierEntityRenderer.renderAfterTranslucent(render.getPoseStack(), render.getCamera(),
                        render.getPartialTick());
            }
        });
    }

    private static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BattleBannerEntityModel.LAYER, BattleBannerEntityModel::getTexturedModelData);
        event.registerLayerDefinition(LightwellEntityModel.LAYER, LightwellEntityModel::getTexturedModelData);
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PaladinEntities.BARRIER.type, BarrierEntityRenderer::new);
        event.registerEntityRenderer(PaladinEntities.BANNER.type, BannerEntityRenderer::new);
        event.registerEntityRenderer(PaladinEntities.LIGHTWELL.type, LightwellEntityRenderer::new);
    }
}
