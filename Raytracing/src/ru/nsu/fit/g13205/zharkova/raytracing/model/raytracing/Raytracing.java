package ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing;

import ru.nsu.fit.g13205.zharkova.raytracing.model.*;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces.Surface;
import ru.nsu.fit.g13205.zharkova.raytracing.view.Frame;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 15.05.16.
 */
public class Raytracing {

    private static final Intensive NULL_INTENSIVE = new Intensive(0f,0f,0f);
    private static final int P_NUM = Runtime.getRuntime().availableProcessors();
    private Intensive bg;
    private volatile Thread renderThread;
    private Dimension dimension;
    private Frame.WorldWaiter worldWaiter;
    private RenderSetting renderSetting;
    private Scene scene;
    private ArrayList<Surface> surfaces;
    private Camera camera;
    private Wireframe w;
    private float[][] red, green, blue;

    public Raytracing(Dimension dimension, RenderSetting renderSetting, Scene scene, double[][] rotate, Frame.WorldWaiter worldWaiter) {
        this.worldWaiter = worldWaiter;
        this.dimension = dimension;
        this.renderSetting = renderSetting;
        this.scene = scene;
        this.surfaces = scene.getSurfaces();
        this.camera = renderSetting.getCamera().rotateCamera(rotate);
        this.w = renderSetting.getWireframe();
        this.bg = new Intensive(renderSetting.getRed(), renderSetting.getGreen(), renderSetting.getBlue());
        this.red = new float[dimension.height][dimension.width];
        this.green = new float[dimension.height][dimension.width];
        this.blue = new float[dimension.height][dimension.width];
    }

    public void doWork() {
        if (renderThread != null)
            throw new IllegalStateException("Cannot render two images at the same time!");

        renderThread = new Thread(() -> {
            worldWaiter.traceStarted();
            render();
            worldWaiter.traceFinished(red, green, blue);
            renderThread = null;
        }, "Raytracer Render");
        renderThread.setDaemon(true);
        renderThread.start();
    }

    private Point3D[][] convert(double ratio, int width, int height){

        Point3D[][] camMatrix = new Point3D[(int) (width*ratio)][(int) (height*ratio)];
        Point3D newView = camera.getView();
        Point3D newEye = camera.getEye();
        Point3D newUP = camera.getUp();

        double deltaW = w.getSw() / (int)(width*ratio);
        double deltaH = w.getSh() / (int)(height*ratio);
        Point3D direction = newView.sub(newEye);
        Point3D right = direction.cross(newUP);

        direction = direction.normalize();
        right = right.normalize();

        Point3D start = newEye.add(direction.mul(w.getZn()));
        Point3D tmp = newUP.mul(w.getSh()/2d).add(right.mul(-w.getSw()/ 2d));

        start = start.add(tmp);

        for (int x = 0; x < (int)(width*ratio); x++) {
            for (int y = 0; y < (int)(height*ratio); y++) {
                Point3D yV = newUP.mul(-deltaH * y);
                Point3D xV = right.mul(deltaW * x);
                Point3D totalShift = yV.add(xV);
                camMatrix[x][y] = start.add(totalShift).sub(newEye);
            }
        }

        return camMatrix;
    }

    class ProcessNormal implements Runnable{

        private int proc;
        private Point3D[][] camMatrix;

        ProcessNormal(int proc, Point3D[][] camMatrix){
            this.proc = proc;
            this.camMatrix = camMatrix;
        }
        @Override
        public void run() {
            for (int i = 0; i < dimension.width; i++) {
                for (int j = proc; j< dimension.height; j=j+P_NUM) {
                    Ray ray = new Ray(camera.getEye(), camMatrix[i][j]);
                    Intensive intensive = check(trace(ray, 1));
                    setColor(intensive,j, i);
                    worldWaiter.pixelTraced();
                }
            }
        }
    }

    class ProcessFine implements Runnable{

        private int proc;
        private Point3D[][] camMatrix;

        ProcessFine(int proc, Point3D[][] camMatrix){
            this.proc = proc;
            this.camMatrix = camMatrix;
        }
        @Override
        public void run() {
            for (int i = proc, x =proc*2; i < dimension.width && x<dimension.width*2; i=i+P_NUM, x=x+2*P_NUM) {
                for (int j = 0, y = 0; j < dimension.height && y<dimension.height*2; j++, y=y+2) {
                    Ray ray1 = new Ray(camera.getEye(), camMatrix[x][y]);
                    Intensive intensive1 = check(trace(ray1, 1));
                    Ray ray2 = new Ray(camera.getEye(), camMatrix[x+1][y]);
                    Intensive intensive2 = check(trace(ray2, 1));
                    Ray ray3 = new Ray(camera.getEye(), camMatrix[x][y+1]);
                    Intensive intensive3 = check(trace(ray3, 1));
                    Ray ray4 = new Ray(camera.getEye(), camMatrix[x+1][y+1]);
                    Intensive intensive4 = check(trace(ray4, 1));

                    float mediumR = (intensive1.red+intensive2.red+intensive3.red+intensive4.red)/4f;
                    float mediumG = (intensive1.green+intensive2.green+intensive3.green+intensive4.green)/4f;
                    float mediumB = (intensive1.blue+intensive2.blue+intensive3.blue+intensive4.blue)/4f;
                    Intensive intensive = new Intensive(mediumR, mediumG, mediumB);
                    setColor(intensive,j,i);
                    worldWaiter.pixelTraced();
                }
            }
        }
    }


    private void render() {
        String quality = renderSetting.getQuality();
        Point3D[][] camMatrix;
        ArrayList<Thread> threads = new ArrayList<>();
        switch (quality){
            case "FINE":
                camMatrix = convert(2, dimension.width, dimension.height);
                for (int i = 0; i < P_NUM; i++) {
                    Thread t = new Thread(new ProcessFine(i, camMatrix));
                    threads.add(t);
                    t.start();
                }
                for (Thread thread: threads){
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "NORMAL":
                camMatrix = convert(1, dimension.width, dimension.height);
                for (int i = 0; i < P_NUM; i++) {
                    Thread t = new Thread(new ProcessNormal(i, camMatrix));
                    threads.add(t);
                    t.start();
                }
                for (Thread thread: threads){
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "ROUGH":
                camMatrix = convert(1d/2, dimension.width, dimension.height);
                for (int i = 0, x =0; i < dimension.width && x<dimension.width/2; i=i+2, x++) {
                    for (int j = 0, y=0; j < dimension.height && y<dimension.height/2; j=j+2, y++) {
                        if(i>=dimension.width || j>=dimension.height){
                            continue;
                        }
                        Ray ray = new Ray(camera.getEye(), camMatrix[x][y]);
                        Intensive intensive = check(trace(ray, 1));
                        setColor(intensive,j,i);
                        worldWaiter.pixelTraced();
                        setColor(intensive,j,i+1);
                        worldWaiter.pixelTraced();
                        setColor(intensive,j+1,i);
                        worldWaiter.pixelTraced();
                        setColor(intensive,j+1,i+1);
                        worldWaiter.pixelTraced();
                    }
                }
                break;
        }
        normColor();
    }

    private Intensive check(Intensive intensive) {
        if(intensive.equals(NULL_INTENSIVE)){
            return bg;
        }
        else {
            return intensive;
        }
    }

    private void normColor() {
        float max = 0;
        for (int i = 0; i <red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                if(red[i][j]>max){
                    max = red[i][j];
                }
                if(green[i][j]>max){
                    max = green[i][j];
                }
                if(blue[i][j]>max){
                    max = blue[i][j];
                }
            }
        }
        double gamma = 1d/renderSetting.getGamma();
        for (int i = 0; i <red.length; i++) {
            for (int j = 0; j < red[i].length; j++) {
                red[i][j] = (float) Math.pow(red[i][j]/max, gamma);
                green[i][j] = (float) Math.pow(green[i][j]/max, gamma);
                blue[i][j] = (float) Math.pow(blue[i][j]/max, gamma);
            }
        }
    }

    private void setColor(Intensive intensive, int i, int j) {
        if(i>=red.length || j>=red[0].length){
            return;
        }
        red[i][j] = intensive.red;
        green[i][j] = intensive.green;
        blue[i][j] = intensive.blue;
    }

    private Intensive trace(Ray ray, int depth) {
        double minDistance = Double.POSITIVE_INFINITY;
        Surface intersectedSurface = null;
        Point3D hit = null;
        Intensive reflectColor = new Intensive(0,0,0);
        Intensive shadeColor = new Intensive(0,0,0);
        for (Surface s: surfaces){
            Intersection intersection = new Intersection(s, ray);
            if (intersection.intersect()) {
                Point3D shapeHit = intersection.getHit();
                if(Math.abs(ray.getOrigin().x-shapeHit.x)<0.0000001 && Math.abs(ray.getOrigin().y-shapeHit.y)<0.0000001  && Math.abs(ray.getOrigin().z-shapeHit.z)<0.0000001){
                    continue;
                }
                double distance = distance(ray.getOrigin(), shapeHit);
                if (distance < minDistance) {
                    hit = shapeHit;
                    intersectedSurface = s;
                    minDistance = distance;
                }
            }
        }
        if(intersectedSurface!=null) {
            for (Light light : scene.getIlluminates()) {
                Intensive tmp = shade(light, hit, intersectedSurface);
                shadeColor = shadeColor.add(tmp);
            }
            if(depth < renderSetting.getDepth()) {
                Point3D normal = intersectedSurface.getNormal(hit);
                reflectColor = trace(getReflectedRay(ray, normal, hit), depth + 1);
            }
            return scene.getAmbientLight().mul(intersectedSurface.getKd()).add(shadeColor.add(reflectColor.mul(intersectedSurface.getKs())));
        }
        return new Intensive(0,0,0);
    }

    private Intensive shade(Light light, Point3D hit, Surface surface) {
        Ray lightRay =new Ray(light.getPosition(), hit.sub(light.getPosition()));
        Point3D normal = surface.getNormal(hit).normalize();
        if(normal.scalar(lightRay.getDirection())>=0){
            return new Intensive(0,0,0);
        }
        double maxDist = distance(light.getPosition(), hit);
        for (Surface s: scene.getSurfaces()){
            Intersection intersection = new Intersection(s, lightRay);
            if(intersection.intersect()){
                Point3D newHit = intersection.getHit();
                if(Math.abs(newHit.x-hit.x)<0.0000001 && Math.abs(newHit.y-hit.y)<0.0000001  && Math.abs(newHit.z-hit.z)<0.0000001){
                    continue;
                }
                double distance = distance(newHit, light.getPosition());
                if(distance<maxDist){
                    return new Intensive(0,0,0);
                }
            }
        }


        Point3D L = light.getPosition().sub(hit).normalize();
        Point3D E = camera.getEye().sub(hit).normalize();

        Point3D H = L.add(E).normalize();
        float Hkoeff = (float) Math.pow(normal.scalar(H), surface.getPower());
        float Lkoeff = (float) normal.scalar(L);
        double d = distance(light.getPosition(), hit);
        float fatt = (float) (1d/(1d+d));

        Intensive sum = surface.getKd().mul(Lkoeff).add(surface.getKs().mul(Hkoeff));
        return light.getIntensive().mul(fatt).mul(sum);
    }

    private Ray getReflectedRay(Ray ray, Point3D normal, Point3D hit) {
        Ray rayInvert = new Ray(hit, ray.getOrigin().sub(hit));
        double a = -1d;
        double b = 2d*normal.scalar(rayInvert.getDirection());
        Point3D ref = normal.mul(b).add(rayInvert.getDirection().mul(a));
        return new Ray(hit, ref);
    }


    private double distance(Point3D eye, Point3D hit) {
        Point3D sub = hit.sub(eye);
        return Math.sqrt(sub.x*sub.x+sub.y*sub.y+sub.z*sub.z);
    }
}
