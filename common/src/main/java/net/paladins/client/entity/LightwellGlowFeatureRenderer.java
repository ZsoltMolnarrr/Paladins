package net.paladins.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;

/// Fullbright glow overlay for the Lightwell — the emissive counterpart to its base texture, so the
/// light column reads as radiant regardless of world light. Mirrors the Frost Elemental's glow layer.
public class LightwellGlowFeatureRenderer
        extends RenderLayer<LightwellEntityRenderer.State, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "textures/entity/lightwell_glow.png");
    // No-cull emissive layer, unlike vanilla's culled `getEyes`: the glow shell has faces the base
    // model doesn't, and culling was dropping the ones facing away from the camera.
    private static final RenderType LAYER = CustomLayers.spellObject(TEXTURE, LightEmission.RADIATE, true);

    public LightwellGlowFeatureRenderer(RenderLayerParent<LightwellEntityRenderer.State, LightwellEntityModel> context) {
        super(context);
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector queue, int light,
                       LightwellEntityRenderer.State state, float limbAngle, float limbDistance) {
        queue.submitModel(this.getParentModel(), state, matrices, LAYER,
                LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
    }
}
