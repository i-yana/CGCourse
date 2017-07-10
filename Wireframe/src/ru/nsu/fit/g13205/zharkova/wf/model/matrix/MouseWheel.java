package ru.nsu.fit.g13205.zharkova.wf.model.matrix;

import ru.nsu.fit.g13205.zharkova.wf.view.View3D;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Created by Yana on 26.04.16.
 */
public class MouseWheel implements MouseWheelListener {

    private View3D view3D;

    public MouseWheel(View3D view3D){
        this.view3D = view3D;
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() >0){//up
            view3D.getProperties().getViewParameters().addZN(-0.1);
            view3D.paintObjects();
        }
        else if(e.getWheelRotation()<0){//down
            view3D.getProperties().getViewParameters().addZN(0.1);
            view3D.paintObjects();
        }
    }
}
