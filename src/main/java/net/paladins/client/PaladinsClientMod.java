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
import net.paladins.effect.Effects;
import net.paladins.entity.BannerEntity;
import net.paladins.entity.BarrierEntity;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.StunParticleSpawner;

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
        CustomModelStatusEffect.register(Effects.DIVINE_PROTECTION.effect, new DivineProtectionRenderer());
        CustomParticleStatusEffect.register(Effects.JUDGEMENT.effect, new StunParticleSpawner());
        CustomParticleStatusEffect.register(Effects.ABSORPTION.effect, new AbsorbParticleSpawner());
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
