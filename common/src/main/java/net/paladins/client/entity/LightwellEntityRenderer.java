package net.paladins.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.paladins.PaladinsMod;
import net.paladins.entity.LightwellEntity;

public class LightwellEntityRenderer
        extends MobRenderer<LightwellEntity, LightwellEntityRenderer.State, LightwellEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(PaladinsMod.ID, "textures/entity/lightwell_base.png");

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

    public LightwellEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new LightwellEntityModel(context.bakeLayer(LightwellEntityModel.LAYER)), 0.4f);
        this.addLayer(new LightwellGlowFeatureRenderer(this));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LightwellEntity entity, State state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.spawnAnimationState.copyFrom(entity.spawnAnimationState);
        state.despawnAnimationState.copyFrom(entity.despawnAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.spellReleaseAnimationState.copyFrom(entity.spellReleaseAnimationState);
        state.spellReleaseSpeed = entity.getSpellReleaseAnimationSpeed(
                LightwellEntityAnimations.spell_release.lengthInSeconds() * 20F);
        state.width = entity.getBbWidth();
    }

    @Override
    protected void setupRotations(State state, PoseStack matrices, float animationProgress, float scale) {
        super.setupRotations(state, matrices, animationProgress, scale);
        // Lift off the ground plus a gentle levitation bob, mirroring the Frost Elemental's float.
        var groundOffset = state.width * 0.25F;
        matrices.translate(0.0, groundOffset + Mth.sin(animationProgress * FLOAT_FREQUENCY) * FLOAT_AMPLITUDE, 0.0);
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return TEXTURE;
    }
}
