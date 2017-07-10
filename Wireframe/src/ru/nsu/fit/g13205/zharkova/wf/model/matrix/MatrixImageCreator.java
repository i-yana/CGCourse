package ru.nsu.fit.g13205.zharkova.wf.model.matrix;

/**
 * Created by Yana on 24.04.16.
 */
public class MatrixImageCreator {
    public static double[][] createImageMatrix(int width, int height) {
        double[][] result = new double[4][4];
        result[0][0] =  (double)width;
        result[1][1] = -(double)height;
        result[2][2] =  1.;
        result[3][3] = 1.;
        result[0][3] = (double)width/2;
        result[1][3] = (double)height/2;
        return result;
    }
}
