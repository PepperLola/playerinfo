package com.palight.playerinfo.training.targets;

import com.palight.playerinfo.rendering.training.TargetRenderer;
import com.palight.playerinfo.util.math.CylindricalCoords;
import com.palight.playerinfo.util.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public abstract class TrainingTarget {
    protected CylindricalCoords position;

    protected float size;

    public TrainingTarget(double angle, double distance, double height, float size) {
        this.position = new CylindricalCoords(angle, distance, height);
        this.size = size;
    }

    public TrainingTarget(CylindricalCoords position, float size) {
        this.position = position;
        this.size = size;
    }

    public abstract void update();

    public void render() {
        TargetRenderer.renderTarget(Minecraft.getMinecraft().thePlayer, this);
    }

    public boolean isPlayerLooking(AbstractClientPlayer player) {
        // need to improve this method
        // currently the player can look through the front of the target on the edges, and it won't count
        // since it technically collides with the cylinder outside the cube
        CylindricalCoords playerLook = CylindricalCoords.playerLookToCoords(player.rotationYawHead, player.rotationPitch, this.position.getRadius());
        Vector3d playerLookVector = playerLook.toVector3d();

        Vector3d targetVector = this.getPositionVector3d();

        return Math.abs(playerLookVector.x - targetVector.x) < this.size / 2 &&
                Math.abs(playerLookVector.y - targetVector.y) < this.size / 2 &&
                Math.abs(playerLookVector.z - targetVector.z) < this.size / 2;
    }

    public CylindricalCoords getPosition() {
        return this.position;
    }

    public void setPosition(CylindricalCoords position) {
        this.position = position;
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Vector3d getPositionVector3d() {
        return this.position.toVector3d();
    }

    public double getAngle() {
        return this.position.getAngle();
    }

    public void setAngle(double angle) {
        this.position.setAngle(angle);
    }

    public double getDistance() {
        return this.position.getRadius();
    }
}
