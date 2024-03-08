package net.paladins.client.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.paladins.PaladinsMod;
import net.paladins.entity.BannerEntity;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.client.render.BeamRenderer;

public class BannerEntityRenderer<T extends BannerEntity> extends EntityRenderer<T> {
    // Item renderer
    private final ItemRenderer itemRenderer;
    public BannerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    public static final Identifier modelId = new Identifier(PaladinsMod.ID, "effect/battle_banner");
    private static final Identifier beamTexture = new Identifier("textures/entity/beacon_beam.png");
    private static final RenderLayer layer =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);


    public void render(T entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-1F * entity.getYaw() + 180F));
        matrixStack.translate(0, 0.5, 0); // Compensate for translate within CustomModels.render

        CustomModels.render(layer, itemRenderer, modelId, matrixStack, vertexConsumers, light, entity.getId());

        matrixStack.translate(0.5, 0, 0.5);
        BeamRenderer.renderBeam(matrixStack, vertexConsumers, beamTexture, entity.age, tickDelta, 1.0F,
                false, 255, 255, 255, 128,
                0.0F, 128F, 0.3F);

        matrixStack.pop();
    }
}
