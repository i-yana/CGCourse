package ru.nsu.fit.g13205.zharkova.raytracing.view;

import ru.nsu.fit.g13205.zharkova.raytracing.model.RenderSetting;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Created by Yana on 26.04.16.
 */
public class MouseWheel implements MouseWheelListener {

    private View3D view3D;
    private boolean ctrlPressed = false;


    public MouseWheel(View3D view3D){
        this.view3D = view3D;
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(ctrlPressed){
            RenderSetting r = view3D.getRenderSetting();
            if(e.getWheelRotation() >0){//up
                Point3D v = r.getCamera().getView();
                r.getCamera().setView(new Point3D(v.x, v.y, v.z+0.5));
                view3D.paintObjects();
            }
            else if(e.getWheelRotation()<0){//down
                Point3D v = r.getCamera().getView();
                r.getCamera().setView(new Point3D(v.x, v.y, v.z - 0.5));
                view3D.paintObjects();
            }
        }
        else {
            if (e.getWheelRotation() > 0) {//up
                view3D.getRenderSetting().getWireframe().divZN(1.1);
                view3D.paintObjects();
            } else if (e.getWheelRotation() < 0) {//down
                view3D.getRenderSetting().getWireframe().mulZN(1.1);
                view3D.paintObjects();
            }
        }
    }

    public void ctrl(boolean b) {
        ctrlPressed = b;
    }
}
