package ru.nsu.fit.g13205.zharkova;

import ru.nsu.fit.g13205.zharkova.parser.Lexer;
import ru.nsu.fit.g13205.zharkova.parser.Parser;
import ru.nsu.fit.g13205.zharkova.parser.ParserException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 22.03.16.
 */
public class MainFunction implements Function{

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
    private String function;
    private Lexer lexer;

    public MainFunction(String function, Boundary b, int u0, int v0, int u1, int v1, int colors) throws IOException {
        this.function = function;
        try {
            lexer = new Lexer(function);
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
        setPixelBorder(u0, v0, u1, v1);
        setCordsBorder(b);
        countExtrems();
        levels = Tools.buildColorLevels(colors, getMin(), getMaxY());
    }

    private void countExtrems() throws IOException {
        double min = getZ(getCord(u0,v0));
        double max = getZ(getCord(u0,v0));
        for (int i = v0; i < v1 ; i++) {
            for (int j = u0; j < u1; j++) {
                double z = getZ(getCord(j,i));
                if(z > max){
                    max = z;
                }
                if(z < min){
                    min = z;
                }
            }
        }
        this.max = max;
        this.min = min;
    }

    public void setPixelBorder(int u0, int v0, int u1, int v1){
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }

    public void setCordsBorder(Boundary b) throws IOException {
        this.a = b.getA();
        this.b = b.getB();
        this.c = b.getC();
        this.d = b.getD();
        countExtrems();
    }

    public Cord getCord(int u, int v){
        //v = v1-v;
        double x =(b-a)*(u-u0)/(u1-u0)+a;
        double y =(d-c)*(v1-v)/(v1-v0)+c;
        return new Cord(x,y);
    }

    public Point getPixel(double x, double y){
        int u = (int)((u1-u0)*(x-a)/(b-a)+u0+0.5);
        int v = v1 -(int)((v1-v0)*(y-c)/(d-c)+v0+0.5);
        //v = v1 - v;
        return new Point(u,v);
    }

    public double getZ(Cord funcCord) throws IOException {
        double x = funcCord.x;
        double y = funcCord.y;
        Parser parser = new Parser(lexer, x, y);
        try {
            return parser.parse();
        } catch (ParserException e) {
            throw new IOException(e.getMessage());
        }
        //return (1-x)*(1-x)+100*(y-x*x)*(y-x*x);
        //return Math.sin(x*x*y);
        //return x*x-y*y;
    }

    public double getMin() {
        return min;
    }

    public double getMaxY(){
        return max;
    }

    public ArrayList<Double> getLevels() {
        return levels;
    }

    public void createGrid(int k, int m) {
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

    public Cord[][] getFunctionGrid(){
        return grid;
    }

    @Override
    public Point[][] getPixelGrid() {
        return pixelGrid;
    }
}
