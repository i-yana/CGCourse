package ru.nsu.fit.g13205.zharkova.raytracing.model;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

/**
 * Created by Yana on 26.04.16.
 */
public class Clipper {

    public static boolean tryPoint(Point3D p1, Point3D p2, Wireframe wireframe){
        double zn = wireframe.getZn();
        double zf = wireframe.getZf();
/*
        if(p1.getZ() >= zn && p1.getZ() <= zf && p2.getZ() >= zn && p2.getZ() <= zf) {
            return true;
        }*/

        return true;
    }
}
