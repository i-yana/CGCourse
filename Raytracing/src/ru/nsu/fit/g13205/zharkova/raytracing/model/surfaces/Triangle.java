package ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Edge3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Intersection;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Ray;

import java.util.ArrayList;

/**
 * Created by Yana on 14.05.16.
 */
public class Triangle extends Surface {

    private Point3D v1;
    private Point3D v2;
    private Point3D v3;
    private Plane plane;
    private ArrayList<Edge3D> edges;

    public Triangle(Point3D v1, Point3D v2, Point3D v3, Intensive kd, Intensive ks, double power){
        super(kd, ks, power);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        Point3D normal = ((v2.sub(v1)).cross(v3.sub(v1))).normalize();
        this.edges = new ArrayList<>();
        double a = normal.x;
        double b = normal.y;
        double c = normal.z;
        double d = v1.scalar(normal);
        this.plane = new Plane(a,b,c,-d);
    }

    @Override
    public Point3D getMaxPoint() {
        double maxX, maxY, maxZ;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
        for (Edge3D edge: edges){
            Point3D p1 = edge.getP1();
            Point3D p2 = edge.getP2();
            if(p1.getX()>maxX) maxX = p1.getX();
            if(p2.getX()>maxX) maxX = p2.getX();
            if(p1.getY()>maxY) maxY = p1.getY();
            if(p2.getY()>maxY) maxY = p2.getY();
            if(p1.getZ()>maxZ) maxZ = p1.getZ();
            if(p2.getZ()>maxZ) maxZ = p2.getZ();
        }
        return new Point3D(maxX, maxY, maxZ);
    }

    @Override
    public Point3D getMinPoint() {
        double minX, minY, minZ;
        minZ = minX = minY = Double.POSITIVE_INFINITY;
        for (Edge3D edge: edges){
            Point3D p1 = edge.getP1();
            Point3D p2 = edge.getP2();
            if(p1.getX()<minX) minX = p1.getX();
            if(p2.getX()<minX) minX = p2.getX();
            if(p1.getY()<minY) minY = p1.getY();
            if(p2.getY()<minY) minY = p2.getY();
            if(p1.getZ()<minZ) minZ = p1.getZ();
            if(p2.getZ()<minZ) minZ = p2.getZ();
        }
        return new Point3D(minX, minY, minZ);
    }


    @Override
    public void createEdges() {
        edges.add(new Edge3D(v1, v2));
        edges.add(new Edge3D(v2, v3));
        edges.add(new Edge3D(v3, v1));
    }

    @Override
    public ArrayList<Edge3D> getEdges() {
        return edges;
    }

    @Override
    public Point3D intersect(Ray ray) {
        Intersection intersection = new Intersection(plane, ray);
        Point3D planeHit;
        if(intersection.intersect()){
            planeHit = intersection.getHit();
        }
        else {
            return null;
        }
        double deltaX = Math.max(Math.max(v1.x, v2.x), v3.x)-Math.min(Math.min(v1.x, v2.x), v3.x);
        double deltaZ = Math.max(Math.max(v1.z, v2.z), v3.z)-Math.min(Math.min(v1.z, v2.z), v3.z);
        double deltaY = Math.max(Math.max(v1.y, v2.y), v3.y)-Math.min(Math.min(v1.y, v2.y), v3.y);
        double s,s1,s2,s3;
        if(deltaZ<=deltaX && deltaZ<=deltaY){
            s = area(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
            s1 = area(v1.x, v1.y, v2.x, v2.y, planeHit.x, planeHit.y);
            s2 = area(v1.x, v1.y, planeHit.x, planeHit.y, v3.x, v3.y);
            s3 = area(planeHit.x, planeHit.y, v2.x, v2.y, v3.x, v3.y);
        }else if(deltaY<=deltaX &&deltaY<=deltaZ){
            s = area(v1.x, v1.z, v2.x, v2.z, v3.x, v3.z);
            s1 = area(v1.x, v1.z, v2.x, v2.z, planeHit.x, planeHit.z);
            s2 = area(v1.x, v1.z, planeHit.x, planeHit.z, v3.x, v3.z);
            s3 = area(planeHit.x, planeHit.z, v2.x, v2.z, v3.x, v3.z);
        }
        else if(deltaX<=deltaY && deltaX<=deltaZ){
            s = area(v1.z, v1.y, v2.z, v2.y, v3.z, v3.y);
            s1 = area(v1.z, v1.y, v2.z, v2.y, planeHit.z, planeHit.y);
            s2 = area(v1.z, v1.y, planeHit.z, planeHit.y, v3.z, v3.y);
            s3 = area(planeHit.z, planeHit.y, v2.z, v2.y, v3.z, v3.y);
        }
        else {
            return null;
        }

        double alfa = s1/s;
        double beta = s2/s;
        double gamma = s3/s;
        double sum = alfa+beta+gamma;
        if(alfa<=1 && beta<=1 && gamma<=1 && alfa>=0 && beta>=0 && gamma>=0 && Math.abs(sum-1d)<0.0000001){
            return planeHit;
        }
        return null;
    }

    private double area(double ax, double  ay, double bx, double by, double cx, double cy) {
        return Math.abs(((bx-ax)*(cy-ay)-(cx-ax)*(by-ay))/2);
    }

    @Override
    public Point3D getNormal(Point3D hit) {
        return ((v2.sub(hit)).cross(v3.sub(hit))).normalize();
    }


}
