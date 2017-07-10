package ru.nsu.fit.g13205.zharkova.wf.model.tools;


import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;

/**
 * Created by Yana on 24.04.16.
 */
public class Edge3D {
    public Point3D p1;
    public Point3D p2;

    public Edge3D(Point3D p1, Point3D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point3D getP1() {
        return p1;
    }

    public void setP1(Point3D p1) {
        this.p1 = p1;
    }

    public Point3D getP2() {
        return p2;
    }

    public void setP2(Point3D p2) {
        this.p2 = p2;
    }
}
