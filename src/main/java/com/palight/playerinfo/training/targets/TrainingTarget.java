package com.palight.playerinfo.training.targets;

import com.palight.playerinfo.rendering.training.TargetRenderer;
import com.palight.playerinfo.util.math.CylindricalCoords;
import com.palight.playerinfo.util.math.Vector3d;
import net.minecraft.client.Minecraft;

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
