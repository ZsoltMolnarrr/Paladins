package net.paladins.client.entity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.entity.LightwellEntity;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn, then wired to the SummonedEntity animation states.
public class LightwellEntityModel extends SinglePartEntityModel<LightwellEntity> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier(PaladinsMod.ID, "lightwell"), "main");

    private final ModelPart root;
    private final ModelPart light;

    public LightwellEntityModel(ModelPart root) {
        this.root = root.getChild("root");
        this.light = this.root.getChild("light");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 23).cuboid(-6.0F, -3.0F, -6.0F, 12.0F, 6.0F, 12.0F, new Dilation(0.0F))
        .uv(24, 5).cuboid(5.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new Dilation(0.0F))
        .uv(0, 0).cuboid(-7.0F, -5.0F, 5.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F))
        .uv(0, 5).cuboid(-7.0F, -5.0F, -5.0F, 2.0F, 3.0F, 10.0F, new Dilation(0.0F))
        .uv(0, 18).cuboid(-7.0F, -5.0F, -7.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

        ModelPartData cube_r1 = root.addChild("cube_r1", ModelPartBuilder.create().uv(48, 0).cuboid(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
        .uv(48, 0).cuboid(-1.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        ModelPartData cube_r2 = root.addChild("cube_r2", ModelPartBuilder.create().uv(48, 0).mirrored().cuboid(-3.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-7.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        ModelPartData light = root.addChild("light", ModelPartBuilder.create().uv(3, 44).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(LightwellEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.updateAnimation(entity.spawnAnimationState,   LightwellEntityAnimations.spawn, ageInTicks,  1F);
        this.updateAnimation(entity.despawnAnimationState, LightwellEntityAnimations.spawn, ageInTicks, -1F);

        boolean anyAction = false;
        if (entity.spellReleaseAnimationState.isRunning()) {
            float releaseSpeed = entity.getSpellReleaseAnimationSpeed(LightwellEntityAnimations.spell_release.lengthInSeconds() * 20F);
            this.updateAnimation(entity.spellReleaseAnimationState, LightwellEntityAnimations.spell_release, ageInTicks, releaseSpeed);
            anyAction = true;
        }
        if (!anyAction) {
            this.updateAnimation(entity.idleAnimationState, LightwellEntityAnimations.idle, ageInTicks, 1F);
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay,
                       float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
