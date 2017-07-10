package ru.nsu.fit.g13205.zharkova.raytracing.model;


import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Matrix;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;

/**
 * Created by Yana on 14.05.16.
 */
public class Camera {

    private Point3D eye;
    private Point3D view;
    private Point3D up;
    private Point3D right;

    public Camera(Point3D eye, Point3D view, Point3D up){
        this.eye = eye;
        this.view = view;
        this.up = up;
        adjustUpVector();
    }

    public Point3D getEye() {
        return eye;
    }

    public Point3D getView() {
        return view;
    }

    public Point3D getUp() {
        return up;
    }

    public void setView(Point3D view) {
        this.view = view;
    }

    public void adjustUpVector() {
        Point3D zaxis = eye.sub(view).normalize();
        right = up.cross(zaxis).normalize();
        up = zaxis.cross(right);
    }

    public void upCamera(double v) {
        eye = new Point3D(eye.x, eye.y+v,eye.z);
        view = new Point3D(view.x, view.y+v, view.z);
    }

    public void downCamera(double v) {
        eye = new Point3D(eye.x, eye.y-v,eye.z);
        view = new Point3D(view.x, view.y-v, view.z);
    }

    public void leftCamera(double v) {
        eye = new Point3D(eye.x, eye.y,eye.z-v);
        view = new Point3D(view.x, view.y, view.z-v);
    }

    public void rightCamera(double v) {
        eye = new Point3D(eye.x, eye.y,eye.z+v);
        view = new Point3D(view.x, view.y, view.z+v);
    }

    public Point3D getRight() {
        return right;
    }

    public Camera rotateCamera(double[][] matrix) {
        double[][] shift_one = new double[][]{
                {
                        1d,0,0,-view.x
                },
                {
                        0,1d,0,-view.y
                },
                {
                        0,0,1d,-view.z
                },
                {
                        0,0,0,1d
                }
        };
        double[][] shift_two = new double[][]{
                {
                        1d,0,0,view.x
                },
                {
                        0,1d,0,view.y
                },
                {
                        0,0,1d,view.z
                },
                {
                        0,0,0,1d
                }
        };
        double[][] inverseMatrix = Matrix.transposition(matrix);
        double[][] inverse = Matrix.mul(shift_two, Matrix.mul(inverseMatrix, shift_one));
        Point3D newEye = Matrix.mul(inverse, eye);
        Point3D newView = view;
        Point3D newUp = Matrix.mul(inverseMatrix, up);
        Camera c = new Camera(newEye, newView, newUp);
        return c;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "eye=" + eye +
                ", view=" + view +
                ", up=" + up +
                ", right=" + right +
                '}';
    }
}
