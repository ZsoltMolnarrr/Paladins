package net.paladins.client.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.entity.LightwellEntity;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;

/// Fullbright glow overlay for the Lightwell — the emissive counterpart to its base texture, so the
/// light column reads as radiant regardless of world light. Mirrors the Frost Elemental's glow layer.
public class LightwellGlowFeatureRenderer extends FeatureRenderer<LightwellEntity, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(PaladinsMod.ID, "textures/entity/lightwell_glow.png");
    // No-cull emissive layer (DISABLE_CULLING), unlike vanilla's culled `getEyes`: the glow shell has
    // faces the base model doesn't, and culling was dropping the ones facing away from the camera.
    private static final RenderLayer LAYER = CustomLayers.spellObject(TEXTURE, LightEmission.RADIATE, true);

    public LightwellGlowFeatureRenderer(FeatureRendererContext<LightwellEntity, LightwellEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LightwellEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV);
    }
}
