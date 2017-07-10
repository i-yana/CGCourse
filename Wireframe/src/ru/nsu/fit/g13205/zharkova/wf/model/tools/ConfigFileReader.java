package ru.nsu.fit.g13205.zharkova.wf.model.tools;

import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.wf.model.RotationObject;
import ru.nsu.fit.g13205.zharkova.wf.model.ViewParameters;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 06.04.16.
 */
public class ConfigFileReader {

    private File file;

    public ConfigFileReader(File file){
        this.file = file;
    }

    public Properties read() throws IOException, ParserConfigurationException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] oneLine;
        int n;
        int m;
        int k;
        double a;
        double b;
        double c;
        double d;
        ViewParameters viewParameters;
        Color backgroundColor;
        int K;
        try {
            oneLine = parse(7, getNextLine(br));
            n = Integer.parseInt(oneLine[0]);
            m = Integer.parseInt(oneLine[1]);
            k = Integer.parseInt(oneLine[2]);
            a = Double.parseDouble(oneLine[3]);
            b = Double.parseDouble(oneLine[4]);
            c = Double.parseDouble(oneLine[5]);
            d = Double.parseDouble(oneLine[6]);

            oneLine = parse(4, getNextLine(br));
            viewParameters = new ViewParameters(Double.parseDouble(oneLine[0]),Double.parseDouble(oneLine[1]),Double.parseDouble(oneLine[2]),Double.parseDouble(oneLine[3]));
            double[][] rotateMatrix = new double[4][4];
            rotateMatrix[0][3] = rotateMatrix[1][3] = rotateMatrix[2][3] = rotateMatrix[3][0] = rotateMatrix[3][1] = 0;
            rotateMatrix[3][3] = 1;

            oneLine = parse(3, getNextLine(br));
            rotateMatrix[0][0] = Double.parseDouble(oneLine[0]);
            rotateMatrix[0][1] = Double.parseDouble(oneLine[1]);
            rotateMatrix[0][2] = Double.parseDouble(oneLine[2]);

            oneLine = parse(3, getNextLine(br));
            rotateMatrix[1][0] = Double.parseDouble(oneLine[0]);
            rotateMatrix[1][1] = Double.parseDouble(oneLine[1]);
            rotateMatrix[1][2] = Double.parseDouble(oneLine[2]);

            oneLine = parse(3, getNextLine(br));
            rotateMatrix[2][0] = Double.parseDouble(oneLine[0]);
            rotateMatrix[2][1] = Double.parseDouble(oneLine[1]);
            rotateMatrix[2][2] = Double.parseDouble(oneLine[2]);

            oneLine = parse(3, getNextLine(br));
            backgroundColor = new Color(Integer.parseInt(oneLine[0]),Integer.parseInt(oneLine[1]),Integer.parseInt(oneLine[2]));
            oneLine = parse(1, getNextLine(br));
            K = Integer.parseInt(oneLine[0]);
            ArrayList<RotationObject> rotationObjects = new ArrayList<>();
            try {
            for (int i = 0; i < K; i++) {

                    Color objColor;
                    Point3D centralPoint;
                    int N;
                    oneLine = parse(3, getNextLine(br));
                    objColor = new Color(Integer.parseInt(oneLine[0]), Integer.parseInt(oneLine[1]), Integer.parseInt(oneLine[2]));
                    oneLine = parse(3, getNextLine(br));
                    centralPoint = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));
                    double[][] rotateObjMatrix = new double[4][4];
                    rotateObjMatrix[0][3] = rotateObjMatrix[1][3] = rotateObjMatrix[2][3] = rotateObjMatrix[3][0] = rotateObjMatrix[3][1] = 0;
                    rotateObjMatrix[3][3] = 1;

                    oneLine = parse(3, getNextLine(br));
                    rotateObjMatrix[0][0] = Double.parseDouble(oneLine[0]);
                    rotateObjMatrix[0][1] = Double.parseDouble(oneLine[1]);
                    rotateObjMatrix[0][2] = Double.parseDouble(oneLine[2]);

                    oneLine = parse(3, getNextLine(br));
                    rotateObjMatrix[1][0] = Double.parseDouble(oneLine[0]);
                    rotateObjMatrix[1][1] = Double.parseDouble(oneLine[1]);
                    rotateObjMatrix[1][2] = Double.parseDouble(oneLine[2]);

                    oneLine = parse(3, getNextLine(br));
                    rotateObjMatrix[2][0] = Double.parseDouble(oneLine[0]);
                    rotateObjMatrix[2][1] = Double.parseDouble(oneLine[1]);
                    rotateObjMatrix[2][2] = Double.parseDouble(oneLine[2]);
                    oneLine = parse(1, getNextLine(br));
                    N = Integer.parseInt(oneLine[0]);
                    ArrayList<Point2D> points = new ArrayList<>();
                    for (int j = 0; j < N; j++) {
                        oneLine = parse(2, getNextLine(br));
                        points.add(new Point2D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1])));
                    }
                    RotationObject object = new RotationObject(objColor, centralPoint, rotateObjMatrix, points);
                    rotationObjects.add(object);

            }
            for (int i = 0; i < K; i++) {
                try {
                    oneLine = parse(7, getNextLine(br));
                }catch (NullPointerException ignored){
                    rotationObjects.get(i).setParams(n,m,k,a,b,c,d);
                    continue;
                }
                n = Integer.parseInt(oneLine[0]);
                m = Integer.parseInt(oneLine[1]);
                k = Integer.parseInt(oneLine[2]);
                a = Double.parseDouble(oneLine[3]);
                b = Double.parseDouble(oneLine[4]);
                c = Double.parseDouble(oneLine[5]);
                d = Double.parseDouble(oneLine[6]);
                rotationObjects.get(i).setParams(n,m,k,a,b,c,d);
            }
            }catch (NumberFormatException e){
                throw new ParserConfigurationException("Error in object "+ K + "int line: " + e.getMessage());
            }
            return new Properties(viewParameters, rotateMatrix, backgroundColor, rotationObjects);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    private String[] parse(int count, String line) throws IOException {
        if(line.contains("//")){
            line = line.substring(0, line.indexOf("//"));
        }
        if(line.split(" ").length != count){
            throw new IOException(line);
        }
        return line.split(" ");
    }

    private String getNextLine(BufferedReader br) throws IOException {
        String line;
        while(true) {
            line = br.readLine();
            line = line.trim();
            if(line.isEmpty()){
                continue;
            }
            if(line.length() == 1){
                break;
            }
            if(line.substring(0,2).equals("//")){
                continue;
            }
            break;
        }
        return line;
    }
}
