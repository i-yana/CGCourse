package ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

/**
 * Created by Yana on 16.05.16.
 */
public class Ray {

    private Point3D origin;
    private Point3D direction;

    public Ray(Point3D origin, Point3D direction) {
        this.direction = direction.normalize();
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ",\ndirection=" + direction +
                '}';
    }

    public Point3D getOrigin() {
        return origin;
    }


    public Point3D getDirection() {
        return direction;
    }
}
