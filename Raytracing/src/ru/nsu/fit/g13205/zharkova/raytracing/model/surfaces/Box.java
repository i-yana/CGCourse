package ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces;


import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Edge3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Ray;

import java.util.ArrayList;

/**
 * Created by Yana on 14.05.16.
 */
public class Box extends Surface {

    private Point3D minPoint;
    private Point3D maxPoint;
    private ArrayList<Edge3D> edges;
    private Point3D normal;

    public Box(Point3D minPoint, Point3D maxPoint, Intensive kd, Intensive ks, double power){
        super(kd, ks, power);
        this.edges = new ArrayList<>();
        this.maxPoint = maxPoint;
        this.minPoint = minPoint;
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
        ArrayList<Edge3D> temp = new ArrayList<>();
        edges.add(new Edge3D(new Point3D(minPoint.getX(),minPoint.getY(),minPoint.getZ()), new Point3D(minPoint.getX(),maxPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(minPoint.getX(),maxPoint.getY(),minPoint.getZ()), new Point3D(maxPoint.getX(),maxPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),maxPoint.getY(),minPoint.getZ()), new Point3D(maxPoint.getX(),minPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),minPoint.getY(),minPoint.getZ()), new Point3D(minPoint.getX(),minPoint.getY(),minPoint.getZ())));

        edges.add(new Edge3D(new Point3D(minPoint.getX(),minPoint.getY(),maxPoint.getZ()), new Point3D(minPoint.getX(),maxPoint.getY(),maxPoint.getZ())));
        edges.add(new Edge3D(new Point3D(minPoint.getX(),maxPoint.getY(),maxPoint.getZ()), new Point3D(maxPoint.getX(),maxPoint.getY(),maxPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),maxPoint.getY(),maxPoint.getZ()), new Point3D(maxPoint.getX(),minPoint.getY(),maxPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),minPoint.getY(),maxPoint.getZ()), new Point3D(minPoint.getX(),minPoint.getY(),maxPoint.getZ())));

        edges.add(new Edge3D(new Point3D(minPoint.getX(),maxPoint.getY(),maxPoint.getZ()), new Point3D(minPoint.getX(),maxPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),maxPoint.getY(),maxPoint.getZ()), new Point3D(maxPoint.getX(),maxPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(maxPoint.getX(),minPoint.getY(),maxPoint.getZ()), new Point3D(maxPoint.getX(),minPoint.getY(),minPoint.getZ())));
        edges.add(new Edge3D(new Point3D(minPoint.getX(),minPoint.getY(),maxPoint.getZ()), new Point3D(minPoint.getX(),minPoint.getY(),minPoint.getZ())));
    }


    @Override
    public ArrayList<Edge3D> getEdges() {
        return edges;
    }

    @Override
    public Point3D intersect(Ray ray) {
        double near = Double.NEGATIVE_INFINITY;
        double far = Double.POSITIVE_INFINITY;
        if(Math.abs(ray.getDirection().x)<0.0000001) {
            if (ray.getOrigin().x < minPoint.x || ray.getOrigin().x > maxPoint.x) {
                return null;
            }
        }
        else {
            double t1x = (minPoint.x-ray.getOrigin().x)/ray.getDirection().x;
            double t2x = (maxPoint.x-ray.getOrigin().x)/ray.getDirection().x;
            if(t2x < t1x){
                double tmp = t1x;
                t1x = t2x;
                t2x = tmp;
            }
            if(t1x > near){
                near = t1x;
            }
            if(t2x<far){
                far = t2x;
            }
            if(near>far || far<0){
                return null;
            }
        }
        if(Math.abs(ray.getDirection().y)<0.0000001) {
            if (ray.getOrigin().y < minPoint.y || ray.getOrigin().y > maxPoint.y) {
                return null;
            }
        }
        else {
            double t1y = (minPoint.y-ray.getOrigin().y)/ray.getDirection().y;
            double t2y = (maxPoint.y-ray.getOrigin().y)/ray.getDirection().y;
            if(t2y < t1y){
                double tmp = t1y;
                t1y = t2y;
                t2y = tmp;
            }
            if(t1y > near){
                near = t1y;
            }
            if(t2y<far){
                far = t2y;
            }
            if(near>far || far<0){
                return null;
            }
        }
        if(Math.abs(ray.getDirection().z)<0.0000001) {
            if (ray.getOrigin().z < minPoint.z || ray.getOrigin().z > maxPoint.z) {
                return null;
            }
        }
        else {
            double t1z = (minPoint.z-ray.getOrigin().z)/ray.getDirection().z;
            double t2z = (maxPoint.z-ray.getOrigin().z)/ray.getDirection().z;
            if(t2z < t1z){
                double tmp = t1z;
                t1z = t2z;
                t2z = tmp;
            }
            if(t1z > near){
                near = t1z;
            }
            if(t2z<far){
                far = t2z;
            }
            if(near>far || far<0){
                return null;
            }
        }
        if(near<0){
            return null;
        }
        return ray.getOrigin().add(ray.getDirection().mul(near));
    }

    @Override
    public Point3D getNormal(Point3D hit) {
        if(Math.abs(hit.x - minPoint.x)<0.0000001){
            normal = new Point3D(-1,0,0);
        }
        if(Math.abs(hit.y - minPoint.y)<0.0000001){
            normal = new Point3D(0,-1,0);
        }
        if(Math.abs(hit.z-minPoint.z)<0.0000001){
            normal = new Point3D(0,0,-1);
        }
        if(Math.abs(hit.x-maxPoint.x)<0.0000001){
            normal = new Point3D(1,0,0);
        }
        if(Math.abs(hit.y-maxPoint.y)<0.0000001){
            normal = new Point3D(0,1,0);
        }
        if(Math.abs(hit.z-maxPoint.z)<0.0000001){
            normal = new Point3D(0,0,1);
        }
        return normal;
    }


}
