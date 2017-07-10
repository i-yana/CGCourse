package ru.nsu.fit.g13205.zharkova;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Yana on 23.03.16.
 */
public class IzoFileReader {

    private File file;

    public IzoFileReader(File file){
        this.file = file;
    }

    public Properties readProperties() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] gridParam;
        String[] rgb;
        int k;
        int m;
        int level;
        int red, green, blue;
        Color isoColor;
        ArrayList<Color> colorMap = new ArrayList<Color>();
        try{
            gridParam = parseTwo(getNextLine(br));
            k = Integer.parseInt(gridParam[0]);
            m = Integer.parseInt(gridParam[1]);
            level = Integer.parseInt(parseOne(getNextLine(br)));
            for (int i = 0; i < level; i++) {
                rgb = parseThree(getNextLine(br));
                red = Integer.parseInt(rgb[0]);
                green = Integer.parseInt(rgb[1]);
                blue = Integer.parseInt(rgb[2]);
                Color color = new Color(red, green, blue);
                colorMap.add(color);
            }
            rgb = parseThree(getNextLine(br));
            red = Integer.parseInt(rgb[0]);
            green = Integer.parseInt(rgb[1]);
            blue = Integer.parseInt(rgb[2]);
            isoColor = new Color(red, green, blue);
        }catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return new Properties(k, m, level, colorMap, isoColor, new Boundary());
    }

    private String[] parseThree(String line) throws IOException {
        if(line.contains("//")){
            line = line.substring(0, line.indexOf("//"));
        }
        if(line.split(" ").length != 3){
            throw new IOException();
        }
        return line.split(" ");
    }

    private String[] parseTwo(String line) throws IOException {
        if(line.contains("//")){
            line = line.substring(0, line.indexOf("//"));
        }
        if(line.split(" ").length != 2){
            throw new IOException();
        }
        return line.split(" ");
    }

    private String getNextLine(BufferedReader br) throws Exception {
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

    private String parseOne(String line) throws IOException {
        if(line.contains("//")){
            line = line.substring(0, line.indexOf("//"));
        }
        line = line.trim();
        if(line.split(" ").length != 1){
            throw new IOException();
        }
        return line;
    }
}
