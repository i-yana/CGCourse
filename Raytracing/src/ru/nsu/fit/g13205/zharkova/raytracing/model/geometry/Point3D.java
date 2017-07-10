package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;

import java.util.Locale;

/**
 * Created by Yana on 24.04.16.
 */
public class Point3D extends javafx.geometry.Point3D {
    public double x;
    public double y;
    public double z;
    public double w;

    public Point3D(double x, double y, double z, double w) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Point3D(double x, double y, double z) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.;
    }

    public Point3D add(Point3D another){
        return new Point3D(this.x + another.x, this.y + another.y, this.z + another.z, 1.);
    }

    public Point3D sub(Point3D another) {
        return new Point3D(this.x - another.x, this.y - another.y, this.z - another.z, 1.);
    }

    public Point3D normalize() {
        Double k = Math.sqrt(x * x + y * y + z * z);
        return new Point3D(x/k, y/k, z/k, w/k);
    }

    public Point3D cross(Point3D another) {
        return new Point3D((this.y*another.z - this.z*another.y), (this.z*another.x - this.x*another.z), (this.x*another.y - this.y*another.x),1.);
    }

    public Double getW() {
        return w;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"%f %f %f",x,y,z);
    }

    public Point3D div(Point3D another) {
        return new Point3D(this.x/another.x,this.y/another.y,this.z/another.z);
    }

    public Point3D mul(Point3D another) {
        return new Point3D(this.x*another.x,this.y*another.y,this.z*another.z);
    }

    public Point3D mul(double another) {
        return new Point3D(this.x*another,this.y*another,this.z*another);
    }

    public double length() {
        return  Math.sqrt(x * x + y * y + z * z);
    }

    public double scalar(Point3D another) {
        return this.x*another.x + this.y*another.y+ this.z*another.z;
    }

    public Point3D pow(double power) {
        return new Point3D(Math.pow(this.x,power), Math.pow(this.y,power),Math.pow(this.z,power));
    }

    public Point3D abs() {
        return new Point3D(Math.abs(x), Math.abs(y), Math.abs(z));
    }
}
