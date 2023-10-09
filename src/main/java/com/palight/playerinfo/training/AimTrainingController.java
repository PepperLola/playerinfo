package com.palight.playerinfo.training;

import com.palight.playerinfo.training.targets.TrackingTarget;
import com.palight.playerinfo.training.targets.TrainingTarget;

import java.util.ArrayList;
import java.util.List;

public class AimTrainingController {
    private static List<TrainingTarget> targets = new ArrayList<>();

    public static void addTarget(TrainingTarget target) {
        targets.add(target);
    }

    public static void removeTarget(TrainingTarget target) {
        targets.remove(target);
    }

    public static List<TrainingTarget> getTargets() {
        return targets;
    }

    public static void clearTargets() {
        targets.clear();
    }

    public static void update() {
        if (targets.isEmpty())
            targets.add(new TrackingTarget(0, 10, 0, 2.0F));
        targets.forEach(TrainingTarget::update);
    }

    public static void render() {
        targets.forEach(TrainingTarget::render);
    }
}
