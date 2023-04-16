package net.paladins.client.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;

public class DivineProtectionRenderer implements CustomModelStatusEffect.Renderer {
    public static final Identifier modelId_base = new Identifier(PaladinsMod.ID, "effect/divine_protection");
    public static final Identifier modelId_overlay = new Identifier(PaladinsMod.ID, "effect/divine_protection_overlay");

    private static final RenderLayer BASE_RENDER_LAYER = RenderLayer.getTranslucentMovingBlock();
    private static final RenderLayer OVERLAY_RENDER_LAYER = CustomLayers.projectile(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false);

    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        var time = livingEntity.world.getTime() + delta;

        var angle = time * 2.25F - 45.0F;
        var horizontalOffset = 0.35F;
        var verticalOffset = livingEntity.getHeight() / 2F;
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        renderShield(matrixStack, verticalOffset, horizontalOffset, angle, itemRenderer, vertexConsumers, light, livingEntity);
        angle += 120F;
        renderShield(matrixStack, verticalOffset, horizontalOffset, angle, itemRenderer, vertexConsumers, light, livingEntity);
        angle += 120F;
        renderShield(matrixStack, verticalOffset, horizontalOffset, angle, itemRenderer, vertexConsumers, light, livingEntity);

        matrixStack.pop();
    }

    private void renderShield(MatrixStack matrixStack, float verticalOffset, float horizontalOffset, float rotation,
                              ItemRenderer itemRenderer, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        matrixStack.translate(0, verticalOffset, -horizontalOffset);
        CustomModels.render(OVERLAY_RENDER_LAYER, itemRenderer, modelId_overlay,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        CustomModels.render(BASE_RENDER_LAYER, itemRenderer, modelId_base,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();
    }
}
