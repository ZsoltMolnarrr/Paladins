package net.paladins.client.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.paladins.PaladinsMod;
import net.paladins.entity.BannerEntity;

public class BannerEntityRenderer<T extends BannerEntity> extends EntityRenderer<T, BannerEntityRenderer.State> {
    public static final Identifier TEXTURE =
            Identifier.of(PaladinsMod.ID, "textures/entity/battle_banner.png");

    /// 1.21.2+ split rendering into extraction (`updateRenderState`) and render; the model reads
    /// everything it needs from this snapshot.
    public static class State extends EntityRenderState {
        public float yaw;
        public final AnimationState spawnAnimationState = new AnimationState();
        public final AnimationState idleAnimationState = new AnimationState();
        public final AnimationState despawnAnimationState = new AnimationState();
    }

    private final BattleBannerEntityModel model;

    public BannerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new BattleBannerEntityModel(context.getPart(BattleBannerEntityModel.LAYER));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void updateRenderState(T entity, State state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.yaw = entity.getYaw();
        state.spawnAnimationState.copyFrom(entity.spawnAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.despawnAnimationState.copyFrom(entity.despawnAnimationState);
    }

    @Override
    public void render(State state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        super.render(state, matrices, queue, cameraState);
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-state.yaw + 180F));
        // Standard entity-model space: y-down and x-mirrored, ground plane at y = 1.5
        matrices.scale(-1F, -1F, 1F);
        matrices.translate(0, -1.5, 0);
        queue.submitModel(this.model, state, matrices, this.model.getLayer(TEXTURE),
                state.light, OverlayTexture.DEFAULT_UV, state.outlineColor, null);
        matrices.pop();
    }

    // The banner rendered fullbright before (cloud client_data.light_level = 15) — keep it self-lit.
    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return 15;
    }
}
