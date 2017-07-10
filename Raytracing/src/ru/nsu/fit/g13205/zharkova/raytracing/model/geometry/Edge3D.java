package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;


/**
 * Created by Yana on 14.05.16.
 */
public class Edge3D {

    private Point3D p1;
    private Point3D p2;

    public Edge3D(Point3D p1, Point3D p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point3D getP1() {
        return p1;
    }

    public Point3D getP2() {
        return p2;
    }
}
