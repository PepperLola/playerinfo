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
                        new PlayerAnimationState(null, null, new Vector3d(0, 0, 0), null, null),
                        new PlayerAnimationState(null, null, new Vector3d(0, 0, 2 * Math.PI / 3), null, null),
                        0.5
                ),
                new AnimationTransition(
                        new PlayerAnimationState(null, null, new Vector3d(0, 0, 2 * Math.PI / 3), null, null),
                        new PlayerAnimationState(null, null, new Vector3d(0, 0, 0), null, null),
                        0.5
                )
        );
    }

    @Override
    public long getDurationMillis() {
        return 1000;
    }


}
