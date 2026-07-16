package net.sophiebun.buntsy.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ClockworkMaidenAnimationDefinition {

    public static final AnimationDefinition itemHold = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("ArmLB", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-83.2327F, 27.3007F, 4.5662F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-85.931F, 27.4294F, 2.8569F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-86.6908F, 25.9538F, 2.3449F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-84.9992F, 26.3901F, 3.4313F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-83.2327F, 27.3007F, 4.5662F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ArmRB", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.2189F, -27.9734F, -1.2187F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-88.2547F, -25.4842F, -0.9034F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-89.6486F, -26.7142F, -1.4138F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-89.4617F, -27.9692F, -1.292F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-87.2189F, -27.9734F, -1.2187F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}
