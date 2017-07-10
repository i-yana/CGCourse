package ru.nsu.fit.g13205.zharkova.wf.model;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Edge3D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;
import ru.nsu.fit.g13205.zharkova.wf.model.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Yana on 15.04.16.
 */
public class RotationObject {

    private Color objColor;
    private Point3D centralPoint;
    private double[][] eulerMatrix;
    private BSpline spline;
    private ArrayList<Point2D> uArray;
    private ArrayList<Double> phiArray;
    private ArrayList<ArrayList<Point3D>> segments = new ArrayList<>();
    private ArrayList<ArrayList<Point3D>> circles = new ArrayList<>();
    private ArrayList<ArrayList<Point3D>> rotatedSegments = new ArrayList<>();
    private ArrayList<ArrayList<Point3D>> rotatedCircles = new ArrayList<>();
    private ArrayList<Edge3D> axis = new ArrayList<>();
    private Point3D maxPoint;
    private double a, b, c, d;
    private int n, m, k;
    private Point3D minPoint;

    public RotationObject(Color objColor, Point3D centralPoint, double[][] eulerMatrix, ArrayList<Point2D> points){
        a = b = c = d = 0;
        n = m = k = 0;
        this.objColor = objColor;
        this.centralPoint = centralPoint;
        this.eulerMatrix = eulerMatrix;
        eulerMatrix[0][3] = centralPoint.getX();
        eulerMatrix[1][3] = centralPoint.getY();
        eulerMatrix[2][3] = centralPoint.getZ();
        this.spline = new BSpline(points);
        this.phiArray = new ArrayList<>();
        this.uArray = new ArrayList<>();
    }

    public ArrayList<Edge3D> getAxis(){
        return axis;
    }

    public ArrayList<ArrayList<Point3D>> getRotatedSegments(){
        return rotatedSegments;
    }

    public ArrayList<ArrayList<Point3D>> getRotatedCircles(){
        return rotatedCircles;
    }

    public Color getObjColor() {
        return objColor;
    }

    public Point3D getCentralPoint() {
        return centralPoint;
    }

    public double[][] getEulerAngle() {
        return eulerMatrix;
    }

    public BSpline getSpline() {
        return spline;
    }

    public void setColor(Color color) {
        this.objColor = color;
    }

    public void setUArray(ArrayList<Point2D> uarray) {
        this.uArray = uarray;
    }

    public ArrayList<Point2D> getUArray() {
        return uArray;
    }

    public ArrayList<Double> getPhiArray() {
        return phiArray;
    }

    public void setPhiArray(ArrayList<Double> phiArray) {
        this.phiArray = phiArray;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public void setA(double a){
        this.a = a;
    }

    public void setB(double b){
        this.b = b;
    }

    public void setC(double c){
        this.c = c;
    }

    public void setD(double d){
        this.d = d;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setLines(int k) {
        segments.clear();
        ArrayList<Point2D> sortedArray =  new ArrayList<>(uArray);
        sortedArray.sort(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return o1.getX() > o2.getX() ? 1 : -1;
            }
        });
        double offset = (sortedArray.get(sortedArray.size()-1).getX()-sortedArray.get(0).getX())/2+sortedArray.get(0).getX();
        for (int i = 0; i < phiArray.size(); i=i+k) {
            Double aPhiArray1 = phiArray.get(i);
            double[][] matrix = Matrix.getRotationMatrix(aPhiArray1);
            ArrayList<Point3D> points = new ArrayList<>();
            for (Point2D anUArray : uArray) {
                double[][] vector = new double[3][1];
                vector[0][0] = anUArray.getY();
                vector[1][0] = anUArray.getY();
                vector[2][0] = anUArray.getX();
                double[][] res = Matrix.mul(matrix, vector);
                Point3D point3D = new Point3D(res[0][0], res[1][0], res[2][0]-offset);
                points.add(point3D);
            }

            segments.add(points);
        }
        circles.clear();
        for (int i = 0; i < uArray.size(); i=i+k) {
            Point2D anUArray = uArray.get(i);
            ArrayList<Point3D> points = new ArrayList<>();
            double[][] vector = new double[3][1];
            vector[0][0] = anUArray.getY();
            vector[1][0] = anUArray.getY();
            vector[2][0] = anUArray.getX();
            for (Double aPhiArray : phiArray) {
                double[][] matrix = Matrix.getRotationMatrix(aPhiArray);
                double[][] res = Matrix.mul(matrix, vector);
                Point3D point3D = new Point3D(res[0][0], res[1][0], res[2][0]-offset);
                points.add(point3D);
            }
            circles.add(points);
        }
        rotateLines();
    }

    public ArrayList<ArrayList<Point3D>> getSegments(){
        return segments;
    }

    public ArrayList<ArrayList<Point3D>> getCircles(){
        return circles;
    }

    public void countPhi(double c, double d, int m, int k) {
        double step = (d-c)/(m*k);
        phiArray.clear();
        double start = c;
        for (int i = 0; i <= m*k; i++) {
            phiArray.add(start);
            start = start + step;
        }
    }

    public void buildSpline() {
        countPhi(c, d, m, k);
        setUArray(spline.getGridPixels(null,n, a,b,k,null));
        setLines(k);
    }

    private void rotateLines() {
        double maxX, maxY, maxZ;
        double minX, minY, minZ;
        maxX = maxY = maxZ = -1000000;
        minZ = minX = minY = 1000000;
        rotatedCircles.clear();
        rotatedSegments.clear();
        axis.clear();
        for (ArrayList<Point3D> point3Ds : segments) {
            ArrayList<Point3D> oneRotatedSegment = new ArrayList<>();
            for (Point3D point3D : point3Ds) {
                Point3D result = Matrix.mul(eulerMatrix, point3D);
                oneRotatedSegment.add(result);
                if (result.getX() > maxX) {
                    maxX = result.getX();
                }
                if (result.getY() > maxY) {
                    maxY = result.getY();
                }
                if (result.getZ() > maxZ) {
                    maxZ = result.getZ();
                }


                if (result.getX() < minX) {
                    minX = result.getX();
                }
                if (result.getY() < minY) {
                    minY = result.getY();
                }
                if (result.getZ() < minZ) {
                    minZ = result.getZ();
                }
            }
            rotatedSegments.add(oneRotatedSegment);
        }
        for (ArrayList<Point3D> point3Ds : circles) {
            ArrayList<Point3D> oneRotatedCircle = new ArrayList<>();
            for (Point3D point3D : point3Ds) {
                Point3D result = Matrix.mul(eulerMatrix, point3D);
                if (result.getX() > maxX) {
                    maxX = result.getX();
                }
                if (result.getY() > maxY) {
                    maxY = result.getY();
                }
                if (result.getZ() > maxZ) {
                    maxZ = result.getZ();
                }


                if (result.getX() < minX) {
                    minX = result.getX();
                }
                if (result.getY() < minY) {
                    minY = result.getY();
                }
                if (result.getZ() < minZ) {
                    minZ = result.getZ();
                }
                oneRotatedCircle.add(result);
            }
            rotatedCircles.add(oneRotatedCircle);
        }
        Point3D p1 = Matrix.mul(eulerMatrix, new Point3D(0, 0, 0));
        Point3D p2 = Matrix.mul(eulerMatrix, new Point3D(1, 0, 0));
        axis.add(new Edge3D(p1, p2));

        p1 = Matrix.mul(eulerMatrix, new Point3D(0, 0, 0));
        p2 = Matrix.mul(eulerMatrix, new Point3D(0, 1, 0));
        axis.add(new Edge3D(p1, p2));

        p1 = Matrix.mul(eulerMatrix, new Point3D(0, 0, 0));
        p2 = Matrix.mul(eulerMatrix, new Point3D(0, 0, 1));
        axis.add(new Edge3D(p1, p2));
        this.maxPoint = new Point3D(maxX, maxY, maxZ);
        this.minPoint = new Point3D(minX, minY, minZ);
    }

    public void setCX(double x){
        centralPoint = new Point3D(x, centralPoint.getY(), centralPoint.getZ());
        eulerMatrix[0][3] = centralPoint.getX();
    }

    public void setCY(double y){
        centralPoint = new Point3D(centralPoint.getX(), y, centralPoint.getZ());
        eulerMatrix[1][3] = centralPoint.getY();
    }

    public void setCZ(double z){
        centralPoint = new Point3D(centralPoint.getX(), centralPoint.getY(), z);
        eulerMatrix[2][3] = centralPoint.getZ();
    }

    public void changeMatrix(double ex, double ey, double ez) {
        double[][] matrixX = Matrix.createRxMatrix(ex);
        double[][] matrixY = Matrix.createRyMatrix(ey);
        eulerMatrix = Matrix.mul(matrixY,Matrix.mul(matrixX, eulerMatrix));
        eulerMatrix[0][3] = centralPoint.getX();
        eulerMatrix[1][3] = centralPoint.getY();
        eulerMatrix[2][3] = centralPoint.getZ();
        rotateLines();
    }

    public void setParams(int n, int m, int k, double a, double b, double c, double d) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Point3D getMaxPoint() {
        return maxPoint;
    }

    public Point3D getMinPoint() {return minPoint;}

}
