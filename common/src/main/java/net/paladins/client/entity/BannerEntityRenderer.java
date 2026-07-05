package net.paladins.client.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.paladins.PaladinsMod;
import net.paladins.entity.BannerEntity;

public class BannerEntityRenderer<T extends BannerEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE =
            Identifier.of(PaladinsMod.ID, "textures/entity/battle_banner.png");

    private final BattleBannerEntityModel model;

    public BannerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new BattleBannerEntityModel(context.getPart(BattleBannerEntityModel.LAYER));
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw() + 180F));
        // Standard entity-model space: y-down and x-mirrored, ground plane at y = 1.5
        matrices.scale(-1F, -1F, 1F);
        matrices.translate(0, -1.5, 0);
        model.setAngles(entity, 0F, 0F, entity.age + tickDelta, 0F, 0F);
        var vertices = vertexConsumers.getBuffer(model.getLayer(TEXTURE));
        model.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV, -1);
        matrices.pop();
    }

    // The banner rendered fullbright before (cloud client_data.light_level = 15) — keep it self-lit.
    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return 15;
    }
}
