package ru.nsu.fit.g13205.zharkova.wf.model.tools;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.wf.model.RotationObject;
import ru.nsu.fit.g13205.zharkova.wf.model.ViewParameters;
import ru.nsu.fit.g13205.zharkova.wf.model.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 06.04.16.
 */
public class Properties {
    public final static ArrayList<Point2D> DEFAULT_POINTS;
    private ViewParameters viewParameters;
    private double[][] sceneMatrix;
    private Color backgroundColor;
    private ArrayList<RotationObject> objects;

    static {
        DEFAULT_POINTS = new ArrayList<>();
        DEFAULT_POINTS.add(new Point2D(-3,1));
        DEFAULT_POINTS.add(new Point2D(-2,1));
        DEFAULT_POINTS.add(new Point2D(-1,1));
        DEFAULT_POINTS.add(new Point2D(0, 1));
        DEFAULT_POINTS.add(new Point2D(1,1));
        DEFAULT_POINTS.add(new Point2D(2,1));
        DEFAULT_POINTS.add(new Point2D(3,1));
    }

    public Properties(ViewParameters viewParameters, double[][] sceneMatrix, Color backgroundColor, ArrayList<RotationObject> objects){
        this.backgroundColor = backgroundColor;
        this.sceneMatrix = sceneMatrix;
        this.viewParameters = viewParameters;
        this.objects = objects;
    }

    public Properties(){
        this.backgroundColor = Color.BLACK;
        this.sceneMatrix= new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
        this.viewParameters = new ViewParameters(8,30,10,10);
        this.objects = new ArrayList<>();
        ArrayList<Point2D> point2Ds = new ArrayList<>();


        point2Ds.add(new Point2D(0.06363717374293998,16.672939520650395));
        point2Ds.add(new Point2D(-0.19091152122882704,10.054673451384588));
        point2Ds.add(new Point2D(-0.1906861224489802,2.2882334693877553));
        point2Ds.add(new Point2D(-0.038137224489796395,0.4576466938775514));
        point2Ds.add(new Point2D(0.11441167346938741,-1.3729400816326525));

        ArrayList<Point2D> point2Ds1 =new ArrayList<>();
        point2Ds1.add(new Point2D(-0.8571428571428577,9.874285714285715));
        point2Ds1.add(new Point2D(1.4057142857142857,6.78857142857143));
        point2Ds1.add(new Point2D(4,4));
        point2Ds1.add(new Point2D(5.451428571428572,0.274285714285714));
        point2Ds1.add(new Point2D(7.8514285714285705,0.3428571428571434));
        point2Ds1.add(new Point2D(10.525714285714287,-0.0685714285714294));
        point2Ds1.add(new Point2D(11.142857142857142,3.3599999999999994));
        point2Ds1.add(new Point2D(11.965714285714284, 5.280000000000001));
        RotationObject rotationObject = new RotationObject(Color.CYAN, new Point3D(0,0,5), new double[][]{{1.0, 0.0, 0.0 ,0.0},{0.0, 1.0, 0.0,0.0},{0.0, 0.0, 1.0,0.0}, {0,0,0,1}}, point2Ds);
        rotationObject.setParams(10,10,10,0.0,1.0,0.0,6.28);
        objects.add(rotationObject);
        RotationObject rotationObject1 = new RotationObject(Color.ORANGE, new Point3D(0,0,0), new double[][]{{1.0, 0.0, 0.0 ,0.0},{0.0, 1.0, 0.0,0.0},{0.0, 0.0, 1.0,0.0}, {0,0,0,1}},point2Ds1);
        rotationObject1.setParams(10,10,10,0.0,1.0,0.0,6.28);
        objects.add(rotationObject1);
    }


    public ViewParameters getViewParameters() {
        return viewParameters;
    }

    public double[][] getSceneMatrix() {
        return sceneMatrix;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public ArrayList<RotationObject> getRotationObjects() {
        return objects;
    }


    public void setColor(Color color) {
        this.backgroundColor = color;
    }

    public void setZF(double zf){
        viewParameters.setZF(zf);
    }

    public void setZN(double zn){
        viewParameters.setZN(zn);
    }

    public void setSW(double sw){
        viewParameters.setSW(sw);
    }

    public void setSH(double sh){
        viewParameters.setSH(sh);
    }

    public void changeSceneMatrix(double ex, double ey, double ez) {
        double[][] matrixX = Matrix.createRxMatrix(ex);
        double[][] matrixY = Matrix.createRyMatrix(ey);
        sceneMatrix = Matrix.mul(matrixY,Matrix.mul(matrixX, sceneMatrix));
    }

    public void initSceneMatrix() {
        sceneMatrix= new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    }
}
