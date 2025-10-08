package net.paladins.client.armor;

import mod.azure.azurelibarmor.rewrite.model.AzBone;
import mod.azure.azurelibarmor.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererPipelineContext;
import mod.azure.azurelibarmor.rewrite.render.layer.AzRenderLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class AzTrimLayer<T> implements AzRenderLayer<T> {
    public final Identifier textureBaseLocation;
    public final Function<ArmorTrim, Identifier> texturePermutations;

    public AzTrimLayer(Identifier baseTexture) {
        this(baseTexture, true);
    }

    public AzTrimLayer(Identifier baseTexture, boolean supportPatterns) {
        this(baseTexture, supportPatterns
                ? (trim) -> {
                    var pattern = trim.getPattern().value();
                    var material = trim.getMaterial().value();
                    var patternName = pattern.assetId().getPath();
                    return Identifier.of(baseTexture.getNamespace(), baseTexture.getPath() + "_" + patternName + "_" + material.assetName());
                }
                : (trim) -> {
                    var material = trim.getMaterial().value();
                    return Identifier.of(baseTexture.getNamespace(), baseTexture.getPath() + "_" + material.assetName());
                }
        );
    }

    public AzTrimLayer(Identifier baseTexture, Function<ArmorTrim, Identifier> textureLocationPermutations) {
        this.textureBaseLocation = baseTexture;
        this.texturePermutations = textureLocationPermutations;
    }

    public void preRender(AzRendererPipelineContext<T> context) {
    }

    public void render(AzRendererPipelineContext<T> context) {
        var armorPipelineContext = (AzArmorRendererPipelineContext) context;
        var itemstack = armorPipelineContext.currentStack();
        if (itemstack == null) {
            return;
        }
        ArmorTrim armorTrim = itemstack.get(DataComponentTypes.TRIM);
        if (armorTrim == null) {
            return;
        }

        var pattern = armorTrim.getPattern().value();

        var bakery = MinecraftClient.getInstance().getBakedModelManager();
        var armorTrimsAtlas = bakery.getAtlas(TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE); // Any way to get this from context?

        var renderPipeline = context.rendererPipeline();
        Identifier trimLocation = texturePermutations.apply(armorTrim);

        Sprite sprite = armorTrimsAtlas.getSprite(trimLocation);
        var renderType = TexturedRenderLayers.getArmorTrims(pattern.decal());
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(context.multiBufferSource().getBuffer(renderType));

        if (context.renderType() != null) {
            RenderLayer prevRenderType = context.renderType();
            VertexConsumer prevVertexConsumer = context.vertexConsumer();
            context.setRenderType(renderType);
            context.setVertexConsumer(vertexConsumer);
            renderPipeline.reRender(context);
            context.setRenderType(prevRenderType);
            context.setVertexConsumer(prevVertexConsumer);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<T> azRendererPipelineContext, AzBone azBone) {
    }
}
