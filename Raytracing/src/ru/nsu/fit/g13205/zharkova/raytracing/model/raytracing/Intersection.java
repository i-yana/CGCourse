package ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces.Surface;

/**
 * Created by Yana on 16.05.16.
 */
public class Intersection {

    private Surface surface;
    private Ray ray;
    private Point3D hit;
    private boolean intersect;

    public Intersection(Surface s, Ray ray) {
        this.surface = s;
        this.ray = ray;
        hit = surface.intersect(ray);
        if(hit!=null){
            intersect = true;
        }
        else intersect = false;
    }

    public boolean intersect(){
        return intersect;
    }

    public Point3D getHit(){
        return  hit;
    }
}
