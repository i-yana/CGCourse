package ru.nsu.g13205.zharkova;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yana on 13.03.16.
 */
public class BMPImageWriter {

    private File file;

    public BMPImageWriter(File file) {
        this.file = file;
    }

    public void saveBMP(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int skip=4-width*3%4;
        byte[] offset = new byte[skip];
        byte[] reserved = new byte[]{0,0,0,0};
        try {
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
            bos.write('B');
            bos.write('M');
            int fileSize = (width*3+skip)*height + 54;
            bos.write(intTobyte(fileSize,4));
            bos.write(reserved);
            bos.write(intTobyte(54,4));
            bos.write(intTobyte(40,4));
            bos.write(intTobyte(width,4));
            bos.write(intTobyte(height,4));
            bos.write(intTobyte(1,2));
            bos.write(intTobyte(24,2));
            bos.write(intTobyte(0,4));
            bos.write(intTobyte((width*3+skip)*height,4));
            bos.write(intTobyte(0,4));
            bos.write(intTobyte(0,4));
            bos.write(intTobyte(0,4));
            bos.write(intTobyte(0,4));
            for(int i=height-1;i>=0;i--)
            {
                for(int j=0;j< width;j++)
                {
                    int rgb = image.getRGB(j,i);
                    bos.write((byte)(rgb & 0xFF));
                    bos.write((byte)(rgb >> 8 & 0xFF));
                    bos.write((byte)(rgb >> 16 & 0xFF));
                }
                if(skip!=4)
                    bos.write(offset);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] intTobyte(int a,int len)
    {
        byte []t=new byte[len];
        t[0]=(byte) ((a&0xff));
        if(len>1)
            t[1]=(byte)((a&0xff00)>>8);
        if(len>2)
            t[2]=(byte)((a&0xff0000)>>16);
        if(len>3)
            t[3]=(byte)((a&0xff000000)>>24);
        return t;
    }

    public static byte[] wordTobyte(int a,int len)
    {
        byte []t=new byte[len];
        t[0]=(byte) ((a&0xff));
        if(len>1)
            t[1]=(byte)((a&0xff00)>>8);
        return t;
    }
}
