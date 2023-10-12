package com.palight.playerinfo.util.math;

public class CylindricalCoords {
    private double angle;
    private double radius;
    private double height;

    public CylindricalCoords(double angle, double radius, double height) {
        this.angle = angle;
        this.radius = radius;
        this.height = height;
    }

    public Vector3d toVector3d() {
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);
        return new Vector3d(x, height, z);
    }

    public CylindricalCoords add(CylindricalCoords other) {
        return new CylindricalCoords(this.angle + other.angle, this.radius + other.radius, this.height + other.height);
    }

    public CylindricalCoords sub(CylindricalCoords other) {
        return new CylindricalCoords(this.angle - other.angle, this.radius - other.radius, this.height - other.height);
    }

    public CylindricalCoords mul(CylindricalCoords other) {
        return new CylindricalCoords(this.angle * other.angle, this.radius * other.radius, this.height * other.height);
    }

    public CylindricalCoords mul(double scalar) {
        return new CylindricalCoords(this.angle * scalar, this.radius * scalar, this.height * scalar);
    }

    public CylindricalCoords div(CylindricalCoords other) {
        return new CylindricalCoords(this.angle / other.angle, this.radius / other.radius, this.height / other.height);
    }

    public CylindricalCoords div(double scalar) {
        return new CylindricalCoords(this.angle / scalar, this.radius / scalar, this.height / scalar);
    }

    public static CylindricalCoords random(double angleScale, double radiusScale, double heightScale) {
        return new CylindricalCoords(
                (2 * Math.random() - 1) * angleScale,
                (2 * Math.random() - 1) * radiusScale,
                (2 * Math.random() - 1) * heightScale
        );
    }

    public static CylindricalCoords random(double minAngle, double maxAngle, double minRadius, double maxRadius, double minHeight, double maxHeight) {
        return new CylindricalCoords(
                Math.random() * (maxAngle - minAngle) + minAngle,
                Math.random() * (maxRadius - minRadius) + minRadius,
                Math.random() * (maxHeight - minHeight) + minHeight
        );
    }

    public static CylindricalCoords playerLookToCoords(double yaw, double pitch, double distance) {
        double angle = Math.toRadians((yaw + 90) % 360);
        double height = Math.tan(-Math.toRadians(pitch)) * distance; // pitch up is negative
        return new CylindricalCoords(angle, distance, height);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
