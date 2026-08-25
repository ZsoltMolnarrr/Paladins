package net.paladins.client.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.paladins.PaladinsMod;
import net.paladins.entity.LightwellEntity;

public class LightwellEntityRenderer
        extends MobEntityRenderer<LightwellEntity, LightwellEntityRenderer.State, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(PaladinsMod.ID, "textures/entity/lightwell_base.png");

    private static final float FLOAT_AMPLITUDE = 0.1F;
    private static final float FLOAT_FREQUENCY = (float)(Math.PI / 20.0); // 2-second cycle (40 ticks)

    public static class State extends LivingEntityRenderState {
        public final AnimationState spawnAnimationState = new AnimationState();
        public final AnimationState despawnAnimationState = new AnimationState();
        public final AnimationState idleAnimationState = new AnimationState();
        public final AnimationState spellReleaseAnimationState = new AnimationState();
        public float spellReleaseSpeed = 1F;
        public float width = 0.9F;
    }

    public LightwellEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new LightwellEntityModel(context.getPart(LightwellEntityModel.LAYER)), 0.4f);
        this.addFeature(new LightwellGlowFeatureRenderer(this));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void updateRenderState(LightwellEntity entity, State state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.spawnAnimationState.copyFrom(entity.spawnAnimationState);
        state.despawnAnimationState.copyFrom(entity.despawnAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.spellReleaseAnimationState.copyFrom(entity.spellReleaseAnimationState);
        state.spellReleaseSpeed = entity.getSpellReleaseAnimationSpeed(
                LightwellEntityAnimations.spell_release.lengthInSeconds() * 20F);
        state.width = entity.getWidth();
    }

    @Override
    protected void setupTransforms(State state, MatrixStack matrices, float animationProgress, float scale) {
        super.setupTransforms(state, matrices, animationProgress, scale);
        // Lift off the ground plus a gentle levitation bob, mirroring the Frost Elemental's float.
        var groundOffset = state.width * 0.25F;
        matrices.translate(0.0, groundOffset + MathHelper.sin(animationProgress * FLOAT_FREQUENCY) * FLOAT_AMPLITUDE, 0.0);
    }

    @Override
    public Identifier getTexture(State state) {
        return TEXTURE;
    }
}
