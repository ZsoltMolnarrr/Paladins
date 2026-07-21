package net.paladins.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.paladins.PaladinsMod;
import net.paladins.client.PaladinsClientMod;
import net.paladins.client.entity.BannerEntityRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.client.entity.BattleBannerEntityModel;
import net.paladins.client.entity.LightwellEntityModel;
import net.paladins.client.entity.LightwellEntityRenderer;
import net.paladins.entity.BannerEntity;
import net.paladins.entity.BarrierEntity;
import net.paladins.entity.LightwellEntity;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = PaladinsMod.ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PaladinsClientMod.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BattleBannerEntityModel.LAYER, BattleBannerEntityModel::getTexturedModelData);
        event.registerLayerDefinition(LightwellEntityModel.LAYER, LightwellEntityModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BarrierEntity.TYPE, BarrierEntityRenderer::new);
        event.registerEntityRenderer(BannerEntity.ENTITY_TYPE, BannerEntityRenderer::new);
        event.registerEntityRenderer(LightwellEntity.TYPE, LightwellEntityRenderer::new);
    }
}