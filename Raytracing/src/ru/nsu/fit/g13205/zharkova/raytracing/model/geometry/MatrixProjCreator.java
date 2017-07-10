package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;


import ru.nsu.fit.g13205.zharkova.raytracing.model.Wireframe;

/**
 * Created by Yana on 24.04.16.
 */
public class MatrixProjCreator {
   public static double[][] createScreenMat(Wireframe screen) {
        double zn = screen.getZn();


        return new double[][]{
            {
                1d,0,0,0
            },
            {
                0,1d,0,0
            },
            {
                0,0,1d,0
            },
            {
                0,0,1d/zn,0
            }
        };
    }


}
