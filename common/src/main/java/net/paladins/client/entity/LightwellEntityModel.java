package net.paladins.client.entity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn, then wired to the SummonedEntity animation states.
public class LightwellEntityModel extends EntityModel<LightwellEntityRenderer.State> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(Identifier.of(PaladinsMod.ID, "lightwell"), "main");

    private final ModelPart light;
    private final Animation spawnAnimation;
    private final Animation idleAnimation;
    private final Animation spellReleaseAnimation;

    public LightwellEntityModel(ModelPart root) {
        super(root);
        this.light = root.getChild("root").getChild("light");
        this.spawnAnimation = LightwellEntityAnimations.spawn.createAnimation(root);
        this.idleAnimation = LightwellEntityAnimations.idle.createAnimation(root);
        this.spellReleaseAnimation = LightwellEntityAnimations.spell_release.createAnimation(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 23).cuboid(-6.0F, -3.0F, -6.0F, 12.0F, 6.0F, 12.0F, new Dilation(0.0F))
        .uv(24, 5).cuboid(5.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new Dilation(0.0F))
        .uv(0, 0).cuboid(-7.0F, -5.0F, 5.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F))
        .uv(0, 5).cuboid(-7.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new Dilation(0.0F))
        .uv(0, 18).cuboid(-7.0F, -5.0F, -7.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 21.0F, 0.0F));

        ModelPartData cube_r1 = root.addChild("cube_r1", ModelPartBuilder.create().uv(48, 0).cuboid(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
        .uv(48, 0).cuboid(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        ModelPartData cube_r2 = root.addChild("cube_r2", ModelPartBuilder.create().uv(48, 0).mirrored().cuboid(-3.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-7.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        ModelPartData light = root.addChild("light", ModelPartBuilder.create().uv(3, 44).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -5.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(LightwellEntityRenderer.State state) {
        super.setAngles(state);
        this.spawnAnimation.apply(state.spawnAnimationState,   state.age,  1F);
        this.spawnAnimation.apply(state.despawnAnimationState, state.age, -1F);

        boolean anyAction = false;
        if (state.spellReleaseAnimationState.isRunning()) {
            this.spellReleaseAnimation.apply(state.spellReleaseAnimationState, state.age, state.spellReleaseSpeed);
            anyAction = true;
        }
        if (!anyAction) {
            this.idleAnimation.apply(state.idleAnimationState, state.age, 1F);
        }
    }
}
