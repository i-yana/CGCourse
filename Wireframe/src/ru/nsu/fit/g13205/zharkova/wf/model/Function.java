package ru.nsu.fit.g13205.zharkova.wf.model;

import javafx.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 12.04.16.
 */
public class Function {

    private double a;
    private double b;
    private double c;
    private double d;
    private int u0;
    private int v0;
    private int u1;
    private int v1;

    public Function(double a, double b,double c, double d, int u0, int v0, int u1, int v1){
        setPixelBorder(u0, v0, u1, v1);
        setCordsBorder(a,b,c,d);
    }

    public double getMaxBound(){
        return Math.max(b,d);
    }

    public void setPixelBorder(int u0, int v0, int u1, int v1){
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }

    public void setCordsBorder(double a, double b,double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Point2D getCord(int u, int v){
        double x =(b-a)*(u-u0)/(u1-u0)+a;
        double y =(d-c)*(v1-v)/(v1-v0)+c;
        return new Point2D(x,y);
    }

    public Point getPixel(double x, double y){
        int u = (int)((u1-u0)*(x-a)/(b-a)+u0+0.5);
        int v = v1-(int)((v1-v0)*(y-c)/(d-c)+v0+0.5);
        return new Point(u,v);
    }

    public ArrayList<Point2D> toPoint2D(ArrayList<Point> pixels) {
        ArrayList<Point2D> point2Ds = new ArrayList<>();
        for (Point p: pixels){
            point2Ds.add(getCord(p.x, p.y));
        }
        return point2Ds;
    }

    public ArrayList<Point> toPoint(ArrayList<Point2D> point2Ds) {
        ArrayList<Point> points = new ArrayList<>();
        for (Point2D p: point2Ds){
            points.add(getPixel(p.getX(), p.getY()));
        }
        return points;
    }

    public Point getPixel(Point2D point2D) {
        double x = point2D.getX();
        double y = point2D.getY();
        int u = (int)((u1-u0)*(x-a)/(b-a)+u0+0.5);
        int v = v1-(int)((v1-v0)*(y-c)/(d-c)+v0+0.5);
        return new Point(u,v);
    }
}
