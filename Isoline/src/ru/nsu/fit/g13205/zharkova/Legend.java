package ru.nsu.fit.g13205.zharkova;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 23.03.16.
 */
public class Legend implements Function{
    /*private double maxY;
    private double maxX;
    private ArrayList<Double> levels;
    private int colorsNum;
    private Cord[][] grid;
    private Point[][] pixelGrid;
    private int k;*/

    private double a;
    private double b;
    private double c;
    private double d;
    private int u0;
    private int v0;
    private int u1;
    private int v1;
    private double max;
    private double min;

    private ArrayList<Double> levels;
    private Cord[][] grid;
    private Point[][] pixelGrid;
    private int k;
    private int m;

    public Legend(double a, double b, double c, double d, int u0, int v0, int u1, int v1, int colors){
        setPixelBorder(u0, v0, u1, v1);
        setCordsBorder(a, b, c, d);
        this.min = c;
        this.max = d;
        levels = Tools.buildColorLevels(colors, getMin(), getMaxY());
        this.k = 2;
        this.m = colors-1;
    }

    public void setPixelBorder(int u0, int v0, int u1, int v1){
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }

    public void setCordsBorder(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Cord getCord(int u, int v){
        //v = v1 - v;
        double x =(b-a)*(u-u0)/(u1-u0)+a;
        double y =(d-c)*(v1-v)/(v1-v0)+c;
        return new Cord(x,y);
    }

    public Point getPixel(double x, double y){
        int u = (int)((u1-u0)*(x-a)/(b-a)+u0+0.5);
        int v = v1-(int)((v1-v0)*(y-c)/(d-c)+v0+0.5);
        //v = v1 - v;
        return new Point(u,v);
    }

    @Override
    public double getZ(Cord funcCord) {
        return funcCord.y;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMaxY() {
        return max;
    }

    @Override
    public ArrayList<Double> getLevels() {
        return levels;
    }

    @Override
    public Cord[][] getFunctionGrid() {
        if(grid == null){
            createGrid();
        }
        return grid;
    }

    @Override
    public Point[][] getPixelGrid() {
        return pixelGrid;
    }

    public void createGrid() {
        pixelGrid = new Point[m][k];
        grid = new Cord[m][k];
        double deltaX = (b-a)/(k-1);
        double deltaY = (d-c)/(m-1);
        double cordX = a;
        double cordY = c;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                grid[i][j] = new Cord(cordX, cordY);
                pixelGrid[i][j] = getPixel(cordX, cordY);
                cordX+=deltaX;
            }
            cordY+=deltaY;
            cordX = a;
        }
    }

    public int getHeight() {
        return m;
    }



}
