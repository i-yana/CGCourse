package ru.nsu.fit.g13205.zharkova.raytracing.model;


import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

import java.awt.*;

/**
 * Created by Yana on 14.05.16.
 */
public class RenderSetting {

    public static final int MAX_DEPTH = 5;
    private Color bg;
    private double gamma;
    private int depth;
    private String quality;
    private Camera camera;
    private Wireframe wireframe;

    public RenderSetting(Color bg, double gamma, int depth, String quality, Point3D eye, Point3D view, Point3D up,
                         double zn, double zf, double sw, double sh) {

        this.bg = bg;
        this.gamma = gamma;
        this.depth = depth;
        this.quality = quality;
        this.camera = new Camera(eye, view, up);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if((dimension.height-100)<sh*100){
            sh = (dimension.height-100)/100d;
        }
        if(dimension.width<sw*100){
            sw = (dimension.width-10)/100d;
        }
        this.wireframe = new Wireframe(zn, zf, sw, sh);
    }

    public RenderSetting() {
        this.bg = new Color(0,0,0);
        this.gamma = 1;
        this.depth = 2;
        this.camera = new Camera(new Point3D(-20,0,0), new Point3D(0,0,0), new Point3D(0,0,1));
        this.wireframe = new Wireframe(8,30,5,5);
        this.quality = "NORMAL";
    }

    public Color getColor() {
        return bg;
    }

    public double getGamma() {
        return gamma;
    }

    public int getDepth() {
        return depth;
    }

    public Camera getCamera() {
        return camera;
    }

    public String getQuality() {
        return quality;
    }

    public Wireframe getWireframe() {
        return wireframe;
    }

    public void setMaxDepth(){
        this.depth = MAX_DEPTH;
    }

    public void setColor(Color bg) {
        this.bg = bg;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setZN(double ZN) {
        this.wireframe.setZN(ZN);
    }

    public void setZF(double ZF) {
        this.wireframe.setZF(ZF);
    }

    public void setQuality(String quality) {
        this.quality = quality.toUpperCase();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public float getGreen() {
        return bg.getGreen()/255f;
    }

    public float getBlue() {
        return bg.getBlue()/255f;
    }

    public float getRed() {
        return bg.getRed()/255f;
    }
}
