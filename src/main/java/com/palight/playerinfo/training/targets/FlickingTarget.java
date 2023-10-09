package com.palight.playerinfo.training.targets;

import com.palight.playerinfo.util.math.CylindricalCoords;
import com.palight.playerinfo.util.math.Vector3d;

public class FlickingTarget extends TrainingTarget {
    public FlickingTarget(double angle, double distance, double height, float size) {
        super(angle, distance, height, size);
    }

    public FlickingTarget(CylindricalCoords position, float size) {
        super(position, size);
    }

    @Override
    public void update() {

    }
}
