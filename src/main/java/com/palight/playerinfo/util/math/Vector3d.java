package com.palight.playerinfo.util.math;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d() {
        this(0, 0, 0);
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vector3d add(Vector3d other) {
        return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3d sub(Vector3d other) {
        return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3d mul(Vector3d other) {
        return new Vector3d(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public Vector3d mul(double scalar) {
        return new Vector3d(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3d div(Vector3d other) {
        return new Vector3d(this.x / other.x, this.y / other.y, this.z / other.z);
    }

    public Vector3d div(double scalar) {
        return new Vector3d(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public Vector3d normalized() {
        double l = Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
        if (l < 1.0E-4D) {
            return new Vector3d(0, 0, 0);
        } else {
            return new Vector3d(this.x / l, this.y / l, this.z / l);
        }
    }

    public void copy(Vector3d other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3d copy() {
        return new Vector3d(this.x, this.y, this.z);
    }
}
