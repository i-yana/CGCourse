package ru.nsu.fit.g13205.zharkova.wf.model.tools;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.wf.model.RotationObject;
import ru.nsu.fit.g13205.zharkova.wf.model.ViewParameters;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Yana on 26.04.16.
 */
public class ConfigFileWriter {

    private File file;

    public ConfigFileWriter(File file) {
        this.file = file;
    }

    public void save(Properties properties) throws FileNotFoundException {
        PrintWriter os= new PrintWriter(file.getAbsoluteFile());
        ArrayList<RotationObject> rotationObjects = properties.getRotationObjects();
        ViewParameters viewParameters = properties.getViewParameters();
        Color color = properties.getBackgroundColor();
        double[][] sceneMatrix = properties.getSceneMatrix();
        if(rotationObjects.size() == 0){
            os.println(10 + " " + 10 + " " + 5 + " " + 0 + " " + 1 + " " + 0 + " " + 6.28);
        }
        else {
            RotationObject rotationObject = rotationObjects.get(0);
            int n = rotationObject.getN();
            int m = rotationObject.getM();
            int k = rotationObject.getK();
            double a = rotationObject.getA();
            double b = rotationObject.getB();
            double c = rotationObject.getC();
            double d = rotationObject.getD();
            os.println(n + " " + m + " " + k + " " + a + " " + b + " " + c + " " + d);
        }
        os.println(viewParameters.getZn() + " " + viewParameters.getZf() + " " + viewParameters.getSw() + " " + viewParameters.getSh());

        os.println(sceneMatrix[0][0] + " " + sceneMatrix[0][1] + " " + sceneMatrix[0][2]);
        os.println(sceneMatrix[1][0] + " " + sceneMatrix[1][1] + " " + sceneMatrix[1][2]);
        os.println(sceneMatrix[2][0] + " " + sceneMatrix[2][1] + " " + sceneMatrix[2][2]);

        os.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        os.println(rotationObjects.size());
        for (RotationObject rotationObject: rotationObjects){
            Color objColor = rotationObject.getObjColor();
            Point3D center = rotationObject.getCentralPoint();
            double[][] eulerMatrix = rotationObject.getEulerAngle();
            ArrayList<Point2D> points = rotationObject.getSpline().getPoints();

            os.println(objColor.getRed() + " " + objColor.getGreen() + " " + objColor.getBlue());
            os.println(center.getX() + " " + center.getY() + " " + center.getZ());
            os.println(eulerMatrix[0][0] + " " + eulerMatrix[0][1] + " " + eulerMatrix[0][2]);
            os.println(eulerMatrix[1][0] + " " + eulerMatrix[1][1] + " " + eulerMatrix[1][2]);
            os.println(eulerMatrix[2][0] + " " + eulerMatrix[2][1] + " " + eulerMatrix[2][2]);
            os.println(points.size());
            for (Point2D point2D : points){
                os.println(point2D.getX() + " " + point2D.getY());
            }
        }
        for (RotationObject rotationObject: rotationObjects){
            int n = rotationObject.getN();
            int m = rotationObject.getM();
            int k = rotationObject.getK();
            double a = rotationObject.getA();
            double b = rotationObject.getB();
            double c = rotationObject.getC();
            double d = rotationObject.getD();
            os.println(n + " " + m + " " + k + " " + a + " " + b + " " + c + " " + d);
        }
        os.close();
    }
}
