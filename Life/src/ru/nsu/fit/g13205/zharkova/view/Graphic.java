package ru.nsu.fit.g13205.zharkova.view;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Created by Yana on 12.02.16.
 */


public class Graphic {

    public static void fill(Point seed, Color color, BufferedImage image){
        int oldColor = image.getRGB(seed.x, seed.y);
        if(oldColor == color.getRGB()){
            return;
        }
        Graphics imGraphics = image.getGraphics();
        imGraphics.setColor(color);
        Stack<Point> stack = new Stack<>();
        stack.push(seed);
        while (!stack.isEmpty()){
            Point point = stack.pop();
            int west;
            int east;
            int y = point.y;
            if(image.getRGB(point.x, point.y) == oldColor){
                west = point.x;
                east = point.x;
                while(west > 0 && image.getRGB(west, y) == oldColor){
                    west--;
                }
                west++;
                while (east < image.getWidth() && image.getRGB(east, y) == oldColor){
                    east++;
                }
                east--;
                imGraphics.drawLine(west, y, east, y);
                for (int x = west; x <= east ; x++) {
                    if(y-1 > 0 && image.getRGB(x,y-1) == oldColor){
                        stack.push(new Point(x,y-1));
                    }
                    if(y+1 < image.getHeight() && image.getRGB(x,y+1) == oldColor){
                        stack.push(new Point(x,y+1));
                    }
                }
            }
        }
    }

    public static void drawHex(Point center, int size, int width, BufferedImage image, int line, int lastLine) {
        size = size + Math.round(width / 2);
        Point[] insideCords = getHexagonCord(center.x, center.y, size);
        Graphics2D imGraphics = (Graphics2D) image.getGraphics();
        imGraphics.setColor(Color.BLACK);
        if (width != 1) {
            imGraphics.setStroke(new BasicStroke(width,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (line % 2 == 0) {
                for (int i = 0; i + 1 < 6; i++) {
                    imGraphics.draw(new Line2D.Float(insideCords[i].x, insideCords[i].y, insideCords[i + 1].x, insideCords[i + 1].y));
                }
                imGraphics.draw(new Line2D.Float(insideCords[0].x, insideCords[0].y, insideCords[5].x, insideCords[5].y));
            } else {
                imGraphics.draw(new Line2D.Float(insideCords[2].x, insideCords[2].y, insideCords[3].x, insideCords[3].y));
                imGraphics.draw(new Line2D.Float(insideCords[5].x, insideCords[5].y, insideCords[0].x, insideCords[0].y));
            }
            if (line == lastLine) {
                imGraphics.draw(new Line2D.Float(insideCords[0].x, insideCords[0].y, insideCords[1].x, insideCords[1].y));
                imGraphics.draw(new Line2D.Float(insideCords[1].x, insideCords[1].y, insideCords[2].x, insideCords[2].y));
            }
        }

        if (line % 2 == 0) {
            for (int i = 0; i + 1 < 6; i++) {
                drawBresenhamLine(insideCords[i], insideCords[i + 1], imGraphics);
            }
            drawBresenhamLine(insideCords[0], insideCords[5], imGraphics);
        } else {
            drawBresenhamLine(insideCords[2], insideCords[3], imGraphics);
            drawBresenhamLine(insideCords[5], insideCords[0], imGraphics);
        }
        if (line == lastLine) {
            drawBresenhamLine(insideCords[0], insideCords[1], imGraphics);
            drawBresenhamLine(insideCords[1], insideCords[2], imGraphics);
        }

    }

    private static Point[] getHexagonCord(int x, int y, int r) {
        Point[] points = new Point[6];
        for (int i = 0; i < 6; i++) {
            int angleDeg = 60 * i + 30;
            double angleRad = Math.PI / 180 * angleDeg;
            Point point = new Point((int) Math.round(x + r * Math.cos(angleRad)), (int) Math.round(y + r * Math.sin(angleRad)));
            points[i] = point;
        }
        return points;
    }
    private static int sign (int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }
    private static void drawBresenhamLine(Point start, Point end, Graphics g) {
        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = end.y;
        int dx=Math.abs(x1 - x2);
        int dy=Math.abs(y1 - y2);
        int sx=sign(x2-x1);
        int sy=sign(y2-y1);
        boolean isSwap = false;
        if (dy>dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
            isSwap = true;
        }
        int e = dy;
        for(int i = 0; i <= dx; i++) {
            g.drawLine(x1,y1,x1,y1);
            if (2 * e >= dx) {
                if (isSwap) {
                    x1 += sx;
                }
                else {
                    y1 += sy;
                }
                e -= dx;
            }
            if (isSwap) {
                y1 += sy;
            }else {
                x1 += sx;
            }
            e += dy;
        }
    }
}
