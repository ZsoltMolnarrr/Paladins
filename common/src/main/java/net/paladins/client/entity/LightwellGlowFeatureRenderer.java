package net.paladins.client.entity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;

/// Fullbright glow overlay for the Lightwell — the emissive counterpart to its base texture, so the
/// light column reads as radiant regardless of world light. Mirrors the Frost Elemental's glow layer.
public class LightwellGlowFeatureRenderer
        extends FeatureRenderer<LightwellEntityRenderer.State, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(PaladinsMod.ID, "textures/entity/lightwell_glow.png");
    // No-cull emissive layer, unlike vanilla's culled `getEyes`: the glow shell has faces the base
    // model doesn't, and culling was dropping the ones facing away from the camera.
    private static final RenderLayer LAYER = CustomLayers.spellObject(TEXTURE, LightEmission.RADIATE, true);

    public LightwellGlowFeatureRenderer(FeatureRendererContext<LightwellEntityRenderer.State, LightwellEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                       LightwellEntityRenderer.State state, float limbAngle, float limbDistance) {
        queue.submitModel(this.getContextModel(), state, matrices, LAYER,
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, state.outlineColor, null);
    }
}
