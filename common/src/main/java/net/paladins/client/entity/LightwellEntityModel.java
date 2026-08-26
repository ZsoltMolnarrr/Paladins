package net.paladins.client.entity;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn, then wired to the SummonedEntity animation states.
public class LightwellEntityModel extends EntityModel<LightwellEntityRenderer.State> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(PaladinsMod.ID, "lightwell"), "main");

    private final ModelPart light;
    private final KeyframeAnimation spawnAnimation;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation spellReleaseAnimation;

    public LightwellEntityModel(ModelPart root) {
        super(root);
        this.light = root.getChild("root").getChild("light");
        this.spawnAnimation = LightwellEntityAnimations.spawn.bake(root);
        this.idleAnimation = LightwellEntityAnimations.idle.bake(root);
        this.spellReleaseAnimation = LightwellEntityAnimations.spell_release.bake(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 23).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
        .texOffs(24, 5).addBox(5.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(0, 0).addBox(-7.0F, -5.0F, 5.0F, 14.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(0, 5).addBox(-7.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
        .texOffs(0, 18).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(48, 0).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-3.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition light = root.addOrReplaceChild("light", CubeListBuilder.create().texOffs(3, 44).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(LightwellEntityRenderer.State state) {
        super.setupAnim(state);
        this.spawnAnimation.apply(state.spawnAnimationState,   state.ageInTicks,  1F);
        this.spawnAnimation.apply(state.despawnAnimationState, state.ageInTicks, -1F);

        boolean anyAction = false;
        if (state.spellReleaseAnimationState.isStarted()) {
            this.spellReleaseAnimation.apply(state.spellReleaseAnimationState, state.ageInTicks, state.spellReleaseSpeed);
            anyAction = true;
        }
        if (!anyAction) {
            this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks, 1F);
        }
    }
}
