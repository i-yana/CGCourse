package ru.nsu.fit.g13205.zharkova.wf.model.matrix;

import ru.nsu.fit.g13205.zharkova.wf.model.Camera;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;

/**
 * Created by Yana on 24.04.16.
 */
public class MatrixCamCreator {

    public static double[][] createCamMatrix(Camera camera){


        Point3D zaxis = camera.getPosition().sub(camera.getLookAt()).normalize();
        Point3D xaxis = camera.getUp().cross(zaxis).normalize();
        Point3D yaxis = zaxis.cross(xaxis);

        double[][] result = new double[4][4];
        result[0][0] = xaxis.x;
        result[0][1] = xaxis.y;
        result[0][2] = xaxis.z;

        result[1][0] = yaxis.x;
        result[1][1] = yaxis.y;
        result[1][2] = yaxis.z;

        result[2][0] = zaxis.x;
        result[2][1] = zaxis.y;
        result[2][2] = zaxis.z;

        result[3][0] = 0.0;
        result[3][1] = 0.0;
        result[3][2] = 0.0;
        result[3][3] = 1.0;

        double[][] shift = new double[4][4];
        shift[0][0] = 1;
        shift[1][1] = 1;
        shift[2][2] = 1;
        shift[3][3] = 1;
        shift[0][3] = -camera.getPosition().getX();
        shift[1][3] = -camera.getPosition().getY();
        shift[2][3] = -camera.getPosition().getZ();



        return Matrix.mul(result, shift);
    }


}
