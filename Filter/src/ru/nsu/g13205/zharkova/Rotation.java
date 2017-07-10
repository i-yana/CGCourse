package ru.nsu.g13205.zharkova;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Yana on 14.03.16.
 */
public class Rotation {

    public static BufferedImage turn(BufferedImage image, int degree) {
        double rad = degree * Math.PI / 180;
        int x0 = image.getWidth() / 2;
        int y0 = image.getHeight() / 2;
        int size = (int) Math.sqrt(image.getHeight()*image.getHeight() + image.getWidth()*image.getWidth());
        if(size>350){
            size = 350;
        }
        BufferedImage outImage = new BufferedImage(size, size, image.getType());
        for (int y = 0; y < outImage.getHeight(); y++)
            for (int x = 0; x < outImage.getWidth(); x++) {
                double realX = x0 + (x - x0) * Math.cos(rad) - (y - y0) * Math.sin(rad);
                double realY = y0 + (y - y0) * Math.cos(rad) + (x - x0) * Math.sin(rad);
                int xx = (int) Math.round(realX);
                int yy = (int) Math.round(realY);
                if (xx >= image.getWidth() || xx < 0 || yy >= image.getHeight() || yy < 0) {
                    outImage.setRGB(x, y, Color.white.getRGB());
                } else {
                    outImage.setRGB(x, y, image.getRGB(xx, yy));
                }
            }
        return outImage;
    }
}
