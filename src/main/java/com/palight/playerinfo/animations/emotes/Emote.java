package com.palight.playerinfo.animations.emotes;

import com.palight.playerinfo.animations.states.impl.PlayerAnimationState;

import java.util.ArrayList;
import java.util.List;

public abstract class Emote {
    protected List<AnimationTransition> transitions;
    public abstract long getDurationMillis();

    public PlayerAnimationState getAnimationState(double t) {
        AnimationTransition current = null;
        for (AnimationTransition transition : this.transitions) {
            if (t - transition.getDuration() < 0) {
                current = transition;
                break;
            }

            t -= transition.getDuration();
        }
        if (current == null) return this.transitions.get(0).getFrom();
        return current.getFrom().interpolate(current.getTo(), t / current.getDuration());
    }

    public Emote() {
        this.transitions = new ArrayList<>();
    }

    protected static class AnimationTransition {
        private final PlayerAnimationState from;
        private final PlayerAnimationState to;
        private final double duration; // in terms of t, 0 <= t <= 1

        public AnimationTransition(PlayerAnimationState from, PlayerAnimationState to, double duration) {
            this.from = from;
            this.to = to;
            this.duration = duration;
        }

        public PlayerAnimationState getFrom() {
            return from;
        }

        public PlayerAnimationState getTo() {
            return to;
        }

        public double getDuration() {
            return duration;
        }
    }
}
