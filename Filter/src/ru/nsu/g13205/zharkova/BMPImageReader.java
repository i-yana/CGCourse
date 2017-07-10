package ru.nsu.g13205.zharkova;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Yana on 04.03.16.
 */
public class BMPImageReader {
    private File file;
    private int map[][];

    BMPImageReader(File file) {
        this.file = file;
    }

    public int[][] readBMP() throws FileBMPException {
        try {
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
            byte[] wb;
            byte[] hb;
            byte[] bfType = new byte[2];
            byte[] biSizeByte = new byte[4];
            byte[] biBitCount = new byte[2];
            byte[] biCompression = new byte[4];
            bis.read(bfType);
            String type = new String(bfType);
            if(!type.equals("BM")){
                throw new FileBMPException("Unknown file format");
            }
            bis.skip(12);
            bis.read(biSizeByte);
            int biSize = byteToint(biSizeByte);
            if(biSize == 12){
                wb = new byte[2];
                hb = new byte[2];
            }
            else {
                wb = new byte[4];
                hb = new byte[4];
            }
            bis.read(wb);
            bis.read(hb);
            int width = byteToint(wb);
            int height = byteToint(hb);
            bis.skip(2);
            bis.read(biBitCount);
            int bitOnPixelCount = byteToWord(biBitCount);

            if(bitOnPixelCount != 24){
                throw new FileBMPException("Expected 24 bitmap bit count");
            }
            bis.read(biCompression);
            if(byteToint(biCompression)!=0){
                throw new FileBMPException("Compressed file");
            }
            bis.skip(20);

            map=new int[height][width];
            int skip=4-width*3%4;
            if(height<0) {
                height = Math.abs(height);
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int blue = bis.read();
                        int green = bis.read();
                        int red = bis.read();
                        Color c = new Color(red, green, blue);
                        map[i][j] = c.getRGB();
                    }
                    if (skip != 4)
                        bis.skip(skip);
                }
            }
            else {
                for (int i = height-1; i >= 0; i--) {
                    for (int j = 0; j < width; j++) {
                        int blue = bis.read();
                        int green = bis.read();
                        int red = bis.read();
                        Color c = new Color(red, green, blue);
                        map[i][j] = c.getRGB();
                    }
                    if (skip != 4)
                        bis.skip(skip);
                }
            }
            bis.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public static  int byteToWord(byte b[])
    {
        int t1=(b[1]&0xff)<<8;
        int t2=b[0]&0xff;
        return t1+t2;
    }

    public static  int byteToint(byte b[])
    {
        int t1=(b[3]&0xff)<<24;
        int t2=(b[2]&0xff)<<16;
        int t3=(b[1]&0xff)<<8;
        int t4=b[0]&0xff;
        return t1+t2+t3+t4;
    }
}
