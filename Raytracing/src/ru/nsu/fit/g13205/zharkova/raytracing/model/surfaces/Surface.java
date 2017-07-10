package ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces;


import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Edge3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Ray;

import java.util.ArrayList;

/**
 * Created by Yana on 14.05.16.
 */
public abstract class Surface {
    private Intensive kd, ks;
    private double power;

    public Surface(Intensive kd, Intensive ks, double power){
        this.kd = kd;
        this.ks = ks;
        this.power = power;
    }

    @Override
    public String toString() {
        return "Surface{" + this.getClass().getCanonicalName() +"}";
    }

    public double getPower() {
        return power;
    }

    public abstract Point3D getMaxPoint();

    public abstract Point3D getMinPoint();

    public abstract void createEdges();

    public abstract ArrayList<Edge3D> getEdges();

    public abstract Point3D intersect(Ray ray);

    public abstract Point3D getNormal(Point3D hit);

    public Intensive getKd() {
        return kd;
    }

    public Intensive getKs() {
        return ks;
    }
}
