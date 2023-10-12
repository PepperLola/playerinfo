package com.palight.playerinfo.training.targets;

import com.palight.playerinfo.training.AimTrainingController;
import com.palight.playerinfo.util.math.CylindricalCoords;

public class TrackingTarget extends TrainingTarget {
    private static int framesUntilUpdate = 0;
    private static final int MIN_HEIGHT = -4;
    private static final int MAX_HEIGHT = 4;

    private CylindricalCoords velocity = new CylindricalCoords(0, 0, 0);
    public TrackingTarget(double angle, double distance, double height, float size) {
        super(angle, distance, height, size);
    }

    public TrackingTarget(CylindricalCoords position, float size) {
        super(position, size);
    }

    public TrackingTarget(CylindricalCoords position, float size, CylindricalCoords velocity) {
        super(position, size);
        this.velocity = velocity;
    }

    @Override
    public void update() {
        if (--framesUntilUpdate <= 0) {
            int speedMult = 2;
            switch (AimTrainingController.DIFFICULTY) {
                case MEDIUM:
                    speedMult = 4;
                    break;
                case HARD:
                    speedMult = 6;
                    break;
            }
            velocity = CylindricalCoords.random(0.1 / 360D * speedMult, 0, 0.001 * speedMult);
            framesUntilUpdate = (int) Math.floor(Math.random() * 1600) + 400;
        }

        if (this.position.getHeight() + velocity.getHeight() > MAX_HEIGHT || this.position.getHeight() + velocity.getHeight() < MIN_HEIGHT)
            velocity = new CylindricalCoords(velocity.getAngle(), velocity.getRadius(), -velocity.getHeight());

        this.position = this.position.add(velocity);
    }
}
