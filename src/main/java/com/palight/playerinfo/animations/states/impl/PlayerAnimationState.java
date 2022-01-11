package com.palight.playerinfo.animations.states.impl;

import com.palight.playerinfo.animations.states.AnimationState;

import javax.vecmath.Vector3d;

public class PlayerAnimationState implements AnimationState<PlayerAnimationState> {
    private Vector3d headRotation;
    private Vector3d leftArmRotation;
    private Vector3d rightArmRotation;
    private Vector3d leftLegRotation;
    private Vector3d rightLegRotation;
    private Vector3d bodyRotation;

    private Vector3d headTranslation;
    private Vector3d leftArmTranslation;
    private Vector3d rightArmTranslation;
    private Vector3d leftLegTranslation;
    private Vector3d rightLegTranslation;
    private Vector3d bodyTranslation;

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

    public PlayerAnimationState setBodyRotation(Vector3d bodyRotation) {
        this.bodyRotation = bodyRotation;
        return this;
    }

    public PlayerAnimationState setHeadTranslation(Vector3d headTranslation) {
        this.headTranslation = headTranslation;
        return this;
    }

    public PlayerAnimationState setLeftArmTranslation(Vector3d leftArmTranslation) {
        this.leftArmTranslation = leftArmTranslation;
        return this;
    }

    public PlayerAnimationState setRightArmTranslation(Vector3d rightArmTranslation) {
        this.rightArmTranslation = rightArmTranslation;
        return this;
    }

    public PlayerAnimationState setLeftLegTranslation(Vector3d leftLegTranslation) {
        this.leftLegTranslation = leftLegTranslation;
        return this;
    }

    public PlayerAnimationState setRightLegTranslation(Vector3d rightLegTranslation) {
        this.rightLegTranslation = rightLegTranslation;
        return this;
    }

    public PlayerAnimationState setBodyTranslation(Vector3d bodyTranslation) {
        this.bodyTranslation = bodyTranslation;
        return this;
    }

    public Vector3d getHeadRotation() {
        return headRotation;
    }

    public Vector3d getLeftArmRotation() {
        return leftArmRotation;
    }

    public Vector3d getRightArmRotation() {
        return rightArmRotation;
    }

    public Vector3d getLeftLegRotation() {
        return leftLegRotation;
    }

    public Vector3d getRightLegRotation() {
        return rightLegRotation;
    }

    public Vector3d getBodyRotation() {
        return bodyRotation;
    }

    public Vector3d getHeadTranslation() {
        return headTranslation;
    }

    public Vector3d getLeftArmTranslation() {
        return leftArmTranslation;
    }

    public Vector3d getRightArmTranslation() {
        return rightArmTranslation;
    }

    public Vector3d getLeftLegTranslation() {
        return leftLegTranslation;
    }

    public Vector3d getRightLegTranslation() {
        return rightLegTranslation;
    }

    public Vector3d getBodyTranslation() {
        return bodyTranslation;
    }

    @Override
    public PlayerAnimationState interpolate(PlayerAnimationState other, double t) {
        Vector3d newHeadRotation = AnimationState.interpolate(this.getHeadRotation(), other.getHeadRotation(), t);
        Vector3d newLeftArmRotation = AnimationState.interpolate(this.getLeftArmRotation(), other.getLeftArmRotation(), t);
        Vector3d newRightArmRotation = AnimationState.interpolate(this.getRightArmRotation(), other.getRightArmRotation(), t);
        Vector3d newLeftLegRotation = AnimationState.interpolate(this.getLeftLegRotation(), other.getLeftLegRotation(), t);
        Vector3d newRightLegRotation = AnimationState.interpolate(this.getRightLegRotation(), other.getRightLegRotation(), t);
        Vector3d newBodyRotation = AnimationState.interpolate(this.getBodyRotation(), other.getBodyRotation(), t);
        Vector3d newHeadTranslation = AnimationState.interpolate(this.getHeadTranslation(), other.getHeadTranslation(), t);
        Vector3d newLeftArmTranslation = AnimationState.interpolate(this.getLeftArmTranslation(), other.getLeftArmTranslation(), t);
        Vector3d newRightArmTranslation = AnimationState.interpolate(this.getRightArmTranslation(), other.getRightArmTranslation(), t);
        System.out.println("RIGHT ARM TRANSLATION AT TIME " + t + " IS " + newRightArmTranslation);
        Vector3d newLeftLegTranslation = AnimationState.interpolate(this.getLeftLegTranslation(), other.getLeftLegTranslation(), t);
        Vector3d newRightLegTranslation = AnimationState.interpolate(this.getRightLegTranslation(), other.getRightLegTranslation(), t);
        Vector3d newBodyTranslation = AnimationState.interpolate(this.getBodyTranslation(), other.getBodyTranslation(), t);
        return new PlayerAnimationState()
                .setHeadRotation(newHeadRotation)
                .setLeftArmRotation(newLeftArmRotation)
                .setRightArmRotation(newRightArmRotation)
                .setLeftLegRotation(newLeftLegRotation)
                .setRightLegRotation(newRightLegRotation)
                .setBodyRotation(newBodyRotation)
                .setHeadTranslation(newHeadTranslation)
                .setLeftArmTranslation(newLeftArmTranslation)
                .setRightArmTranslation(newRightArmTranslation)
                .setLeftLegTranslation(newLeftLegTranslation)
                .setRightLegTranslation(newRightLegTranslation)
                .setBodyTranslation(newBodyTranslation);
    }
}
