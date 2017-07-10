package ru.nsu.fit.g13205.zharkova.wf.model;

import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Yana on 09.04.16.
 */
public class BSpline{

    private ArrayList<Point2D> points;
   // private ArrayList<Point> splinePoints;
    private ArrayList<Point2D> addedPoints;
    private float length;

    private static final double[][] M = {
            {
                    -1./6,3./6,-3./6,1./6
            },
            {
                    3./6,-6./6,3./6,0./6
            },
            {
                    -3./6,0./6,3./6,0./6
            },
            {
                    1./6,4./6,1./6,0./6
            }
    };

    public BSpline(ArrayList<Point2D> points) {
        this.points = new ArrayList<>(points);
        this.addedPoints = new ArrayList<>();
        setAddedPoints();
    }

    public void setAddedPoints(){
        addedPoints.clear();
        for (int i = 0; i < points.size(); i++) {
            if(i+1<points.size()) {
                Point2D first = points.get(i);
                Point2D second = points.get(i + 1);
                double middleX = Math.abs(first.getX() - second.getX()) / 2 + Math.min(first.getX(), second.getX());
                double middleY = Math.abs(first.getY() - second.getY()) / 2 + Math.min(first.getY(), second.getY());
                addedPoints.add(new Point2D(middleX, middleY));
            }
        }
    }

    public void countLength(){
        double[][] Gx = new double[4][1];
        double[][] Gy = new double[4][1];
        double[][] T1 = new double[1][4];
        double[][] T2 = new double[1][4];
        this.length = 0;
        double delta = 0.001;
        for (int i = 1; i < points.size()-2; i++) {
            Gx[0][0] = points.get(i-1).getX();
            Gx[1][0] = points.get(i).getX();
            Gx[2][0] = points.get(i+1).getX();
            Gx[3][0] = points.get(i+2).getX();
            Gy[0][0] = points.get(i-1).getY();
            Gy[1][0] = points.get(i).getY();
            Gy[2][0] = points.get(i+1).getY();
            Gy[3][0] = points.get(i+2).getY();
            double t = 0;
            while (t+delta<=1) {
                T1[0][0] = t*t*t;
                T1[0][1] = t*t;
                T1[0][2] = t;
                T1[0][3] = 1;
                T2[0][0] = (t+delta)*(t+delta)*(t+delta);
                T2[0][1] = (t+delta)*(t+delta);
                T2[0][2] = (t+delta);
                T2[0][3] = 1;

                double[][] res = mul(mul(T1,M),Gx);
                double x1 = res[0][0];
                res = mul(mul(T2,M),Gx);
                double x2 = res[0][0];
                res = mul(mul(T1,M),Gy);
                double f1 = res[0][0];
                res = mul(mul(T2,M),Gy);
                double f2 = res[0][0];
                length += Math.sqrt((x1-x2)*(x1-x2)+(f1-f2)*(f1-f2));
                t=t+delta;
            }
        }
    }


    public ArrayList<Point2D> getGridPixels(Graphics g, int n, double a, double b, int k, Function f){
        countLength();
        double aLn = length*a;
        double bLn = length*b;
        double step = (bLn-aLn)/(n*k);
        ArrayList<Point2D> gridPoint = new ArrayList<>();
        double[][] Gx = new double[4][1];
        double[][] Gy = new double[4][1];
        double[][] T1 = new double[1][4];
        double[][] T2 = new double[1][4];
        double ln = 0;
        double delta = 0.001;
        double start = aLn;
        for (int i = 1; i < points.size()-2; i++) {
            Gx[0][0] = points.get(i-1).getX();
            Gx[1][0] = points.get(i).getX();
            Gx[2][0] = points.get(i+1).getX();
            Gx[3][0] = points.get(i+2).getX();
            Gy[0][0] = points.get(i-1).getY();
            Gy[1][0] = points.get(i).getY();
            Gy[2][0] = points.get(i+1).getY();
            Gy[3][0] = points.get(i+2).getY();
            double t = 0;
            while (t+delta<=1) {
                T1[0][0] = t*t*t;
                T1[0][1] = t*t;
                T1[0][2] = t;
                T1[0][3] = 1;
                T2[0][0] = (t+delta)*(t+delta)*(t+delta);
                T2[0][1] = (t+delta)*(t+delta);
                T2[0][2] = (t+delta);
                T2[0][3] = 1;

                double[][] res = mul(mul(T1,M),Gx);
                double x1 = res[0][0];
                res = mul(mul(T2,M),Gx);
                double x2 = res[0][0];
                res = mul(mul(T1,M),Gy);
                double f1 = res[0][0];
                res = mul(mul(T2,M),Gy);
                double f2 = res[0][0];
                ln += Math.sqrt((x1-x2)*(x1-x2)+(f1-f2)*(f1-f2));
                if(ln>=start && ln<bLn){
                    gridPoint.add(new Point2D(x2, f2));
                    start = start+step;
                }
                if(bLn-ln<=0.01){
                    gridPoint.add(new Point2D(x2, f2));
                    return gridPoint;
                }
                if(ln>=aLn && ln <=bLn && f != null && g != null) {
                    Point point = f.getPixel(x2, f2);
                    g.drawLine(point.x, point.y, point.x, point.y);
                }
                t=t+delta;
            }
        }
        return gridPoint;
    }

    private static double[][] mul(double[][] mA, double[][] mB) {
        int m = mA.length;
        int n = mB[0].length;
        int o = mB.length;
        double[][] res = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; k++) {
                    res[i][j] += mA[i][k] * mB[k][j];
                }
            }
        }
        return res;
    }

    public double getMax() {
        double max = Math.abs(points.get(0).getX());
        for (Point2D point: points) {
            if (Math.abs(point.getX()) > max) {
                max = Math.abs(point.getX());
            }
            if (Math.abs(point.getY() * 1.5) > max) {
                max = Math.abs(point.getY()) * 1.5;
            }
        }
        return max;
    }


    public void drawSupportPoints(BufferedImage interactiveLayer, Function f) {
        Graphics gr = interactiveLayer.getGraphics();
        gr.setColor(Color.WHITE);
        for(int i = 0; i< points.size();i++){
            Point point = f.getPixel(points.get(i).getX(), points.get(i).getY());
            gr.drawOval(point.x - 5, point.y - 5, 10, 10);
            if(i+1< points.size()){
                Point2D first = points.get(i);
                Point2D second = points.get(i+1);
                Point2D addedPoint = addedPoints.get(i);
                Point pixFirst = f.getPixel(first.getX(), first.getY());
                Point pixSecond = f.getPixel(second.getX(), second.getY());
                Point pixAdded = f.getPixel(addedPoint.getX(), addedPoint.getY());
                gr.drawLine(pixFirst.x, pixFirst.y, pixAdded.x, pixAdded.y);
                gr.drawLine(pixAdded.x, pixAdded.y, pixSecond.x, pixSecond.y);
                gr.drawOval(pixAdded.x - 3, pixAdded.y - 3, 6, 6);
            }
        }
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public ArrayList<Point2D> getSupportPoints() {
        return addedPoints;
    }

    public void replacePoint(Point2D dragPoint, Point2D point) {
        int index = points.indexOf(dragPoint);
        points.set(index, point);
        try {
            addedPoints.set(index - 1, getMiddlePoint(index - 1, index));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try {
            addedPoints.set(index, getMiddlePoint(index, index + 1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }

    private Point2D getMiddlePoint(int a, int b) throws ArrayIndexOutOfBoundsException{
        try {
            Point2D first = points.get(a);
            Point2D second = points.get(b);
            double middleX = Math.abs(first.getX() - second.getX()) / 2 + Math.min(first.getX(), second.getX());
            double middleY = Math.abs(first.getY() - second.getY()) / 2 + Math.min(first.getY(), second.getY());
            return new Point2D(middleX, middleY);
        }catch (IndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void replaceSupportPoint(Point2D dragPoint, Point2D point) {
        addedPoints.set(addedPoints.indexOf(dragPoint), point);
    }

    public void removePoint(Point2D dragPoint) {
        points.remove(points.indexOf(dragPoint));
        recountMiddlePoints();
    }

    private void recountMiddlePoints() {
        addedPoints.clear();
        for (int i = 0; i < points.size(); i++) {
            if(i+1<points.size()){
                addedPoints.add(getMiddlePoint(i, i + 1));
            }
        }
    }

    public void addPoint(Point2D dragPoint) {
        int index = addedPoints.indexOf(dragPoint);
        points.add(index+1, dragPoint);
        recountMiddlePoints();
    }
}
