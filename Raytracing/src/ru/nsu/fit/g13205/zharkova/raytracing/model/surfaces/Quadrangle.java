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
public class Quadrangle extends Surface {

    private Point3D point1;
    private Point3D point2;
    private Point3D point3;
    private Point3D point4;
    private Triangle t1;
    private Triangle t2;
    private Point3D normal;
    private ArrayList<Edge3D> edges;

    public Quadrangle(Point3D point1, Point3D point2, Point3D point3, Point3D point4, Intensive kd, Intensive ks, double power){
        super(kd ,ks, power);
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.t1 = new Triangle(point2, point3, point1, kd, ks, power);
        this.t2 = new Triangle(point1, point3, point4, kd ,ks, power);
        this.edges = new ArrayList<>();
    }

    @Override
    public Point3D getMaxPoint() {
        double maxX, maxY, maxZ;
        maxX = maxY = maxZ = -1000000;
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
        minZ = minX = minY = 1000000;
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
        edges.add(new Edge3D(point1, point2));
        edges.add(new Edge3D(point2, point3));
        edges.add(new Edge3D(point3, point4));
        edges.add(new Edge3D(point4, point1));
    }


    @Override
    public ArrayList<Edge3D> getEdges() {
        return edges;
    }

    @Override
    public Point3D intersect(Ray ray) {
        Point3D hit = null;
        Intersection intersection = new Intersection(t1, ray);
        if(intersection.intersect()){
            hit = intersection.getHit();
            normal = t1.getNormal(hit);
            return hit;
        }
        Intersection intersection1 = new Intersection(t2, ray);
        if(intersection1.intersect()){
            hit = intersection1.getHit();
            normal = t2.getNormal(hit);
            return hit;
        }
        return null;
    }

    @Override
    public Point3D getNormal(Point3D hit) {
        return normal;
    }


}
