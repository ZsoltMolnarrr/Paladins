package net.paladins.client.entity;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.paladins.PaladinsMod;
import net.paladins.entity.BarrierEntity;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.client.compatibility.ShaderCompatibility;
import net.spell_engine.client.util.Color;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.render.RenderPhase.*;

public class BarrierEntityRenderer<T extends BarrierEntity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;

    public static final Identifier blankTextureId = new Identifier(PaladinsMod.ID, "item/barrier");
    public static final List<BarrierEntity> activeBarriers = new ArrayList<>();

    private static final int[] LIGHT_UP_ORDER = {0, 2, 8, 6, 4, 3, 9, 1, 5, 10, 7, 11};

    public static void setup() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            VertexConsumerProvider.Immediate vcProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            renderAllInWorld(context.matrixStack(), vcProvider, context.camera(), LightmapTextureManager.MAX_LIGHT_COORDINATE, context.tickDelta());
        });
    }

    public BarrierEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }


    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.isAlive()) {
            activeBarriers.add(entity); // rendering is batched, and instead called AFTER_TRANSLUCENT to correctly apply transparency(issues with transparency may still persist with other objects)
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public static void renderAllInWorld(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, int light, float tickDelta) {
        matrices.push();
        Vec3d camPos = camera.getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);
        var config = ShaderCompatibility.isShaderPackInUse() ? Config.IRIS : Config.VANILLA;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(config.layer());
        for (BarrierEntity entity : activeBarriers) {
            matrices.push();
            matrices.translate(entity.getX(), entity.getY()+1, entity.getZ());
            renderShield(entity, matrices, vertexConsumer, light, tickDelta, config);
            matrices.pop();
        }
        vertexConsumers.draw();
        matrices.pop();
        activeBarriers.clear();
    }

    private record Config(
            RenderLayer layer,
            float red,
            float green,
            float blue,
            float alpha,
            float panelFlashAlpha,
            float expirationPulseAlpha) {

        private static final Color shield = Color.from(0xffcc66);

        public static final Config VANILLA = new Config(
                CustomLayers.create(
                        SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                        BEACON_BEAM_PROGRAM,
                        TRANSLUCENT_TRANSPARENCY,
                        DISABLE_CULLING,
                        COLOR_MASK,
                        ENABLE_OVERLAY_COLOR,
                        MAIN_TARGET,
                        true),
                shield.red(), shield.green(), shield.blue(), 0.8f, 0.9f, 1f);

        public static final Config IRIS = new Config(
                CustomLayers.create(
                        SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                        ENTITY_TRANSLUCENT_EMISSIVE_PROGRAM,
                        TRANSLUCENT_TRANSPARENCY,
                        DISABLE_CULLING,
                        COLOR_MASK,
                        ENABLE_OVERLAY_COLOR,
                        MAIN_TARGET,
                        false),
                shield.red(), shield.green(), shield.blue(), 0.2f, 0.6f, 0.8f);
    }

    public static void renderShield(BarrierEntity entity, MatrixStack matrices, VertexConsumer vertexConsumer, int light, float tickDelta, Config config) {
        var spell = entity.getSpell();
        if (spell == null) {
            return;
        }

        float radius = spell.range*0.8f;
        float zSlant = (float) (Math.PI/8f); // the amount of slant along the z axis that the segments have
        float size = (radius*MathHelper.sqrt(3f))/3f; // half of the side length of each segment - calculated using the formula for triangle side length from height
        float offset = radius*(MathHelper.sin(zSlant)+1); // offset from the center for each segment - the top of each segment should be exactly `radius` blocks away from the middle

        int overlayUV = OverlayTexture.DEFAULT_UV;

        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(blankTextureId);
        float u1 = sprite.getMinU();
        float u2 = sprite.getMaxU();
        float v1 = sprite.getMinV();
        float v2 = sprite.getMaxV();

        double fullTime = entity.getWorld().getTime()/20d;
        long time = entity.getWorld().getTime()/20;
        double delta = (fullTime-time)*2; // delta is how far along the animation is
        if (delta > 1) delta = 2-delta; // send in opposite direction if halfway
        delta = 1 - Math.pow(1 - delta, 4); // ease out interpolation

        for (int m = 0; m < 2; m++) { // 2 outer loops, 1 for the top half and 1 for the bottom half
            for (int i = 0; i < 6; i++) { // 6 inner loops, 1 for each segment(since it's a hexagon)
                matrices.push();
                if (m == 0) matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.PI)); // flip 180 degrees if doing the bottom half
                matrices.translate(offset, 0, 0);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (i/3f*Math.PI)), -offset, 0, 0); // rotate around middle to position segment
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(zSlant)); // applying z slant

                float r = config.red();
                float g = config.green();
                float b = config.blue();
                float alpha = config.alpha();

                if (entity.age >= entity.getTimeToLive() - entity.expirationDuration()) {
                    int relAge = entity.getTimeToLive() - entity.expirationDuration() - entity.age;
                    alpha = config.expirationPulseAlpha * Math.abs(MathHelper.cos((float) ((relAge*1.25f)/10f * Math.PI))); // simple calculation to flash in and out - the PI and 1.25 multiplications are to make it start at full alpha and end at none
                } else  if (time % 12 == LIGHT_UP_ORDER[i+(m*6)]) {
                    //g+=(float) (0.1f*delta);
                    var glow = (float) (0.5f*delta);
                    r = blend(r, 1f, glow);
                    g = blend(g, 1f, glow);
                    b = blend(b, 1f, glow);
                    alpha = blend(alpha, config.panelFlashAlpha(), glow);
                }

                Matrix4f matrix = new Matrix4f(matrices.peek().getPositionMatrix()); // copying matrix to avoid issue with sodium's matrix optimizations
                Matrix3f normalMatrix = matrices.peek().getNormalMatrix();
                vertexConsumer.vertex(matrix, 0, radius, -size).color(r, g, b, 0f).texture(u1, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next(); // main part
                vertexConsumer.vertex(matrix, 0, 0, -size).color(r, g, b, alpha).texture(u1, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, 0, size).color(r, g, b, alpha).texture(u2, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u2, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();

                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u1, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next(); // flip side, so that it renders from both the inside and outside
                vertexConsumer.vertex(matrix, 0, 0, size).color(r, g, b, alpha).texture(u1, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, 0, -size).color(r, g, b, alpha).texture(u2, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, radius, -size).color(r, g, b, 0f).texture(u2, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();

                matrices.pop();
                matrices.push(); // finding the position of the next quad, so that we can grab its vertex for a triangle
                Matrix4f newMatrix = matrices.peek().getPositionMatrix();
                if (m == 0) matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.PI));
                matrices.translate(offset, 0, 0);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) ((i-1)/3f*Math.PI)), -offset, 0, 0);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(zSlant));

                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u2, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next(); // rendering main part of the connector triangle
                vertexConsumer.vertex(matrix, 0, 0, size).color(r, g, b, alpha).texture(u2, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(newMatrix, 0, 0, -size).color(r, g, b, alpha).texture(u1, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u1, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();

                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u2, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next(); // flip side, so that it renders from both the inside and outside
                vertexConsumer.vertex(newMatrix, 0, 0, -size).color(r, g, b, alpha).texture(u1, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, 0, size).color(r, g, b, alpha).texture(u2, v1).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                vertexConsumer.vertex(matrix, 0, radius, size).color(r, g, b, 0f).texture(u1, v2).overlay(overlayUV).light(light).normal(normalMatrix, 0, 0, 0).next();
                matrices.pop();
            }
        }
    }

    public static float blend(float min, float max, float delta) {
        return min + (max - min) * delta;
    }
}
