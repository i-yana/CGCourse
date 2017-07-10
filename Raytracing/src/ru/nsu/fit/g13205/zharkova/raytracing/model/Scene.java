package ru.nsu.fit.g13205.zharkova.raytracing.model;


import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces.Surface;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 11.05.16.
 */
public class Scene {
    private Intensive ambientLight;
    private ArrayList<Light> illuminates;
    private ArrayList<Surface> surfaces;
    private Point3D minPoint, maxPoint;

    public Scene(Color ambientLight, ArrayList<Light> illuminates, ArrayList<Surface> surfaces) {
        this.ambientLight = new Intensive(ambientLight.getRed()/255f, ambientLight.getGreen()/255f, ambientLight.getBlue()/255f);
        this.illuminates = illuminates;
        this.surfaces = surfaces;
        for (Surface surface : surfaces) {
            surface.createEdges();
        }
        searchMinMax();
    }

    private void searchMinMax() {
        double maxX, maxY, maxZ;
        double minX, minY, minZ;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        for (Surface r : surfaces){
            Point3D maxs = r.getMaxPoint();
            if(maxs.getX()>maxX){
                maxX = maxs.getX();
            }
            if(maxs.getY()>maxY){
                maxY = maxs.getY();
            }
            if(maxs.getZ()>maxZ){
                maxZ = maxs.getZ();
            }

            Point3D mins = r.getMinPoint();
            if(mins.getX()<minX){
                minX = mins.getX();
            }
            if(mins.getY()<minY){
                minY = mins.getY();
            }
            if(mins.getZ()<minZ){
                minZ = mins.getZ();
            }
        }

        this.maxPoint = new Point3D(maxX, maxY, maxZ);
        this.minPoint = new Point3D(minX, minY, minZ);
    }


    public Intensive getAmbientLight() {
        return ambientLight;
    }

    public ArrayList<Light> getIlluminates() {
        return illuminates;
    }

    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }

    public Point3D getMinPoint() {
        return minPoint;
    }

    public Point3D getMaxPoint() {
        return maxPoint;
    }
}
