package ru.nsu.fit.g13205.zharkova.wf.model.tools;

/**
 * Created by Yana on 26.04.16.
 */
public class Clipper {

    public static boolean tryPoint(Point3D p1, Point3D p2, double zn, double zf, double sw, double sh){
        if(p1.getX() >= -sw/2.0 && p1.getX() <= sw/2.0 && p2.getX() >= -sw/2.0 && p2.getX() <= sw/2.0){
            if(p1.getY() >= -sh/2.0 && p1.getY() <= sh/2.0 && p2.getY() >= -sh/2.0 && p2.getY() <= sh/2.0){
                if(p1.getZ() >= zn && p1.getZ() <= zf && p2.getZ() >= zn && p2.getZ() <= zf) {
            return true;
        }}}

        return false;
    }
}
