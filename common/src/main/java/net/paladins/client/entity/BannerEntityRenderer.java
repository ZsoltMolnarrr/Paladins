package net.paladins.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.paladins.PaladinsMod;
import net.paladins.entity.BannerEntity;

public class BannerEntityRenderer<T extends BannerEntity> extends EntityRenderer<T, BannerEntityRenderer.State> {
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "textures/entity/battle_banner.png");

    /// 1.21.2+ split rendering into extraction (`updateRenderState`) and render; the model reads
    /// everything it needs from this snapshot.
    public static class State extends EntityRenderState {
        public float yaw;
        public final AnimationState spawnAnimationState = new AnimationState();
        public final AnimationState idleAnimationState = new AnimationState();
        public final AnimationState despawnAnimationState = new AnimationState();
    }

    private final BattleBannerEntityModel model;

    public BannerEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BattleBannerEntityModel(context.bakeLayer(BattleBannerEntityModel.LAYER));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T entity, State state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.yaw = entity.getYRot();
        state.spawnAnimationState.copyFrom(entity.spawnAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.despawnAnimationState.copyFrom(entity.despawnAnimationState);
    }

    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        super.submit(state, matrices, queue, cameraState);
        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(-state.yaw + 180F));
        // Standard entity-model space: y-down and x-mirrored, ground plane at y = 1.5
        matrices.scale(-1F, -1F, 1F);
        matrices.translate(0, -1.5, 0);
        queue.submitModel(this.model, state, matrices, this.model.renderType(TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        matrices.popPose();
    }

    // The banner rendered fullbright before (cloud client_data.light_level = 15) — keep it self-lit.
    @Override
    protected int getBlockLightLevel(T entity, BlockPos pos) {
        return 15;
    }
}
