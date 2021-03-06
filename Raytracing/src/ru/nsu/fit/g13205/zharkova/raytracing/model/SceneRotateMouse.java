package ru.nsu.fit.g13205.zharkova.raytracing.model;

import ru.nsu.fit.g13205.zharkova.raytracing.view.View3D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yana on 14.05.16.
 */
public class SceneRotateMouse extends MouseAdapter {

    private View3D view3D;
    private boolean isEnable = true;

    public SceneRotateMouse(View3D view3D){
        this.view3D = view3D;
    }

    Point point;


    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isEnable || point==null){
            return;
        }
        try {
            Point p = e.getPoint();
            int deltaX = -point.x + p.x;
            int deltaY = -point.y + p.y;
            view3D.rotateCamera(deltaX * 0.01, deltaY * 0.01);
            view3D.paintObjects();
            point = p;
        }catch (Exception ignore){}
    }

    @Override
    public void mousePressed(MouseEvent e){
        if(!isEnable){
            return;
        }

        point = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e){
        point = null;
    }

    public void enableRotate(boolean isRotate) {
        this.isEnable = isRotate;
    }
}