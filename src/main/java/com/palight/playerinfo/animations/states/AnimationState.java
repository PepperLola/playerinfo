package com.palight.playerinfo.animations.states;

import javax.vecmath.Vector3d;

public interface AnimationState<T> {
    static double interpolate(double v1, double v2, double t) {
        return (1-t) * v1 + t * v2;
    }

    static Vector3d interpolate(Vector3d v1, Vector3d v2, double t) {
        if (v1 == null && v2 != null) return v2;
        if (v2 == null && v1 != null) return v1;
        if (v1 == null) return null;
        return new Vector3d(interpolate(v1.x, v2.x, t), interpolate(v1.y, v2.y, t), interpolate(v1.z, v2.z, t));
    }

    T interpolate(T other, double t);
}
