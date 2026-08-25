package net.paladins.client.entity;

import net.minecraft.client.render.entity.animation.AnimationDefinition;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.19 or later with Yarn mappings
 */
public class LightwellEntityAnimations {
    public static final AnimationDefinition idle = AnimationDefinition.Builder.create(1.5F).looping()
        .addBoneAnimation("root", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("root", new Transformation(Transformation.Targets.MOVE_ORIGIN,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("light", new Transformation(Transformation.Targets.MOVE_ORIGIN,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("light", new Transformation(Transformation.Targets.SCALE,
            new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 0.9F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
        ))
        .build();

    public static final AnimationDefinition spawn = AnimationDefinition.Builder.create(1.0F)
        .addBoneAnimation("root", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 360.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.6F, AnimationHelper.createRotationalVector(0.0F, -7.5F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("root", new Transformation(Transformation.Targets.MOVE_ORIGIN,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.3F, AnimationHelper.createTranslationalVector(0.0F, 8.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.6F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.8F, AnimationHelper.createTranslationalVector(0.0F, 0.25F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.9F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("root", new Transformation(Transformation.Targets.SCALE,
            new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.3F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.75F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("light", new Transformation(Transformation.Targets.SCALE,
            new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.35F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
        ))
        .build();

    public static final AnimationDefinition spell_release = AnimationDefinition.Builder.create(1.0F)
        .addBoneAnimation("root", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.05F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.2F, AnimationHelper.createRotationalVector(0.0F, 2.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.45F, AnimationHelper.createRotationalVector(0.0F, -362.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6F, AnimationHelper.createRotationalVector(0.0F, -362.5F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.7F, AnimationHelper.createRotationalVector(0.0F, -360.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("root", new Transformation(Transformation.Targets.MOVE_ORIGIN,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.05F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.45F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("light", new Transformation(Transformation.Targets.MOVE_ORIGIN,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("light", new Transformation(Transformation.Targets.SCALE,
            new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.05F, AnimationHelper.createScalingVector(1.0F, 0.8F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.2F, AnimationHelper.createScalingVector(1.0F, 1.9F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4F, AnimationHelper.createScalingVector(1.0F, 1.9F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.65F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
        ))
        .build();
}
