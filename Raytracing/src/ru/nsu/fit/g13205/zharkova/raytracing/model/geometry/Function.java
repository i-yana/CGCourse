package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;

import java.awt.*;

/**
 * Created by Yana on 14.05.16.
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

    public Point getPixel(double x, double y){
        int u = (int)((u1-u0)*(x-a)/(b-a)+u0+0.5);
        int v = v1-(int)((v1-v0)*(y-c)/(d-c)+v0+0.5);
        return new Point(u,v);
    }
}
