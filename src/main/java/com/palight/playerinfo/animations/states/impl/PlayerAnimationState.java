package com.palight.playerinfo.animations.states.impl;

import com.palight.playerinfo.animations.states.AnimationState;

import javax.vecmath.Vector3d;

public class PlayerAnimationState implements AnimationState<PlayerAnimationState> {
    private Vector3d headRotation;
    private Vector3d leftArmRotation;
    private Vector3d rightArmRotation;
    private Vector3d leftLegRotation;
    private Vector3d rightLegRotation;

    // TODO make setters for these so we don't have to pass all of them in

    public PlayerAnimationState(Vector3d headRotation, Vector3d leftArmRotation, Vector3d rightArmRotation, Vector3d leftLegRotation, Vector3d rightLegRotation) {
        this.headRotation = headRotation;
        this.leftArmRotation = leftArmRotation;
        this.rightArmRotation = rightArmRotation;
        this.leftLegRotation = leftLegRotation;
        this.rightLegRotation = rightLegRotation;
    }

    public PlayerAnimationState() {
    }

    public PlayerAnimationState setHeadRotation(Vector3d headRotation) {
        this.headRotation = headRotation;
        return this;
    }

    public PlayerAnimationState setLeftArmRotation(Vector3d leftArmRotation) {
        this.leftArmRotation = leftArmRotation;
        return this;
    }

    public PlayerAnimationState setRightArmRotation(Vector3d rightArmRotation) {
        this.rightArmRotation = rightArmRotation;
        return this;
    }

    public PlayerAnimationState setLeftLegRotation(Vector3d leftLegRotation) {
        this.leftLegRotation = leftLegRotation;
        return this;
    }

    public PlayerAnimationState setRightLegRotation(Vector3d rightLegRotation) {
        this.rightLegRotation = rightLegRotation;
        return this;
    }

    public Vector3d getHeadRotation() {
        return headRotation == null ? new Vector3d(0, 0, 0) : this.headRotation;
    }

    public Vector3d getLeftArmRotation() {
        return leftArmRotation == null ? new Vector3d(0, 0, 0) : this.leftArmRotation;
    }

    public Vector3d getRightArmRotation() {
        return rightArmRotation == null ? new Vector3d(0, 0, 0) : this.rightArmRotation;
    }

    public Vector3d getLeftLegRotation() {
        return leftLegRotation == null ? new Vector3d(0, 0, 0) : this.leftLegRotation;
    }

    public Vector3d getRightLegRotation() {
        return rightLegRotation == null ? new Vector3d(0, 0, 0) : this.rightLegRotation;
    }

    @Override
    public PlayerAnimationState interpolate(PlayerAnimationState other, double t) {
        Vector3d newHeadRotation = AnimationState.interpolate(this.getHeadRotation(), other.getHeadRotation(), t);
        Vector3d newLeftArmRotation = AnimationState.interpolate(this.getLeftArmRotation(), other.getLeftArmRotation(), t);
        Vector3d newRightArmRotation = AnimationState.interpolate(this.getRightArmRotation(), other.getRightArmRotation(), t);
        Vector3d newLeftLegRotation = AnimationState.interpolate(this.getLeftLegRotation(), other.getLeftLegRotation(), t);
        Vector3d newRightLegRotation = AnimationState.interpolate(this.getRightLegRotation(), other.getRightLegRotation(), t);
        return new PlayerAnimationState(newHeadRotation, newLeftArmRotation, newRightArmRotation, newLeftLegRotation, newRightLegRotation);
    }
}
