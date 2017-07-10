package ru.nsu.fit.g13205.zharkova.raytracing.io;

import ru.nsu.fit.g13205.zharkova.raytracing.model.Light;
import ru.nsu.fit.g13205.zharkova.raytracing.model.Scene;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Intensive;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces.*;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 11.05.16.
 */
public class SceneFileReader {

    private File file;

    public SceneFileReader(File file){
        this.file = file;
    }

    public Scene read() throws IOException, ParserConfigurationException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] oneLine;
        Color ambientLight;
        int numLight = 0;
        try{
            oneLine = Parser.parse(3, Parser.getNextLine(br));
            ambientLight = new Color(Integer.parseInt(oneLine[0]), Integer.parseInt(oneLine[1]),  Integer.parseInt(oneLine[2]));
            oneLine = Parser.parse(1, Parser.getNextLine(br));
            numLight = Integer.parseInt(oneLine[0]);
            ArrayList<Light> lights = new ArrayList<>();
            for (int i = 0; i < numLight; i++) {
                oneLine = Parser.parse(6, Parser.getNextLine(br));
                Point3D point = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                Color color = new Color(Integer.parseInt(oneLine[3]), Integer.parseInt(oneLine[4]), Integer.parseInt(oneLine[5]));
                Light light = new Light(point, color);
                lights.add(light);
            }
            ArrayList<Surface> surfaces = new ArrayList<>();
            Intensive kd, ks;
            Point3D point1, point2, point3, point4;
            double power;
            while (true) {
                try {
                    oneLine = Parser.parse(1, Parser.getNextLine(br));
                    switch (oneLine[0]) {
                        case "SPHERE":
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            Point3D center = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(1, Parser.getNextLine(br));
                            double radius = Double.parseDouble(oneLine[0]);
                            oneLine = Parser.parse(7, Parser.getNextLine(br));
                            kd = new Intensive(Float.parseFloat(oneLine[0]), Float.parseFloat(oneLine[1]), Float.parseFloat(oneLine[2]));
                            ks = new Intensive(Float.parseFloat(oneLine[3]), Float.parseFloat(oneLine[4]), Float.parseFloat(oneLine[5]));
                            power = Double.parseDouble(oneLine[6]);
                            surfaces.add(new Sphere(center, radius, kd, ks, power));
                            break;
                        case "BOX":
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            Point3D minPoint = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            Point3D maxPoint = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(7, Parser.getNextLine(br));
                            kd = new Intensive(Float.parseFloat(oneLine[0]), Float.parseFloat(oneLine[1]), Float.parseFloat(oneLine[2]));
                            ks = new Intensive(Float.parseFloat(oneLine[3]), Float.parseFloat(oneLine[4]), Float.parseFloat(oneLine[5]));
                            power = Double.parseDouble(oneLine[6]);
                            surfaces.add(new Box(minPoint, maxPoint, kd, ks, power));
                            break;
                        case "TRIANGLE":
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point1 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point2 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point3 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(7, Parser.getNextLine(br));
                            kd = new Intensive(Float.parseFloat(oneLine[0]), Float.parseFloat(oneLine[1]), Float.parseFloat(oneLine[2]));
                            ks = new Intensive(Float.parseFloat(oneLine[3]), Float.parseFloat(oneLine[4]), Float.parseFloat(oneLine[5]));
                            power = Double.parseDouble(oneLine[6]);
                            surfaces.add(new Triangle(point1, point2, point3, kd, ks, power));
                            break;
                        case "QUADRANGLE":
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point1 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point2 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point3 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(3, Parser.getNextLine(br));
                            point4 = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                            oneLine = Parser.parse(7, Parser.getNextLine(br));
                            kd = new Intensive(Float.parseFloat(oneLine[0]), Float.parseFloat(oneLine[1]), Float.parseFloat(oneLine[2]));
                            ks = new Intensive(Float.parseFloat(oneLine[3]), Float.parseFloat(oneLine[4]), Float.parseFloat(oneLine[5]));
                            power = Double.parseDouble(oneLine[6]);
                            surfaces.add(new Quadrangle(point1, point2, point3, point4, kd, ks, power));
                            break;
                        default:
                            throw new ParserConfigurationException("Wrong format");
                    }
                }catch (Exception e){
                    return new Scene(ambientLight, lights, surfaces);
                }
            }
        }catch (NumberFormatException e){
            throw new ParserConfigurationException("Error in object "+ numLight + "int line: " + e.getMessage());
        }
    }


}
