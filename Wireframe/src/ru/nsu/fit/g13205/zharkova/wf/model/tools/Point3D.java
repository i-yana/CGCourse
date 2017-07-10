package ru.nsu.fit.g13205.zharkova.wf.model.tools;

/**
 * Created by Yana on 24.04.16.
 */
public class Point3D extends javafx.geometry.Point3D {
    public Double x;
    public Double y;
    public Double z;
    public Double w;

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

    public Point3D sub(Point3D another) {
        return new Point3D(this.x - another.x, this.y - another.y, this.z - another.z, 1.);
    }

    public Point3D normalize() {
        Double k = Math.sqrt(x * x + y * y + z * z);
        return new Point3D(x/k, y/k, z/k, w/k);
    }

    public Point3D cross(Point3D another) {
        return new Point3D((this.y*another.z - this.z*another.y), (this.x*another.z - this.z*another.x), (this.x*another.y - this.y*another.x),1.);
    }

    public Double getW() {
        return w;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
