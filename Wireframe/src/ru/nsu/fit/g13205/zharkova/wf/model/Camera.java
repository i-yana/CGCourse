package ru.nsu.fit.g13205.zharkova.wf.model;


import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;

/**
 * Created by Yana on 24.04.16.
 */
public class Camera {

    Point3D position;
    Point3D lookAt;
    Point3D up;

    public Camera(Point3D position, Point3D lookAt){
        this.position = position;
        this.lookAt = lookAt;
        this.up = new Point3D(0, 1, 0);
    }

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D position) {
        this.position = position;
    }

    public Point3D getLookAt() {
        return lookAt;
    }

    public void setLookAt(Point3D lookAt) {
        this.lookAt = lookAt;
    }

    public Point3D getUp() {
        return up;
    }

    public void setUp(Point3D up) {
        this.up = up;
    }
}
