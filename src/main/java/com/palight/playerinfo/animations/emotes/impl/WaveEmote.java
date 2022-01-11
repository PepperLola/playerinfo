package com.palight.playerinfo.animations.emotes.impl;

import com.palight.playerinfo.animations.emotes.Emote;
import com.palight.playerinfo.animations.states.impl.PlayerAnimationState;

import javax.vecmath.Vector3d;
import java.util.Arrays;

public class WaveEmote extends Emote {

    public WaveEmote() {
        super();
        this.transitions = Arrays.asList(
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 0)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        0.25
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        0.1
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        0.1
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        0.1
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        0.1
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 5 * Math.PI / 6)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        0.1
                ),
                new AnimationTransition(
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 2 * Math.PI / 3)),
                        new PlayerAnimationState().setRightArmRotation(new Vector3d(0, 0, 0)),
                        0.25
                )
        );
    }

    @Override
    public long getDurationMillis() {
        return 1000;
    }


}
