package ru.nsu.fit.g13205.zharkova;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 23.03.16.
 */
public class Properties {

    private int k;
    private int m;
    private int level;
    private ArrayList<Color> colorMap;
    private Color isoColor;
    private Boundary boundary;
    private String function;

    public static final int DEFAULT_K = 50;
    public static final int DEFAULT_M = 50;
    public static final int DEFAULT_LEVELS = 15;
    public static final ArrayList<Color> DEFAULT_COLORS = new ArrayList<>();
    public static final Color DEFAULT_ISOLINE_COLOR = Color.BLACK;
    public static final String DEFAULT_FUNCTION = "x*x-y*y";
    public static final Properties DEFAULT_PROPERTIES = new Properties(DEFAULT_FUNCTION, DEFAULT_K, DEFAULT_M, DEFAULT_LEVELS, DEFAULT_COLORS, DEFAULT_ISOLINE_COLOR, new Boundary());

    static {
        DEFAULT_COLORS.add(0, new Color(0, 100, 0));
        DEFAULT_COLORS.add(0, new Color(0, 128, 0));
        DEFAULT_COLORS.add(0, new Color(34, 139, 34));
        DEFAULT_COLORS.add(0, new Color(50, 205, 50));
        DEFAULT_COLORS.add(0, new Color(0, 255, 0));
        DEFAULT_COLORS.add(0, new Color(124, 252, 0));
        DEFAULT_COLORS.add(0, new Color(127, 255, 0));
        DEFAULT_COLORS.add(0, new Color(173, 255, 47));
        DEFAULT_COLORS.add(0, new Color(127, 255, 212));
        DEFAULT_COLORS.add(0, new Color(64, 224, 208));
        DEFAULT_COLORS.add(0, new Color(0 ,206, 209));
        DEFAULT_COLORS.add(0, new Color(30, 144, 255));
        DEFAULT_COLORS.add(0, new Color(0, 0, 255));
        DEFAULT_COLORS.add(0, new Color(0, 0, 205));
        DEFAULT_COLORS.add(0, new Color(0, 0, 139));
    }

    public Properties(String function, int k, int m, int level, ArrayList<Color> colorMap, Color isoColor, Boundary boundary){
        this.k = k;
        this.m = m;
        this.level = level;
        this.colorMap = colorMap;
        this.isoColor = isoColor;
        this.boundary = boundary;
        this.function = function;
    }

    public Properties(int k, int m, int level, ArrayList<Color> colorMap, Color isoColor, Boundary boundary){
        this.k = k;
        this.m = m;
        this.level = level;
        this.colorMap = colorMap;
        this.isoColor = isoColor;
        this.boundary = boundary;
        this.function = DEFAULT_FUNCTION;
    }

    public Properties(Properties properties) {
        this.k = properties.getK();
        this.m = properties.getM();
        this.level = properties.getLevel();
        this.colorMap = new ArrayList<>(properties.getColors());
        this.isoColor = properties.getIsoColor();
        this.boundary = new Boundary(properties.getBoundary());
        this.function = properties.getFunction();
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Color> getColors() {
        return colorMap;
    }

    public Color getIsoColor() {
        return isoColor;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public void setKM(int k, int m) {
        this.k = k;
        this.m = m;
    }

    public String getFunction() {
        return function;
    }

    public void setIsolineColor(Color color) {
        this.isoColor = color;
    }
}
