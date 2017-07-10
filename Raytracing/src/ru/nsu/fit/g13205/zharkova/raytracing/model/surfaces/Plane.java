package ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Edge3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Ray;

import java.util.ArrayList;

/**
 * Created by Yana on 23.05.16.
 */
public class Plane extends Surface {

    private double a, b, c, d;
    private Point3D normal;

    public Plane(double a, double b, double c, double d) {
        super(null, null, 0.0);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.normal = new Point3D(a, b, c).normalize();
    }

    @Override
    public Point3D getMaxPoint() {
        return null;
    }

    @Override
    public Point3D getMinPoint() {
        return null;
    }

    @Override
    public void createEdges() {

    }

    @Override
    public ArrayList<Edge3D> getEdges() {
        return null;
    }

    @Override
    public Point3D intersect(Ray ray) {
        double vd = normal.scalar(ray.getDirection());
        if(vd>=0){
            return null;
        }
        double v0 = -normal.scalar(ray.getOrigin())-d;
        double t = v0/vd;
        if(t<0){
            return null;
        }
        return ray.getOrigin().add(ray.getDirection().mul(t));
    }

    @Override
    public Point3D getNormal(Point3D hit) {
        return normal;
    }
}
