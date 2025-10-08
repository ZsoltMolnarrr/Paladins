package net.paladins.client.armor;

import mod.azure.azurelibarmor.common.internal.common.cache.texture.AutoGlowingTexture;
import mod.azure.azurelibarmor.rewrite.model.AzBone;
import mod.azure.azurelibarmor.rewrite.render.AzRendererPipeline;
import mod.azure.azurelibarmor.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import mod.azure.azurelibarmor.rewrite.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelibarmor.rewrite.render.layer.AzRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

public class PaladinArmorRenderer extends AzArmorRenderer {
    public static PaladinArmorRenderer paladin() {
        return new PaladinArmorRenderer("paladin_armor", "paladin_armor", "paladin_armor");
    }
    public static PaladinArmorRenderer crusader() {
        return new PaladinArmorRenderer("paladin_armor", "crusader_armor", "crusader_armor");
    }
    public static PaladinArmorRenderer netheriteCrusader() {
        return new PaladinArmorRenderer("paladin_armor", "netherite_crusader_armor", "crusader_armor");
    }

    public PaladinArmorRenderer(String modelName, String textureName, String trimTexture) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(PaladinsMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(PaladinsMod.ID, "textures/armor/" + textureName + ".png")
        )
                .addRenderLayer(new AzTrimLayer<>(Identifier.of(PaladinsMod.ID, "armor/trim/" + trimTexture), false))
                .build());
    }
}
