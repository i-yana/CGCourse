package ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Edge3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Matrix;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Ray;

import java.util.ArrayList;

/**
 * Created by Yana on 14.05.16.
 */
public class Sphere extends Surface {

    private Point3D center;
    private double radius;
    private ArrayList<Edge3D> edges;
    private Point3D max, min;

    public Sphere(Point3D center, double radius, Intensive kd, Intensive ks, double power) {
        super(kd, ks, power);
        this.center = center;
        this.radius = radius;
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

        double angleStep = Math.PI / 40;
        double angleRad = 0;
        ArrayList<Point2D> uArray = new ArrayList<>();
        while (angleRad <= Math.PI) {
            Point2D point = new Point2D(radius * Math.cos(angleRad), radius * Math.sin(angleRad));
            uArray.add(point);
            angleRad = angleRad + angleStep;
        }
        Point2D point = new Point2D(radius * Math.cos(Math.PI), radius * Math.sin(Math.PI));
        uArray.add(point);
        ArrayList<Double> phiArray = countPhi();
        for (int i = 0; i < phiArray.size(); i++) {
            Double aPhiArray1 = phiArray.get(i);
            double[][] matrix = Matrix.getRotationMatrix(aPhiArray1);
            ArrayList<Point3D> points = new ArrayList<>();
            for (Point2D anUArray : uArray) {
                double[][] vector = new double[3][1];
                vector[0][0] = anUArray.getY();
                vector[1][0] = anUArray.getY();
                vector[2][0] = anUArray.getX();
                double[][] res = Matrix.mul(matrix, vector);
                Point3D point3D = new Point3D(res[0][0] + center.getX(), res[1][0] + center.getY(), res[2][0] + center.getZ());
                points.add(point3D);
            }
            for (int j = 1; j < points.size(); j++) {
                edges.add(new Edge3D(points.get(j - 1), points.get(j)));
            }
        }
        for (int i = 0; i < uArray.size(); i++) {
            Point2D anUArray = uArray.get(i);
            ArrayList<Point3D> points = new ArrayList<>();
            double[][] vector = new double[3][1];
            vector[0][0] = anUArray.getY();
            vector[1][0] = anUArray.getY();
            vector[2][0] = anUArray.getX();
            for (Double aPhiArray : phiArray) {
                double[][] matrix = Matrix.getRotationMatrix(aPhiArray);
                double[][] res = Matrix.mul(matrix, vector);
                Point3D point3D = new Point3D(res[0][0] + center.getX(), res[1][0] + center.getY(), res[2][0] + center.getZ());
                points.add(point3D);
            }
            for (int j = 1; j < points.size(); j++) {
                edges.add(new Edge3D(points.get(j - 1), points.get(j)));
            }
        }
    }


    public ArrayList<Double> countPhi() {
        double step = 2*Math.PI/40;
        ArrayList<Double> phiArray = new ArrayList<>();
        double start = 0;
        for (int i = 0; i <= 40; i++) {
            phiArray.add(start);
            start = start + step;
        }
        return phiArray;
    }

    @Override
    public ArrayList<Edge3D> getEdges() {
        return edges;
    }

    @Override
    public Point3D intersect(Ray ray) {
        Point3D OC =center.sub(ray.getOrigin());
        if(OC.scalar(OC)<radius*radius){
            return null;
        }
        double ca = OC.scalar(ray.getDirection());
        if(ca<0){
            return null;
        }
        double DD = OC.scalar(OC)-ca*ca;
        double hc = radius*radius-DD;
        if(hc<0){
            return null;
        }
        double t= ca-Math.sqrt(hc);

        Point3D normal = getNormal(ray.getOrigin().add(ray.getDirection().mul(t)));
        if(normal.scalar(ray.getDirection())>=0){
            return null;
        }
        return ray.getOrigin().add(ray.getDirection().mul(t));
    }

    @Override
    public Point3D getNormal(Point3D hit) {
        return hit.sub(center).div(new Point3D(radius, radius, radius)).normalize();
    }
}
