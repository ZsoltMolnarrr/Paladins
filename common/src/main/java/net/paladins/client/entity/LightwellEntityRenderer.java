package net.paladins.client.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.paladins.PaladinsMod;
import net.paladins.entity.LightwellEntity;

public class LightwellEntityRenderer extends MobEntityRenderer<LightwellEntity, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            new Identifier(PaladinsMod.ID, "textures/entity/lightwell_base.png");

    private static final float FLOAT_AMPLITUDE = 0.1F;
    private static final float FLOAT_FREQUENCY = (float)(Math.PI / 20.0); // 2-second cycle (40 ticks)

    public LightwellEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new LightwellEntityModel(context.getPart(LightwellEntityModel.LAYER)), 0.4f);
        this.addFeature(new LightwellGlowFeatureRenderer(this));
    }

    @Override
    protected void setupTransforms(LightwellEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        // Lift off the ground plus a gentle levitation bob, mirroring the Frost Elemental's float.
        var groundOffset = entity.getWidth() * 0.25F;
        matrices.translate(0.0, groundOffset + MathHelper.sin(animationProgress * FLOAT_FREQUENCY) * FLOAT_AMPLITUDE, 0.0);
    }

    @Override
    public Identifier getTexture(LightwellEntity entity) {
        return TEXTURE;
    }
}
