package ru.nsu.g13205.zharkova;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Yana on 07.03.16.
 */
public class Filter {
    public static BufferedImage grayScale(BufferedImage inImage) throws NullPointerException{
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        int r, g, b, rgb;
        for(int y = 0; y < inImage.getHeight(); y++){
            for (int x = 0; x < inImage.getWidth(); x++) {
                rgb = inImage.getRGB(x,y);
                r = (int)(0.299 * (rgb >> 16 & 0xff));
                g = (int)(0.587 * (rgb >> 8 & 0xff));
                b = (int)(0.114 * (rgb & 0xff));
                rgb = (r+g+b) << 16 | (r+g+b) << 8 | (r+g+b);
                outImage.setRGB(x, y, rgb);
            }
        }
        return outImage;
    }

    public static BufferedImage negativeScale(BufferedImage inImage) throws NullPointerException{
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        int rgb,r,g,b;
        for(int y = 0; y < inImage.getHeight(); y++){
            for (int x = 0; x < inImage.getWidth(); x++) {
                rgb = inImage.getRGB(x,y);
                r = 255 - (rgb >> 16 & 0xff);
                g = 255 - (rgb >> 8 & 0xff);
                b = 255 - (rgb & 0xff);
                rgb = r << 16 | g << 8 | b;
                outImage.setRGB(x, y, rgb);
            }
        }
        return outImage;
    }


    private static BufferedImage applyСonvolution(double[][] matrix, double div, int offset, BufferedImage inImage){
        BufferedImage temp = createBorder(inImage);
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        for (int y = 1; y < temp.getHeight()-1; y++) {
            for (int x = 1; x < temp.getWidth()-1; x++) {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix.length; j++) {
                        int pixelPosX = x + (i - (matrix.length / 2));
                        int pixelPosY = y + (j - (matrix.length / 2));
                        if ((pixelPosX < 0) || (pixelPosX >= temp.getWidth()) || (pixelPosY < 0) || (pixelPosY >= temp.getHeight()))
                            continue;
                        Color c = new Color(temp.getRGB(pixelPosX, pixelPosY));
                        int r = c.getRed();
                        int g = c.getGreen();
                        int b = c.getBlue();

                        double kernelVal = matrix[i][j];

                        totalR += r * kernelVal;
                        totalG += g * kernelVal;
                        totalB += b * kernelVal;

                    }
                }
                if (div <= 0) div = 1;

                totalR /= div;
                totalR += offset;
                if (totalR < 0) totalR = 0;
                if (totalR > 255) totalR = 255;

                totalG /= div;
                totalG += offset;
                if (totalG < 0) totalG = 0;
                if (totalG > 255) totalG = 255;

                totalB /= div;
                totalB += offset;
                if (totalB < 0) totalB = 0;
                if (totalB > 255) totalB = 255;
                outImage.setRGB(x-1, y-1, new Color(totalR, totalG, totalB).getRGB());
            }
        }
        return outImage;
    }

    private static BufferedImage createBorder(BufferedImage inImage) {
        BufferedImage outImage = new BufferedImage(inImage.getWidth()+2, inImage.getHeight()+2, inImage.getType());
        outImage.setRGB(0,0,inImage.getRGB(0,0));
        outImage.setRGB(outImage.getWidth()-1, outImage.getHeight()-1, inImage.getRGB(inImage.getWidth()-1, inImage.getHeight()-1));
        outImage.setRGB(0, outImage.getHeight()-1, inImage.getRGB(0, inImage.getHeight()-1));
        outImage.setRGB(outImage.getWidth()-1, 0, inImage.getRGB(inImage.getWidth()-1, 0));
        for (int x = 0; x < inImage.getWidth(); x++) {
            outImage.setRGB(x+1,0, inImage.getRGB(x,0));
        }
        for (int x = 0; x < inImage.getWidth(); x++) {
            outImage.setRGB(x+1,outImage.getHeight()-1, inImage.getRGB(x,inImage.getHeight()-1));
        }
        for (int y = 0; y < inImage.getHeight(); y++) {
            outImage.setRGB(0,y+1, inImage.getRGB(0,y));
        }
        for (int y = 0; y < inImage.getHeight(); y++) {
            outImage.setRGB(outImage.getWidth()-1,y+1, inImage.getRGB(inImage.getWidth()-1,y));
        }
        for (int y = 0; y < inImage.getHeight(); y++) {
            for (int x = 0; x < inImage.getWidth(); x++) {
                outImage.setRGB(x+1, y+1, inImage.getRGB(x,y));
            }
        }
        return outImage;
    }

    public static BufferedImage selectOutline(BufferedImage inImage, int threshold){
        BufferedImage tempImege = grayScale(inImage);
        double[][] matrix = new double[][]
                {
                        {
                                0,-1,0
                        },
                        {
                                -1,4,-1
                        },
                        {
                                0,-1,0
                        }

                };
        tempImege = applyСonvolution(matrix, 1,0, tempImege);
        for (int y = 0; y < tempImege.getHeight(); y++) {
            for (int x = 0; x < tempImege.getWidth(); x++) {
                int rgb = tempImege.getRGB(x,y);
                int red = (rgb >>16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                if(blue > threshold){
                    blue= 255;
                }
                else {
                    blue = 0;
                }
                if(green>threshold){
                    green = 255;
                }
                else {
                    green = 0;
                }
                if(red>threshold){
                    red = 255;
                }
                else {
                    red = 0;
                }
                rgb = red<<16|green<<8|blue;
                tempImege.setRGB(x,y,rgb);
            }
        }
        return tempImege;
    }


    public static BufferedImage sharpnessFilter(BufferedImage inImage){
        double[][] sharpnessMatrix = new double[][]
                {
                        {
                                0,-1,0
                        },
                        {
                                -1,5,-1
                        },
                        {
                                0,-1,0
                        }

                };
        return applyСonvolution(sharpnessMatrix, 1,0, inImage);
    }

    public static BufferedImage embossFilter(BufferedImage inImage){
        double[][] matrix = new double[][]
                {
                        {
                                0,1,0
                        },
                        {
                                -1,0,1
                        },
                        {
                                0,-1,0
                        }

                };
        BufferedImage outImage =  grayScale(inImage);
        outImage = applyСonvolution(matrix, 1, 128, outImage);
        return outImage;
    }

    public static BufferedImage robertsFilter(BufferedImage inImage, int threshold){
        BufferedImage tempImage = createBorder(Filter.grayScale(inImage));
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(),inImage.getType());
        for (int i = 1; i < tempImage.getHeight()-1; i++) {
            for (int j = 1; j < tempImage.getWidth()-1; j++) {
                if(j+1 >= tempImage.getWidth() || i+1 >= tempImage.getHeight()){
                    continue;
                }
                Color a = new Color(tempImage.getRGB(j,i));
                Color b = new Color(tempImage.getRGB(j+1,i+1));
                Color c = new Color(tempImage.getRGB(j+1, i));
                Color d = new Color(tempImage.getRGB(j, i+1));
                int r1 = Math.abs(a.getRed() - b.getRed());
                int g1 = Math.abs(a.getGreen() - b.getGreen());
                int b1 = Math.abs(a.getBlue() - b.getBlue());
                int r2 = Math.abs(c.getRed() - d.getRed());
                int g2 = Math.abs(c.getGreen() - d.getGreen());
                int b2 = Math.abs(c.getBlue() - d.getBlue());
                int red = (int)Math.sqrt(r1*r1 + r2*r2);
                int green = (int)Math.sqrt(g1*g1 + g2*g2);
                int blue = (int)Math.sqrt(b1*b1 + b2*b2);
                if(blue > threshold){
                    blue = 255;
                }
                else{
                    blue = 0;
                }
                if(green>threshold){
                    green = 255;
                }
                else {
                    green = 0;
                }
                if(red>threshold){
                    red = 255;
                }
                else {
                    red = 0;
                }


                outImage.setRGB(j-1,i-1, new Color(red,green, blue).getRGB());
            }
        }
        return outImage;
    }

    public static BufferedImage sobelFilter(BufferedImage inImage, int threshold){
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(),inImage.getType());
        BufferedImage tempImage = createBorder(grayScale(inImage));
        double[][] vertical = new double[][]{
                {
                        -1,-2,-1
                },
                {
                        0,0,0
                },
                {
                        1,2,1
                }
        };
        double[][] horizontal = new double[][]{
                {
                        -1,0,1
                },
                {
                        -2,0,2
                },
                {
                        -1,0,1
                }
        };
        int matrixSize = 3;
        double ratio = 0.25;
        for (int y = 1; y < tempImage.getHeight()-1; y++) {
            for (int x = 1; x < tempImage.getWidth()-1; x++) {
                int r1 = 0;
                int g1 = 0;
                int b1 = 0;
                int r2 = 0;
                int g2 = 0;
                int b2 = 0;
                for (int i = 0; i < matrixSize; i++) {
                    for (int j = 0; j < matrixSize; j++) {
                        int pixelPosX = x + (i - (matrixSize / 2));
                        int pixelPosY = y + (j - (matrixSize / 2));
                        if ((pixelPosX < 0) || (pixelPosX >= tempImage.getWidth()) || (pixelPosY < 0) || (pixelPosY >= tempImage.getHeight()))
                            continue;
                        Color c = new Color(tempImage.getRGB(pixelPosX, pixelPosY));
                        int r = c.getRed();
                        int g = c.getGreen();
                        int b = c.getBlue();

                        double kernelValHorizontal = horizontal[i][j];
                        double kernelValVertical = vertical[i][j];

                        r1 += ratio * r * kernelValHorizontal;
                        g1 += ratio * g * kernelValHorizontal;
                        b1 += ratio * b * kernelValHorizontal;

                        r2 += ratio * r * kernelValVertical;
                        g2 += ratio * g * kernelValVertical;
                        b2 += ratio * b * kernelValVertical;
                    }
                }
                int totalR = (int) Math.round(Math.sqrt(r1 * r1 + r2 * r2));
                int totalG = (int) Math.round(Math.sqrt(g1 * g1 + g2 * g2));
                int totalB = (int) Math.round(Math.sqrt(b1 * b1 + b2 * b2));

                if(totalR > threshold){
                    totalR = 255;
                }
                else {
                    totalR = 0;
                }
                if(totalG>threshold){
                    totalG = 255;
                }
                else {
                    totalG = 0;
                }
                if(totalB>threshold){
                    totalB = 255;
                }
                else {
                    totalB = 0;
                }
                outImage.setRGB(x-1, y-1, new Color(totalR,totalG,totalB).getRGB());
            }
        }
        return outImage;
    }

    public static BufferedImage blur(BufferedImage inImage){
        double[][] matrix = new double[][]{
                {
                        0,1,0
                },
                {
                        1,2,1
                },
                {
                        0,1,0
                }
        };
        return applyСonvolution(matrix, 6,0, inImage);
    }

    public static BufferedImage mediansBlur(BufferedImage inImage){
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        BufferedImage temp = createBorder(inImage);
        for (int y = 1; y < temp.getHeight()-1; y++) {
            for (int x = 1; x < temp.getWidth()-1; x++) {
                ArrayList<Color> list = new ArrayList<>();
                for (int i = y-2; i <= y+2; i++) {
                    for (int j = x-2; j <= x+2; j++) {
                        try {
                            list.add(new Color(temp.getRGB(j, i)));
                        }catch (ArrayIndexOutOfBoundsException ignored){}
                    }
                }
                list.sort(new Comparator<Color>() {
                    @Override
                    public int compare(Color o1, Color o2) {
                        int first = o1.getRed() + o1.getGreen() + o1.getBlue();
                        int second = o2.getRed() + o2.getGreen() + o2.getBlue();
                        return first>second?-1:first<second?1:0;
                    }
                });
                Color newPixel;
                if(list.size() == 25){
                    newPixel = list.get(13);
                }
                else {
                    newPixel = list.get(list.size()/2);
                }
                outImage.setRGB(x-1,y-1,newPixel.getRGB());
            }
        }
        return outImage;
    }

    public static BufferedImage aquaFilter(BufferedImage inImage) {
        BufferedImage outImage = mediansBlur(inImage);
        outImage = sharpnessFilter(outImage);
        return outImage;
    }

    public static BufferedImage gammaCorrection(BufferedImage inImage, double gamma){
        BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), inImage.getType());
        gamma = 1./gamma;
        int r,g,b;
        for (int y = 0; y < inImage.getHeight(); y++) {
            for (int x = 0; x < inImage.getWidth(); x++) {
                Color rgb = new Color(inImage.getRGB(x,y));
                r = rgb.getRed();
                g = rgb.getGreen();
                b = rgb.getBlue();
                r = (int) (255 * (Math.pow((double) r / (double) 255, gamma)));
                g = (int) (255 * (Math.pow((double) g / (double) 255, gamma)));
                b = (int) (255 * (Math.pow((double) b / (double) 255, gamma)));
                outImage.setRGB(x,y, new Color(r,g,b).getRGB());
            }
        }
        return outImage;
    }
}
