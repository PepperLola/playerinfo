package com.palight.playerinfo.rendering.cosmetics;

import com.palight.playerinfo.util.NumberUtil;
import com.palight.playerinfo.util.math.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class VerletSimulation {
    private static final Vector3d gravity = new Vector3d(0, -25, 0);
    private List<Point> points = new ArrayList<>();
    private List<Stick> sticks = new ArrayList<>();
    private final int numIterations = 20;

    public boolean init(int partCount) {
        if (points.size() != partCount) {
            points.clear();
            sticks.clear();
            for (int i = 0; i < partCount; i++) {
                Point point = new Point();
                point.position.y = -i;
                point.locked = i == 0;
                points.add(point);
                if (i > 0) {
                    sticks.add(new Stick(points.get(i - 1), point, 1f));
                }
            }
            return true;
        }

        return false;
    }

    public void simulate() {
        float deltaTime = 0.05f;
        // add gravity
        Vector3d down = gravity.copy().mul(deltaTime);
        for (Point p : points) {
            if (!p.locked) {
                Vector3d positionBeforeUpdate = p.position.copy();
//                p.position.add(p.position.sub(p.prevPosition));
//                p.position.add(gravity.mul(deltaTime * deltaTime));
                p.position = p.position.add(down);
                p.prevPosition = positionBeforeUpdate;
            }
//            System.out.println("----------------GRAVITY----------------");
//            System.out.println("X1: " + p.getLerpX(0) + " | X2: " + p.getLerpX(0));
//            System.out.println("Y1: " + p.getLerpY(0) + " | Y2: " + p.getLerpY(0));
        }


        for (int i = 0; i < numIterations; i++) {
            for (int j = sticks.size() - 1; j >= 0; j--) {
                Stick stick = sticks.get(j);
                Vector3d stickCenter = (stick.pointA.position.copy().add(stick.pointB.position)).div(2);
                Vector3d stickDir = (stick.pointA.position.copy().sub(stick.pointB.position)).normalized();

                if (!stick.pointA.locked)
                    stick.pointA.position = stickCenter.copy().add(stickDir.copy().mul(stick.length / 2));
                if (!stick.pointB.locked)
                    stick.pointB.position = stickCenter.copy().sub(stickDir.copy().mul(stick.length / 2));
            }
        }

        for (int i = 0; i < sticks.size(); i++) {
            Stick stick = sticks.get(i);
            Vector3d stickDir = stick.pointA.position.copy().sub(stick.pointB.position).normalized();

            if (!stick.pointB.locked)
                stick.pointB.position = stick.pointA.position.copy().sub(stickDir.mul(stick.length));

//            System.out.println("POINT " + i + " - X1: " + stick.pointA.getLerpX(0) + " | X2: " + stick.pointB.getLerpX(0));
//            System.out.println("POINT " + i + " - Y1: " + stick.pointA.getLerpY(0) + " | Y2: " + stick.pointB.getLerpY(0));
        }

        Point basePoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.position.x - basePoint.position.x > 0) p.position.x = basePoint.position.x;
        }
    }

    public void applyMovement(Vector3d movement) {
        points.get(0).prevPosition.copy(points.get(0).position);
        points.get(0).position = points.get(0).position.add(new Vector3d(movement.x, movement.y, movement.z));
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Stick> getSticks() {
        return sticks;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public static class Point {
        public Vector3d position = new Vector3d();
        public Vector3d prevPosition = new Vector3d();
        public boolean locked;

        public float getLerpX(float deltaTime) {
            return (float) NumberUtil.lerp(deltaTime, prevPosition.x, position.x);
        }

        public float getLerpY(float deltaTime) {
            return (float) NumberUtil.lerp(deltaTime, prevPosition.y, position.y);
        }

        public float getLerpZ(float deltaTime) {
            return (float) NumberUtil.lerp(deltaTime, prevPosition.z, position.z);
        }
    }

    public static class Stick {
        public Point pointA, pointB;
        public float length;

        public Stick(Point pointA, Point pointB, float length) {
            this.pointA = pointA;
            this.pointB = pointB;
            this.length = length;
        }
    }
}
