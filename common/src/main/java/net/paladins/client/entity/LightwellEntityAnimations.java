package net.paladins.client.entity;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.19 or later with Yarn mappings
 */
public class LightwellEntityAnimations {
    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(1.5F).looping()
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.SCALE,
            new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .build();

    public static final AnimationDefinition spawn = AnimationDefinition.Builder.withLength(1.0F)
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.SCALE,
            new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.3F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.SCALE,
            new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.35F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .build();

    public static final AnimationDefinition spell_release = AnimationDefinition.Builder.withLength(1.0F)
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0.05F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.2F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, -362.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, -362.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.05F, KeyframeAnimations.posVec(0.0F, -0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.SCALE,
            new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.05F, KeyframeAnimations.scaleVec(1.0F, 0.8F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.0F, 1.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.0F, 1.9F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.65F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .build();
}
