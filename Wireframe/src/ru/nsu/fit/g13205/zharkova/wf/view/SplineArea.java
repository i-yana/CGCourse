package ru.nsu.fit.g13205.zharkova.wf.view;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.wf.model.BSpline;
import ru.nsu.fit.g13205.zharkova.wf.model.Function;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Properties;
import ru.nsu.fit.g13205.zharkova.wf.model.RotationObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Yana on 08.04.16.
 */
public class SplineArea extends JPanel {
    private Properties properties;
    private BufferedImage image = new BufferedImage(525,350,BufferedImage.TYPE_INT_ARGB);
    private BufferedImage axis = new BufferedImage(525,350, BufferedImage.TYPE_INT_RGB);
    private BufferedImage interactiveLayer = new BufferedImage(525,350, BufferedImage.TYPE_INT_ARGB);
    private Function f;
    private boolean isInteractiveLayer = true;
    private MouseHandler mouseHandler;
    private boolean autoScale = true;
    private View3D view3D;
    private RotationObject rotationObject;
    private BSpline spline;
    private double currentMax = 0;
    //private ArrayList<Point> splinePoints;

    public SplineArea(Properties properties, View3D view3D){
        super();
        this.properties = properties;
        this.view3D = view3D;
        mouseHandler = new MouseHandler();
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
        setPreferredSize(new Dimension(525, 350));
        setMaximumSize(new Dimension(525, 350));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(axis, 0, 0,null);
        g.drawImage(image, 0, 0,null);
        if(isInteractiveLayer) {
            g.drawImage(interactiveLayer, 0, 0, null);
        }
    }


    public void createArea(int objectNumber) {
        try {
            this.rotationObject = properties.getRotationObjects().get(objectNumber);
        }catch (IndexOutOfBoundsException e){
            clearImage(image);
            clearImage(interactiveLayer);
            repaint();
            view3D.paintObjects();
            spline = null;
            return;
        }
        this.spline = rotationObject.getSpline();
        createFunction(spline.getMax());
        buildSpline();
        repaint();
    }


    public void clearImage(BufferedImage image){
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB(j,i,new Color(0,0,0,0).getRGB());
            }
        }
    }

    private void createFunction(double max){
        currentMax = max;
        f = new Function(-max*1.5, max*1.5, -max, max, 0,0, image.getWidth(),image.getHeight());
        paintAxis();
    }

    private void paintAxis(){
        clearImage(axis);
        Graphics g = axis.getGraphics();
        g.setColor(Color.darkGray);
        g.drawLine(axis.getWidth()/2, 0, axis.getWidth()/2, image.getHeight());
        g.drawLine(0, axis.getHeight()/2, axis.getWidth(), image.getHeight()/2);
        int step = f.getPixel(1,0).x-axis.getWidth()/2;
        int i = axis.getWidth()/2;
        while (i<axis.getWidth()){
            g.drawLine(i, image.getHeight()/2-3, i, image.getHeight()/2+3);
            i = i+step;
        }
        i = axis.getWidth()/2;
        while (i>=0){
            g.drawLine(i, image.getHeight()/2-3, i, image.getHeight()/2+3);
            i = i-step;
        }
        i = axis.getHeight()/2;
        while (i<axis.getHeight()){
            g.drawLine(image.getWidth()/2-3, i, image.getWidth()/2+3, i);
            i = i+step;
        }
        i = axis.getHeight()/2;
        while (i>=0){
            g.drawLine(image.getWidth()/2-3, i, image.getWidth()/2+3, i);
            i = i-step;
        }
    }

    private void buildSpline() {
        clearImage(image);
        clearImage(interactiveLayer);
        if(spline == null){
            return;
        }
        spline.drawSupportPoints(interactiveLayer, f);
        Graphics gr = image.getGraphics();
        gr.setColor(rotationObject.getObjColor());
        ArrayList<Point2D> points = spline.getGridPixels(gr, rotationObject.getN(), rotationObject.getA(), rotationObject.getB(), rotationObject.getK(),f);
        rotationObject.setUArray(points);
        rotationObject.countPhi(rotationObject.getC(), rotationObject.getD(), rotationObject.getM(), rotationObject.getK());
        rotationObject.setLines(rotationObject.getK());
        view3D.createBox();
        view3D.paintObjects();
    }




    public void showLines(boolean mode) {
        if(mode){
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }
        else {
            removeMouseListener(mouseHandler);
            removeMouseMotionListener(mouseHandler);
        }
        this.isInteractiveLayer = mode;
        repaint();
    }

    public void notifyAboutPropertiesChange() {
        buildSpline();
        repaint();
    }

    public void zoomIn() {
        if(spline == null){
            return;
        }
        if(currentMax-1<spline.getMax()/1.5){
            return;
        }
        createFunction(currentMax-1);
        buildSpline();
        repaint();
    }

    public void zoomOut() {
        createFunction(currentMax+1);
        buildSpline();
        repaint();
    }

    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    class MouseHandler extends MouseAdapter {

        private Point2D dragPoint = null;
        private boolean newPoint = false;
        @Override
        public void mouseDragged(MouseEvent e) {
            if(dragPoint==null){
                return;
            }
            Point point = e.getPoint();
            Point2D point2D = f.getCord(point.x, point.y);
            try {
                image.getRGB(point.x, point.y);
                if(!newPoint) {
                    spline.replacePoint(dragPoint,point2D);
                    buildSpline();
                }
                else {
                    spline.replaceSupportPoint(dragPoint, point2D);
                    clearImage(interactiveLayer);
                    spline.drawSupportPoints(interactiveLayer,f);
                }
                dragPoint = point2D;
                repaint();
            }catch (ArrayIndexOutOfBoundsException ex){
                if(point.x < 0){
                    point.x = 0;
                }
                if(point.x>=image.getWidth()){
                    point.x = image.getWidth()-1;
                }
                if(point.y<0){
                    point.y = 0;
                }
                if(point.y>=image.getHeight()){
                    point.y = image.getHeight()-1;
                }
                if(!newPoint) {
                    spline.replacePoint(dragPoint, point2D);
                    buildSpline();
                }
                else {
                    spline.replaceSupportPoint(dragPoint, point2D);
                    clearImage(interactiveLayer);
                    spline.drawSupportPoints(interactiveLayer,f);
                }
                dragPoint = point2D;
                repaint();
            }
        }


        @Override
        public void mousePressed(MouseEvent e){
            Point point = e.getPoint();
            for (Point2D point2D: spline.getPoints()){
                Point p = f.getPixel(point2D.getX(), point2D.getY());
                int deltaX = Math.abs(point.x-p.x);
                int deltaY = Math.abs(point.y-p.y);
                if((deltaX)*(deltaX) + (deltaY)*(deltaY) <= 100){
                    dragPoint = point2D;
                    return;
                }
            }
            for (Point2D point2D: spline.getSupportPoints()){
                Point p = f.getPixel(point2D.getX(), point2D.getY());
                int deltaX = Math.abs(point.x-p.x);
                int deltaY = Math.abs(point.y-p.y);
                if((deltaX)*(deltaX) + (deltaY)*(deltaY) <= 60){
                    dragPoint = point2D;
                    newPoint = true;
                    return;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
            if(dragPoint == null){
                return;
            }
            if (e.getButton() == MouseEvent.BUTTON3 && spline.getPoints().size()>4) {
                if (!newPoint) {
                    spline.removePoint(dragPoint);
                }
            }
            else if(newPoint){
                spline.addPoint(dragPoint);
            }
            newPoint = false;
            dragPoint = null;
            if(autoScale) {
                createFunction(spline.getMax());
            }
            buildSpline();
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }


}
