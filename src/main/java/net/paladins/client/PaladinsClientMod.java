package net.paladins.client;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.block.PaladinBlocks;
import net.paladins.client.armor.PaladinArmorRenderer;
import net.paladins.client.armor.PriestArmorRenderer;
import net.paladins.client.effect.AbsorbParticleSpawner;
import net.paladins.client.effect.DivineProtectionRenderer;
import net.paladins.client.entity.BannerEntityRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.BannerEntity;
import net.paladins.entity.BarrierEntity;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.StunParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;
import java.util.function.Supplier;

public class PaladinsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                Identifier.of(PaladinsMod.ID, "projectile/judgement"),
                DivineProtectionRenderer.modelId_base,
                DivineProtectionRenderer.modelId_overlay,
                BannerEntityRenderer.modelId
        ));
        CustomModelStatusEffect.register(PaladinEffects.DIVINE_PROTECTION.effect, new DivineProtectionRenderer());
        CustomParticleStatusEffect.register(PaladinEffects.JUDGEMENT.effect, new StunParticleSpawner());
        // CustomParticleStatusEffect.register(PaladinEffects.ABSORPTION.effect, new AbsorbParticleSpawner());
        CustomParticleStatusEffect.register(
                PaladinEffects.ABSORPTION.effect,
                new BuffParticleSpawner(
                        new ParticleBatch(
                                SpellEngineParticles.aura_effect_553.id().toString(),
                                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                                1, 0, 0)
                                .scale(1.4F)
                                .followEntity(true)
                                .color(Color.HOLY.alpha(0.75F).toRGBA())
                ).withFrequency(30).scaleWithAmplifier(false)
        );


        BlockRenderLayerMap.INSTANCE.putBlock(PaladinBlocks.MONK_WORKBENCH, RenderLayer.getCutout());

        EntityRendererRegistry.register(BarrierEntity.TYPE, BarrierEntityRenderer::new);
        EntityRendererRegistry.register(BannerEntity.ENTITY_TYPE, BannerEntityRenderer::new);

        BarrierEntityRenderer.setup();

        registerArmorRenderer(Armors.paladinArmorSet_t1, PaladinArmorRenderer::paladin);
        registerArmorRenderer(Armors.paladinArmorSet_t2, PaladinArmorRenderer::crusader);
        registerArmorRenderer(Armors.paladinArmorSet_t3, PaladinArmorRenderer::netheriteCrusader);
        registerArmorRenderer(Armors.priestArmorSet_t1, PriestArmorRenderer::priest);
        registerArmorRenderer(Armors.priestArmorSet_t2, PriestArmorRenderer::prior);
        registerArmorRenderer(Armors.priestArmorSet_t3, PriestArmorRenderer::netheritePrior);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
}
