package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;


import ru.nsu.fit.g13205.zharkova.raytracing.model.Camera;
import ru.nsu.fit.g13205.zharkova.raytracing.model.Wireframe;

/**
 * Created by Yana on 17.04.16.
 */
public class Matrix {

    public static double[][] buildMatrix(double[][] rotate, Camera camera, Wireframe v){
        double[][] shift_one = new double[][]{
                {
                        1d,0,0,-camera.getView().x
                },
                {
                        0,1d,0,-camera.getView().y
                },
                {
                        0,0,1d,-camera.getView().z
                },
                {
                        0,0,0,1d
                }
        };
        double[][] shift_two = new double[][]{
                {
                        1d,0,0,camera.getView().x
                },
                {
                        0,1d,0,camera.getView().y
                },
                {
                        0,0,1d,camera.getView().z
                },
                {
                        0,0,0,1d
                }
        };
        double[][] sceneMatrix = Matrix.mul(shift_two, Matrix.mul(rotate, shift_one));
        double[][] toCam = MatrixCamCreator.createCamMatrix(camera);
        double[][] toProj = MatrixProjCreator.createScreenMat(v);
        double[][] matrix = Matrix.mul(toProj, toCam);
        double[][] res = Matrix.mul(matrix, sceneMatrix);
        return res;
    }


    public static double[][] getRotationMatrix(Double phi) {
        double[][] matrix = new double[3][3];
        matrix[0][0] = Math.cos(phi);
        matrix[1][1] = Math.sin(phi);
        matrix[2][2] = 1;
        return matrix;
    }

    public static double[][] mul(double[][] mA, double[][] mB) {
        int m = mA.length;
        int n = mB[0].length;
        int o = mB.length;
        double[][] res = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; k++) {
                    res[i][j] += mA[i][k] * mB[k][j];
                }
            }
        }
        return res;
    }

    public static double scalarMultiplication(double[] v1, double[] v2){
        double value = 0;

        for(int i = 0; i < v1.length; i++){
            value += v1[i] * v2[i];
        }

        return value;
    }

    public static double[][] mulVector(double[][] mA, Point3D point3D) {
        double[][] vector = new double[4][1];
        vector[0][0] = point3D.getX();
        vector[1][0] = point3D.getY();
        vector[2][0] = point3D.getZ();
        vector[3][0] = point3D.getW();
        double[][] res =  mul(mA,vector);
        return res;
    }

    public static Point3D mul(double[][] mA, Point3D point3D) {
        double[][] vector = new double[4][1];
        vector[0][0] = point3D.getX();
        vector[1][0] = point3D.getY();
        vector[2][0] = point3D.getZ();
        vector[3][0] = point3D.getW();
        double[][] res =  mul(mA,vector);
        return new Point3D(res[0][0], res[1][0], res[2][0], res[3][0]);
    }

    public static double[][] transposition(double[][] m){
        double[][] result = new double[m[0].length][m.length];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = m[j][i];
            }
        }
        return result;
    }



    public static double[][] createRxMatrix(double ex) {
        double[][] mat = new double[][]{
                {
                        1,0,0,0
                },
                {
                       0, Math.cos(ex),-Math.sin(ex),0
                },
                {
                        0,Math.sin(ex),Math.cos(ex),0
                },
                {
                        0,0,0,1
                }
        };
        mat[0][0] = 1;
        return mat;
    }

    public static double[][] createRyMatrix(double ey) {
        double[][] mat = new double[][]{
                {
                  Math.cos(ey),0,-Math.sin(ey),0
                },
                {
                        0,1,0,0
                },
                {
                        Math.sin(ey),0, Math.cos(ey),0
                },
                {
                        0,0,0,1
                }
        };
        return mat;
    }

    public static double[][] createRzMatrix(double ez) {
        double[][] mat = new double[][]{
                {
                    Math.cos(ez),-Math.sin(ez),0,0
                },
                {
                    Math.sin(ez),Math.cos(ez),0,0
                },
                {
                    0,0,1,0
                },
                {
                    0,0,0,1
                }
        };
        return mat;
    }
}
