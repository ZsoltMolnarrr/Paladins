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
import net.paladins.entity.BarrierEntity;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;

public class BarrierEntityRenderer<T extends BarrierEntity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;

    public static final Identifier modelId = new Identifier(PaladinsMod.ID, "effect/barrier");

    public BarrierEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }
    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    private static final RenderLayer LAYER =
            CustomLayers.spellEffect(LightEmission.RADIATE, true);

    private static final int segments = 6;


    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        var spell = ((BarrierEntity)entity).getSpell();
        if (spell == null) {
            return;
        }
        var range = spell.range;

        matrices.push();
        float horizontalOffset = 1F;
        float verticalOffset = 0;

        var scaleFactor = range * 0.75F;

        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        matrices.translate(0.0F, 1, 0);

        float segmentWidthMultiplier = (360F / segments) * 0.025F; // Gives 1.5F for 6 segments
        for (int i = 0; i < segments; i++) {
            var rotation = 360F / segments * i;
            matrices.push();
            // Render top part

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
            matrices.scale(segmentWidthMultiplier, 1F, 1F);
            matrices.translate(0.0F, verticalOffset, -horizontalOffset);
            CustomModels.render(LAYER, itemRenderer, modelId,
                    matrices, vertexConsumers, light, entity.getId());
            matrices.pop();

            matrices.push();
            // Render bottom part
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
            matrices.scale(segmentWidthMultiplier, 1F, 1F);
            matrices.translate(0.0F, - verticalOffset + 1, -horizontalOffset);
            CustomModels.render(LAYER, itemRenderer, modelId,
                    matrices, vertexConsumers, light, entity.getId());
            matrices.pop();
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
