package net.paladins.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.client.effect.AbsorbParticleSpawner;
import net.paladins.client.effect.DivineProtectionRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.effect.Effects;
import net.paladins.entity.BarrierEntity;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.StunParticleSpawner;

import java.util.List;

public class PaladinsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                new Identifier(PaladinsMod.ID, "projectile/judgement"),
                DivineProtectionRenderer.modelId_base,
                DivineProtectionRenderer.modelId_overlay
        ));
        CustomModelStatusEffect.register(Effects.DIVINE_PROTECTION, new DivineProtectionRenderer());
        CustomParticleStatusEffect.register(Effects.JUDGEMENT, new StunParticleSpawner());
        CustomParticleStatusEffect.register(Effects.ABSORPTION, new AbsorbParticleSpawner());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinBlocks.MONK_WORKBENCH, RenderLayer.getCutout());

        EntityRendererRegistry.register(BarrierEntity.TYPE, BarrierEntityRenderer::new);
        BarrierEntityRenderer.setup();
    }
}
