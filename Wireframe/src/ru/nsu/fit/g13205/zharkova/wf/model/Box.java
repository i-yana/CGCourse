package ru.nsu.fit.g13205.zharkova.wf.model;

import ru.nsu.fit.g13205.zharkova.wf.model.tools.Edge3D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;

import java.util.ArrayList;

/**
 * Created by Yana on 27.04.16.
 */
public class Box {

    private static ArrayList<Edge3D> box = new ArrayList<>();
    private static final int k = 10;
    private ArrayList<Edge3D> edges;

    static {
        box.add(new Edge3D(new Point3D(-1,-1,-1), new Point3D(-1,1,-1)));
        box.add(new Edge3D(new Point3D(-1,1,-1), new Point3D(1,1,-1)));
        box.add(new Edge3D(new Point3D(1,1,-1), new Point3D(1,-1,-1)));
        box.add(new Edge3D(new Point3D(1,-1,-1), new Point3D(-1,-1,-1)));

        box.add(new Edge3D(new Point3D(-1,-1,1), new Point3D(-1,1,1)));
        box.add(new Edge3D(new Point3D(-1,1,1), new Point3D(1,1,1)));
        box.add(new Edge3D(new Point3D(1,1,1), new Point3D(1,-1,1)));
        box.add(new Edge3D(new Point3D(1,-1,1), new Point3D(-1,-1,1)));

        box.add(new Edge3D(new Point3D(-1,1,1), new Point3D(-1,1,-1)));
        box.add(new Edge3D(new Point3D(1,1,1), new Point3D(1,1,-1)));
        box.add(new Edge3D(new Point3D(1,-1,1), new Point3D(1,-1,-1)));
        box.add(new Edge3D(new Point3D(-1,-1,1), new Point3D(-1,-1,-1)));
    }

    public Box(){
        edges = new ArrayList<>();
        for (Edge3D edge3D : box){
            Point3D p1 = edge3D.getP1();
            Point3D p2 = edge3D.getP2();
            if(p1.getX() != p2.getX()){
                double step = -1;
                while (step+0.1<=1){
                    edges.add(new Edge3D(new Point3D(step, p1.getY(), p1.getZ()), new Point3D(step+0.1, p2.getY(), p2.getZ())));
                    step = step+0.1;
                }
            }
            if(p1.getY() != p2.getY()){
                double step = -1;
                while (step+0.1<=1){
                    edges.add(new Edge3D(new Point3D(p1.getX(), step, p1.getZ()), new Point3D(p2.getX(), step+0.1, p2.getZ())));
                    step = step+0.1;
                }
            }
            if(p1.getZ() != p2.getZ()){
                double step = -1;
                while (step+0.1<=1){
                    edges.add(new Edge3D(new Point3D(p1.getX(), p1.getY(), step), new Point3D(p1.getX(), p1.getY(), step+0.1)));
                    step = step+0.1;
                }
            }

        }

    }

    public ArrayList<Edge3D> getEdges(){
        return edges;
    }


}
