package ru.nsu.g13205.zharkova;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Yana on 07.03.16.
 */
public class Dithering {
    public static BufferedImage floydSteinbergDithering(BufferedImage inImage, int redPaletteNum, int greenPaletteNum, int bluePaletteNum) {
        int[] redPalette = new int[redPaletteNum];
        int[] greenPalette = new int[greenPaletteNum];
        int[] bluePalette = new int[bluePaletteNum];
        fillPalette(redPalette);
        fillPalette(greenPalette);
        fillPalette(bluePalette);

        Color[][] pixels = new Color[inImage.getHeight()][inImage.getWidth()];
        for (int i = 0; i < inImage.getHeight(); i++) {
            for (int j = 0; j < inImage.getWidth(); j++) {
                pixels[i][j] = new Color(inImage.getRGB(j,i));

            }
        }

        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        for (int i = 0; i < inImage.getHeight(); i++) {

                if(i%2==0) {
                    for (int j = 0; j < inImage.getWidth(); j++) {
                        Color oldColor = pixels[i][j];
                        Color newColor = findClosestPaletteColor(oldColor, redPalette, greenPalette, bluePalette);
                        outImage.setRGB(j, i, newColor.getRGB());

                        if (j + 1 < inImage.getWidth())
                            pixels[i][j + 1] = operation(pixels[i][j + 1], oldColor, newColor, 7. / 16.);
                        if (j - 1 >= 0 && i + 1 < inImage.getHeight())
                            pixels[i + 1][j - 1] = operation(pixels[i + 1][j - 1], oldColor, newColor, 3. / 16.);
                        if (i + 1 < inImage.getHeight())
                            pixels[i + 1][j] = operation(pixels[i + 1][j], oldColor, newColor, 5. / 16.);
                        if (j + 1 < inImage.getWidth() && i + 1 < inImage.getHeight())
                            pixels[i + 1][j + 1] = operation(pixels[i + 1][j + 1], oldColor, newColor, 1. / 16.);
                    }
                }
                else{
                    for (int j = inImage.getWidth()-1; j >= 0; j--) {
                        Color oldColor = pixels[i][j];
                        Color newColor = findClosestPaletteColor(oldColor, redPalette, greenPalette, bluePalette);
                        outImage.setRGB(j, i, newColor.getRGB());

                        if (j - 1 > 0)
                            pixels[i][j - 1] = operation(pixels[i][j - 1], oldColor, newColor, 7. / 16.);
                        if (j + 1 < inImage.getWidth() && i + 1 < inImage.getHeight())
                            pixels[i + 1][j + 1] = operation(pixels[i + 1][j + 1], oldColor, newColor, 3. / 16.);
                        if (i + 1 < inImage.getHeight())
                            pixels[i + 1][j] = operation(pixels[i + 1][j], oldColor, newColor, 5. / 16.);
                        if (j - 1 >= 0 && i + 1 < inImage.getHeight())
                            pixels[i + 1][j - 1] = operation(pixels[i + 1][j - 1], oldColor, newColor, 1. / 16.);
                    }
                }
            }

        return outImage;
    }
    private static Color operation(Color c, Color old, Color newColor, double d){
        int r = format((int) ((old.getRed() - newColor.getRed()) * d) + c.getRed());
        int g = format((int) ((old.getGreen() - newColor.getGreen()) * d) + c.getGreen());
        int b = format((int) ((old.getBlue() - newColor.getBlue()) * d) + c.getBlue());
        return new Color(r,g,b);
    }

    private static int format(int c){
        return Math.max(0, Math.min(255, c));
    }

    private static Color findClosestPaletteColor(Color oldColor, int[] redPalette, int[] greenPalette, int[] bluePalette) {
        int redComponent = oldColor.getRed();
        int greenComponent = oldColor.getGreen();
        int blueComponent = oldColor.getBlue();
        int closestRed = Math.abs(redPalette[0] - redComponent);
        int redIndex = 0;
        for (int i = 1; i < redPalette.length; i++) {
            if(Math.abs(redComponent - redPalette[i]) < closestRed){
                closestRed = Math.abs(redComponent - redPalette[i]);
                redIndex = i;
            }
        }

        int closestGreen = Math.abs(greenPalette[0] - greenComponent);
        int greenIndex = 0;
        for (int i = 1; i < greenPalette.length; i++) {
            if(Math.abs(greenComponent - greenPalette[i]) < closestGreen){
                closestGreen = Math.abs(greenComponent - greenPalette[i]);
                greenIndex = i;
            }
        }

        int closestBlue = Math.abs(bluePalette[0] - blueComponent);
        int blueIndex = 0;
        for (int i = 1; i < bluePalette.length; i++) {
            if(Math.abs(blueComponent - bluePalette[i]) < closestBlue){
                closestBlue = Math.abs(blueComponent - bluePalette[i]);
                blueIndex = i;
            }
        }
        return new Color(redPalette[redIndex], greenPalette[greenIndex], bluePalette[blueIndex]);
    }

    private static void fillPalette(int[] palette) {
        palette[0] = 0;
        int step = 255/(palette.length-1);
        for (int color = step, i = 1; i< palette.length; color+=step, i++) {
            palette[i] = color;
        }
    }

    public static BufferedImage orderedDithering(BufferedImage inImage, int redPaletteNum, int greenPaletteNum, int bluePaletteNum) {
        int[] redPalette = new int[redPaletteNum];
        int[] greenPalette = new int[greenPaletteNum];
        int[] bluePalette = new int[bluePaletteNum];
        fillPalette(redPalette);
        fillPalette(greenPalette);
        fillPalette(bluePalette);
        int[][] matrix = {
                {
                        1, 9, 3, 11
                }
                ,
                {
                        13, 5, 15, 7
                }
                ,
                {
                        4, 12, 2, 10
                }
                ,
                {
                        16, 8, 14, 6
                }
        };

        double ratioR = 255./(redPaletteNum -1);
        double ratioG = 255./(greenPaletteNum-1);
        double ratioB = 255./(bluePaletteNum-1);
        double factor = 1./17;
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        for (int y = 0; y < inImage.getHeight(); y++) {
            for (int x = 0; x < inImage.getWidth(); x++) {
                Color oldPixel = new Color(inImage.getRGB(x, y));
                int red = oldPixel.getRed();
                int green = oldPixel.getGreen();
                int blue = oldPixel.getBlue();
                if(red < (int)(ratioR*matrix[y%4][x%4] * factor)){
                    red = (int) (red - ratioR/2);
                }
                else {
                    red = (int) (red + ratioR/2);
                }

                if(green < (int)(ratioG*matrix[y%4][x%4] * factor)){
                    green = (int) (green - ratioG/2);
                }
                else {
                    green = (int) (green + ratioG/2);
                }

                if(blue < (int)(ratioB*matrix[y%4][x%4] * factor)){
                    blue = (int) (blue - ratioB/2);
                }
                else {
                    blue = (int) (blue + ratioB/2);
                }
                if(red>255) red = 255;
                if(red<0) red = 0;
                if(green>255) green = 255;
                if(green<0) green = 0;
                if(blue>255) blue = 255;
                if(blue<0) blue = 0;

                Color value = new Color(red, green, blue);
                Color newPixel = findClosestPaletteColor(value, redPalette, greenPalette, bluePalette);
                outImage.setRGB(x, y, newPixel.getRGB());
            }
        }
        return outImage;
    }
}
