package ru.nsu.fit.g13205.zharkova.raytracing.model;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

import java.awt.*;

/**
 * Created by Yana on 11.05.16.
 */
public class Light {

    private Point3D position;
    private Intensive intensive;

    public Light(Point3D point, Color color){
        this.position = point;
        float r = color.getRed()/255f;
        float g = color.getGreen()/255f;
        float b = color.getBlue()/255f;
        this.intensive = new Intensive(r,g,b);
    }

    public Point3D getPosition() {
        return position;
    }


    public Intensive getIntensive() {
        return intensive;
    }
}
