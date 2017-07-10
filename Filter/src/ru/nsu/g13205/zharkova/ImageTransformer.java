package ru.nsu.g13205.zharkova;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Yana on 11.03.16.
 */
public class ImageTransformer {

    public static final int SUPER_SAMPLING = 1;
    public static final int BILINEAR = 2;

    public static BufferedImage resize(BufferedImage image, int w, int h, int type){
        if(type == SUPER_SAMPLING){
            return SuperSampling(image, w, h);
        }
        if(type == BILINEAR){
            return resizeBilinear(image, w, h);
        }
        return null;
    }

    private static BufferedImage SuperSampling(BufferedImage inImage, int newWidth, int newHeight){
        int h = inImage.getHeight();
        int w = inImage.getWidth();
        if(newWidth<1) newWidth = inImage.getWidth();
        if(newHeight<1) newHeight = inImage.getHeight();
        BufferedImage outImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        double compressionFactorX = (double) inImage.getWidth() / newWidth;
        double compressionFactorY = (double) inImage.getHeight() / newHeight;
        for (int i = 0, yy = 0; i < inImage.getHeight() && yy < newHeight; i++, yy++) {
            double realStartY = i * compressionFactorY;
            double realEndY = (i+1) * compressionFactorY;
            int startY = (int)(i*compressionFactorY);
            int endY = (int) ((i+1)*compressionFactorY + ((i+1)*compressionFactorY-(int)((i+1)*compressionFactorY)>0?1:0));
            for (int j = 0, xx = 0; j < inImage.getWidth() && xx < newWidth; j++, xx++) {
                double realStartX = j * compressionFactorX;
                double realEndX = (j+1) * compressionFactorX;
                int startX = (int) (j*compressionFactorX);
                int endX = (int) ((j+1)*compressionFactorX + ((j+1)*compressionFactorX-(int)((j+1)*compressionFactorX)>0?1:0));
                double[][] areas = new double[endY - startY][endX - startX];
                fillMatrix(areas, 1d);
                if(realStartY != startY){
                    for (int k = 0; k < areas[0].length; k++) {
                        areas[0][k] = areas[0][k] - (realStartY - startY) * areas[0][k];
                    }
                }
                if(realEndY != endY){
                    for (int k = 0; k < areas[0].length; k++) {
                        areas[areas.length-1][k] = areas[areas.length-1][k] - (endY - realEndY) * areas[areas.length-1][k];
                    }
                }
                if(realStartX != startX){
                    for (int k = 0; k < areas.length; k++) {
                        areas[k][0] = areas[k][0] - (realStartX - startX) * areas[k][0];
                    }
                }
                if(realEndX != endX){
                    for (int k = 0; k < areas.length; k++) {
                        areas[k][areas[0].length-1] = areas[k][areas[0].length-1] - (endX - realEndX) * areas[k][areas[0].length-1];
                    }
                }
                double totalR = 0;
                double totalG = 0;
                double totalB = 0;
                double totalPixels = 0;
                for (int y = startY, m = 0; y < (endY>h?h:endY); y++, m++) {
                    for (int x = startX, n = 0; x < (endX>w?w:endX); x++, n++) {
                        Color c = new Color(inImage.getRGB(x, y));
                        totalR += c.getRed() * areas[m][n];
                        totalG += c.getGreen() * areas[m][n];
                        totalB += c.getBlue() * areas[m][n];
                        totalPixels+=areas[m][n];
                    }
                }
                int red = (int) (totalR/totalPixels);
                int green = (int) (totalG/totalPixels);
                int blue = (int) (totalB/totalPixels);
                red = red & 0xFF;
                green = green & 0xFF;
                blue = blue & 0xFF;
                outImage.setRGB(xx, yy, new Color(red, green, blue).getRGB());
            }
        }
        return outImage;
    }


    private static void fillMatrix(double[][] weight, double value) {
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                weight[i][j] = value;
            }
        }
    }

    private static BufferedImage resizeBilinear(BufferedImage inImage, int newWidth, int newHeight) {
        BufferedImage outImage = new BufferedImage(newWidth,newHeight, inImage.getType());
        int w = inImage.getWidth();
        int h = inImage.getHeight();
        int a, b, c, d, x, y;
        double compressionX = (double)(w)/newWidth ;
        double compressionY = (double)(h)/newHeight ;
        double realX, realY, blue, red, green ;
        for (int i=0;i<newHeight;i++) {
            for (int j=0;j<newWidth;j++) {
                x = (int) (compressionX * j);
                y = (int) (compressionY * i);
                realX = (compressionX * j) - x;
                realY = (compressionY * i) - y;
                a = inImage.getRGB(x, y);
                if(x+1>=inImage.getWidth()) b = inImage.getRGB(x, y);
                else b = inImage.getRGB(x + 1, y);
                if(y+1>=inImage.getHeight()) c = inImage.getRGB(x, y);
                else c = inImage.getRGB(x, y + 1);
                if(x+1>=inImage.getWidth() || y+1 >=inImage.getHeight()) d = inImage.getRGB(x, y);
                else d = inImage.getRGB(x + 1, y + 1);

                blue = (a&0xff)*(1-realX)*(1-realY)+(b&0xff)*(realX)*(1-realY)+(c&0xff)*(realY)*(1-realX)+(d&0xff)*(realX*realY);
                green = ((a>>8)&0xff)*(1-realX)*(1-realY) + ((b>>8)&0xff)*(realX)*(1-realY) + ((c>>8)&0xff)*(realY)*(1-realX) + ((d>>8)&0xff)*(realX*realY);
                red = ((a>>16)&0xff)*(1-realX)*(1-realY) + ((b>>16)&0xff)*(realX)*(1-realY) + ((c>>16)&0xff)*(realY)*(1-realX)+((d>>16)&0xff)*(realX*realY);
                outImage.setRGB(j, i, new Color((int) red, (int) green, (int) blue).getRGB());
            }
        }
        return outImage ;
    }
}
