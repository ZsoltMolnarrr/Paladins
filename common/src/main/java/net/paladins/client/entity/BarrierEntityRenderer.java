package net.paladins.client.entity;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.paladins.PaladinsMod;
import net.paladins.entity.BarrierEntity;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.client.compatibility.ShaderCompatibility;
import net.spell_engine.client.util.Color;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public class BarrierEntityRenderer<T extends BarrierEntity> extends EntityRenderer<T, BarrierEntityRenderer.State> {
    /// Rendering was split into extraction and render in 1.21.2; the barrier is drawn from a
    /// deferred batch (see {@link #submit}), so the state only carries the entity.
    public static class State extends EntityRenderState {
        @Nullable public BarrierEntity barrier;
    }

    public static final Identifier blankTextureId = Identifier.fromNamespaceAndPath(PaladinsMod.ID, "spell_effect/barrier");
    public static final List<BarrierEntity> activeBarriers = new ArrayList<>();

    private static final int[] LIGHT_UP_ORDER = {0, 2, 8, 6, 4, 3, 9, 1, 5, 10, 7, 11};

    /// Submits the batched barrier geometry. Loader-neutral — each platform's client entrypoint calls this
    /// from its own hook (Fabric `LevelRenderEvents.COLLECT_SUBMITS`; NeoForge `SubmitCustomGeometryEvent`),
    /// mirroring SpellEngine's `BeamRenderer.submit`. Both hooks run at the end of
    /// `LevelRenderer#submitFeatures`, i.e. after `submitEntities` has filled {@link #activeBarriers},
    /// and hand out the same camera-relative identity pose stack the entity submits get.
    /// (26.2 removed `MultiBufferSource`; up to 26.1 this drew immediately in an after-translucent event.)
    public static void submit(PoseStack matrices, SubmitNodeCollector collector, Camera camera, float tickDelta) {
        renderAllInWorld(matrices, collector, camera, LightCoordsUtil.FULL_BRIGHT, tickDelta);
    }

    public BarrierEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T entity, State state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.barrier = entity;
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        var entity = state.barrier;
        if (entity != null && entity.isAlive()) {
            activeBarriers.add(entity); // rendering is batched, and instead called AFTER_TRANSLUCENT to correctly apply transparency(issues with transparency may still persist with other objects)
        }
        super.submit(state, matrices, queue, cameraState);
    }

    public static void renderAllInWorld(PoseStack matrices, SubmitNodeCollector vertexConsumers, Camera camera, int light, float tickDelta) {
        if (activeBarriers.isEmpty()) {
            return;
        }
        matrices.pushPose();
        Vec3 camPos = camera.position();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);
        var config = ShaderCompatibility.isShaderPackInUse() ? Config.IRIS : Config.VANILLA;
        for (BarrierEntity entity : activeBarriers) {
            matrices.pushPose();
            matrices.translate(entity.getX(), entity.getY()+1, entity.getZ());
            // One custom-geometry submit per barrier: the collector copies the pose now, the lambda writes
            // the vertices when the feature renderer builds the frame (vanilla `BeaconRenderer` idiom).
            vertexConsumers.submitCustomGeometry(matrices, config.layer(), (pose, vertices) ->
                    renderShield(entity, pose, vertices, light, tickDelta, config));
            matrices.popPose();
        }
        matrices.popPose();
        activeBarriers.clear();
    }

    private record Config(
            RenderType layer,
            float red,
            float green,
            float blue,
            float alpha,
            float panelFlashAlpha,
            float expirationPulseAlpha) {

        private static final Color shield = Color.from(0xffcc66);

        // 1.21.11: a layer is a `RenderSetup` over a `RenderPipeline`. Same split as 1.21.1: vanilla draws
        // through the beacon-beam program with translucent blending, shader packs through an additive
        // (lightning-style, SRC_ALPHA/ONE) variant so the panels stay vibrant when bloomed.
        private static final RenderType VANILLA_LAYER =
                CustomLayers.spellObject(TextureAtlas.LOCATION_BLOCKS, LightEmission.GLOW, true);
        // Shader packs: vanilla's lightning pipeline, as on 1.21.1 (flat color, additive; Iris handles it as
        // lightning, not as a full-bright beacon beam, so it does not bloom out)
        private static final RenderType IRIS_LAYER = CustomLayers.spellObjectLightning();

        public static final Config VANILLA = new Config(VANILLA_LAYER,
                shield.red(), shield.green(), shield.blue(), 0.8f, 0.9f, 1f);

        public static final Config IRIS = new Config(IRIS_LAYER,
                shield.red(), shield.green(), shield.blue(), 0.5f, 1f, 0.8f);
    }

    public static void renderShield(BarrierEntity entity, PoseStack.Pose base, VertexConsumer vertexConsumer, int light, float tickDelta, Config config) {
        if (entity == null) {
            return;
        }
        // The submit node handed us a copy of the pose; the segment transforms below are local to it.
        var matrices = new PoseStack();
        matrices.last().set(base);
        var entry = entity.getSpellEntry();
        if (entry == null) {
            return;
        }
        var spell = entry.value();

        float radius = spell.range*0.8f;
        float zSlant = (float) (Math.PI/8f); // the amount of slant along the z axis that the segments have
        float size = (radius*Mth.sqrt(3f))/3f; // half of the side length of each segment - calculated using the formula for triangle side length from height
        float offset = radius*(Mth.sin(zSlant)+1); // offset from the center for each segment - the top of each segment should be exactly `radius` blocks away from the middle

        int overlayUV = OverlayTexture.NO_OVERLAY;

        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(blankTextureId);
        float u1 = sprite.getU0();
        float u2 = sprite.getU1();
        float v1 = sprite.getV0();
        float v2 = sprite.getV1();

        double fullTime = entity.level().getGameTime()/20d;
        long time = entity.level().getGameTime()/20;
        double delta = (fullTime-time)*2; // delta is how far along the animation is
        if (delta > 1) delta = 2-delta; // send in opposite direction if halfway
        delta = 1 - Math.pow(1 - delta, 4); // ease out interpolation

        for (int m = 0; m < 2; m++) { // 2 outer loops, 1 for the top half and 1 for the bottom half
            for (int i = 0; i < 6; i++) { // 6 inner loops, 1 for each segment(since it's a hexagon)
                matrices.pushPose();
                if (m == 0) matrices.mulPose(Axis.XP.rotation((float) Math.PI)); // flip 180 degrees if doing the bottom half
                matrices.translate(offset, 0, 0);
                matrices.rotateAround(Axis.YP.rotation((float) (i/3f*Math.PI)), -offset, 0, 0); // rotate around middle to position segment
                matrices.mulPose(Axis.ZP.rotation(zSlant)); // applying z slant

                float r = config.red();
                float g = config.green();
                float b = config.blue();
                float alpha = config.alpha();

                if (entity.tickCount >= entity.getTimeToLive() - entity.expirationDuration()) {
                    int relAge = entity.getTimeToLive() - entity.expirationDuration() - entity.tickCount;
                    alpha = config.expirationPulseAlpha * Math.abs(Mth.cos((float) ((relAge*1.25f)/10f * Math.PI))); // simple calculation to flash in and out - the PI and 1.25 multiplications are to make it start at full alpha and end at none
                } else  if (time % 12 == LIGHT_UP_ORDER[i+(m*6)]) {
                    //g+=(float) (0.1f*delta);
                    var glow = (float) (0.5f*delta);
                    r = blend(r, 1f, glow);
                    g = blend(g, 1f, glow);
                    b = blend(b, 1f, glow);
                    alpha = blend(alpha, config.panelFlashAlpha(), glow);
                }

                Matrix4f matrix = new Matrix4f(matrices.last().pose()); // copying matrix to avoid issue with sodium's matrix optimizations
                var matrixEntry = matrices.last();
                // Matrix3f normalMatrix = matrixEntry.getNormalMatrix();
                vertexConsumer.addVertex(matrix, 0, radius, -size).setColor(r, g, b, 0f).setUv(u1, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0); // main part
                vertexConsumer.addVertex(matrix, 0, 0, -size).setColor(r, g, b, alpha).setUv(u1, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, 0, size).setColor(r, g, b, alpha).setUv(u2, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u2, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);

                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u1, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0); // flip side, so that it renders from both the inside and outside
                vertexConsumer.addVertex(matrix, 0, 0, size).setColor(r, g, b, alpha).setUv(u1, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, 0, -size).setColor(r, g, b, alpha).setUv(u2, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, radius, -size).setColor(r, g, b, 0f).setUv(u2, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);

                matrices.popPose();
                matrices.pushPose(); // finding the position of the next quad, so that we can grab its vertex for a triangle
                Matrix4f newMatrix = matrices.last().pose();
                if (m == 0) matrices.mulPose(Axis.XP.rotation((float) Math.PI));
                matrices.translate(offset, 0, 0);
                matrices.rotateAround(Axis.YP.rotation((float) ((i-1)/3f*Math.PI)), -offset, 0, 0);
                matrices.mulPose(Axis.ZP.rotation(zSlant));

                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u2, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0); // rendering main part of the connector triangle
                vertexConsumer.addVertex(matrix, 0, 0, size).setColor(r, g, b, alpha).setUv(u2, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(newMatrix, 0, 0, -size).setColor(r, g, b, alpha).setUv(u1, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u1, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);

                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u2, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0); // flip side, so that it renders from both the inside and outside
                vertexConsumer.addVertex(newMatrix, 0, 0, -size).setColor(r, g, b, alpha).setUv(u1, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, 0, size).setColor(r, g, b, alpha).setUv(u2, v1).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                vertexConsumer.addVertex(matrix, 0, radius, size).setColor(r, g, b, 0f).setUv(u1, v2).setOverlay(overlayUV).setLight(light).setNormal(matrixEntry, 0, 0, 0);
                matrices.popPose();
            }
        }
    }

    public static float blend(float min, float max, float delta) {
        return min + (max - min) * delta;
    }
}
