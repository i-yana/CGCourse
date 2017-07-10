package ru.nsu.fit.g13205.zharkova;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Yana on 23.03.16.
 */
public class Tools {

    public static final double ZOOM_STEP = 0.5;
    public static final String FORMAT = "#0.000000";
    public static final Pattern zeroPattern;

    static {
        zeroPattern = Pattern.compile(",?0*$");
    }

    private static final Double EPS = 0.01;


    public static String sub(String s, int v) {
        if(s.length()>v){
            s = s.substring(0,v);
        }
        return s;
    }

    public static ArrayList<Double> buildColorLevels(int colors, double min, double max) {
        ArrayList<Double> levels = new ArrayList<>();
        double delta = (max-min)/colors;
        double h = min;
        for (int i = 0; i < colors; i++) {
            levels.add(h);
            h = h + delta;
        }
        return levels;
    }

    public static Color findLevel(double h, Function function, ArrayList<Color> colors) {
        ArrayList<Double> levels = function.getLevels();
        Color lvlColor = colors.get(0);
        for (int i = 0; i < levels.size(); i++) {
            if(h >= levels.get(i)){
                if(i+1 ==levels.size() || h<levels.get(i+1)) {
                    lvlColor = colors.get(i);
                }
            }
        }
        return lvlColor;
    }
    
    private static Info analyses(Function function, double z, Cord cF1, Cord cF2, Cord cF3, Cord cF4) throws IOException {
        ArrayList<Point> inputPoint = new ArrayList<>();
        ArrayList<Integer> sides = new ArrayList<>();
        double f1 = function.getZ(cF1);
        double f2 = function.getZ(cF2);
        double f3 = function.getZ(cF3);
        double f4 = function.getZ(cF4);
        if(Math.abs(z-f1)<0.00000000001){
            f1 = z;
        }
        if(Math.abs(z-f2)<0.00000000001){
            f2 = z;
        }
        if(Math.abs(z-f3)<0.00000000001){
            f3 = z;
        }
        if(Math.abs(z-f4)<0.00000000001){
            f4 = z;
        }
        if(f1 == z && f2 == z && f3 == z && f4 == z){
            Point p = new Point(function.getPixel(cF1.x, cF1.y));
            inputPoint.add(p);
            sides.add(1);
            p = new Point(function.getPixel(cF2.x, cF2.y));
            inputPoint.add(p);
            sides.add(2);
            p = new Point(function.getPixel(cF3.x, cF3.y));
            inputPoint.add(p);
            sides.add(3);
            p = new Point(function.getPixel(cF4.x, cF4.y));
            inputPoint.add(p);
            sides.add(4);
        }
        else {
            if (!(z < f1 && z < f2 || z > f1 && z > f2)) {
                double dx = cF2.x - cF1.x;
                double x;
                if (f1 < f2) {
                    x = cF1.x + dx * (z - f1) / (f2 - f1);
                } else {
                    x = cF2.x - dx * (z - f2) / (f1 - f2);
                }
                Point p = new Point(function.getPixel(x, cF2.y));
                if (!inputPoint.contains(p)) {
                    inputPoint.add(p);
                    sides.add(1);
                }
            }
            if (!(z < f1 && z < f3 || z > f1 && z > f3)) {
                double dy = cF3.y - cF1.y;
                double y;
                if (f1 < f3) {
                    y = cF1.y + dy * (z - f1) / (f3 - f1);
                } else {
                    y = cF3.y - dy * (z - f3) / (f1 - f3);
                }
                Point p = new Point(function.getPixel(cF1.x, y));
                if (!inputPoint.contains(p)) {
                    inputPoint.add(new Point(function.getPixel(cF1.x, y)));
                    sides.add(2);
                }
            }
            if (!(z < f3 && z < f4 || z > f3 && z > f4)) {
                double dx = cF4.x - cF3.x;
                double x;
                if (f3 < f4) {
                    x = cF3.x + dx * (z - f3) / (f4 - f3);
                } else {
                    x = cF4.x - dx * (z - f4) / (f3 - f4);
                }
                Point p = new Point(function.getPixel(x, cF3.y));
                if (!inputPoint.contains(p)) {
                    inputPoint.add(p);
                    sides.add(3);
                }
            }
            if (!(z < f2 && z < f4 || z > f2 && z > f4)) {
                double dy = cF4.y - cF2.y;
                double y;
                if (f2 < f4) {
                    y = cF2.y + dy * (z - f2) / (f4 - f2);
                } else {
                    y = cF4.y - dy * (z - f4) / (f2 - f4);
                }
                Point p = new Point(function.getPixel(cF2.x, y));
                if (!inputPoint.contains(p)) {
                    inputPoint.add(p);
                    sides.add(4);
                }
            }
        }
        return new Info(inputPoint, sides);
    }

    public static void createIsolines(Function function, BufferedImage image, Double z, Color color) throws IOException {
        Cord[][] pointInNode = function.getFunctionGrid();
        Point[][] gridNode = function.getPixelGrid();
        Graphics g = image.getGraphics();
        g.setColor(color);
        for (int i = 0; i < gridNode.length - 1; i++) {
            for (int j = 0; j < gridNode[i].length - 1; j++) {
                Info info = analyses(function, z, pointInNode[i][j], pointInNode[i][j + 1], pointInNode[i + 1][j], pointInNode[i + 1][j + 1]);

                ArrayList<Point> inputPoint = info.getPoints();
                ArrayList<Integer> sides = info.getSides();
                switch (inputPoint.size()){
                    case 1:
                        g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(0).x, inputPoint.get(0).y);
                        break;
                    case 2:
                        g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(1).x, inputPoint.get(1).y);
                        break;
                    case 3:{
                        int first = sides.get(0);
                        int second = sides.get(1);
                        int third = sides.get(2);
                        if(Math.abs(first-second) == 0 ){
                            g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(2).x, inputPoint.get(2).y);
                        }
                        if(Math.abs(first-third) == 0 ){
                            g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(3).x, inputPoint.get(3).y);
                        }
                        if(Math.abs(second-third) == 0 ){
                            g.drawLine(inputPoint.get(2).x, inputPoint.get(2).y, inputPoint.get(3).x, inputPoint.get(3).y);
                        }
                        break;
                    }
                    case 4:{
                        double f5 = (function.getZ(pointInNode[i][j]) + function.getZ(pointInNode[i][j+1]) + function.getZ(pointInNode[i+1][j]) + function.getZ(pointInNode[i+1][j+1]))/4;
                        if(f5 >= z){
                            g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(3).x, inputPoint.get(3).y);
                            g.drawLine(inputPoint.get(1).x, inputPoint.get(1).y, inputPoint.get(2).x, inputPoint.get(2).y);
                        }
                        else {
                            g.drawLine(inputPoint.get(0).x, inputPoint.get(0).y, inputPoint.get(1).x, inputPoint.get(1).y);
                            g.drawLine(inputPoint.get(3).x, inputPoint.get(3).y, inputPoint.get(2).x, inputPoint.get(2).y);
                        }
                    }
                }
            }
        }
        g.dispose();
    }

    public static BufferedImage interpolateLegend(Legend l, BufferedImage inImage){
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());

        ArrayList<Double> levels = l.getLevels();
        if(levels.size()<1){
            outImage = inImage;
        }
        Point pixel = l.getPixel(0, levels.get(levels.size()-1));
        int stepY = pixel.y;
        int startY = pixel.y/2;
        for (int i = 0; i < startY; i++) {
            for (int j = 0; j < outImage.getWidth(); j++) {
                outImage.setRGB(j,i,inImage.getRGB(j,i));
            }
        }
        for (int i = startY; i < inImage.getHeight(); i = i + stepY) {
            for (int y = i; y < (i+stepY>=inImage.getHeight()?inImage.getHeight():i+stepY); y++) {
                for (int x = 0; x < inImage.getWidth(); x++) {
                    int v1 = i;
                    int v2 = (i+stepY>=inImage.getHeight()?inImage.getHeight():i+stepY)-1;
                    Color rgb1 = new Color(inImage.getRGB(x, v1));
                    Color rgb2 = new Color(inImage.getRGB(x, v2));
                    int red = rgb1.getRed() * (v2 - y) / (v2 - v1) + rgb2.getRed() * (y - v1) / (v2 - v1);
                    int green = rgb1.getGreen() * (v2 - y) / (v2 - v1) + rgb2.getGreen() * (y - v1) / (v2 - v1);
                    int blue = rgb1.getBlue() * (v2 - y) / (v2 - v1) + rgb2.getBlue() * (y - v1) / (v2 - v1);
                    outImage.setRGB(x,y,new Color(red, green, blue).getRGB());
                }
            }
        }
        return outImage;
    }

    public static String eraseLastZero(String text){
        return zeroPattern.matcher(text).replaceAll("");
    }

    public static BufferedImage interpolateMap(MainFunction function, Legend legend, BufferedImage interpolateLegend, int w, int h) throws IOException {
        BufferedImage outImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < outImage.getHeight(); i++) {
            for (int j = 0; j < outImage.getWidth(); j++) {
                Double z;
                Point pixel;
                Cord functionCord = function.getCord(j, i);
                z = function.getZ(functionCord);
                pixel = legend.getPixel(0, z);
                Color color = new Color(interpolateLegend.getRGB(pixel.x, pixel.y >= interpolateLegend.getHeight() ? pixel.y - 1 : pixel.y));
                outImage.setRGB(j, i, color.getRGB());
            }
        }
        return outImage;
    }

    static class Info{
        private ArrayList<Point> points;
        private ArrayList<Integer> sides;

        public Info(ArrayList<Point> points, ArrayList<Integer> sides){
            this.points = points;
            this.sides = sides;
        }
        public ArrayList<Point> getPoints(){
            return points;
        }
        public ArrayList<Integer> getSides(){
            return sides;
        }
    }
}
