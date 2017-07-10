package ru.nsu.fit.g13205.zharkova.view;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Yana on 14.02.16.
 */
public class FileLoader {
    private GameView gameView;
    private GridSettings gridSettings;

    public FileLoader(GameView gameView) {
        this.gameView = gameView;
        this.gridSettings = gameView.getGridSettings();
    }

    public void save(File file, GridPanel gridPanel) throws IOException {
        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
        out.println(gridPanel.getGridWidth() + " " + gridPanel.getGridHeight());
        out.println(gridSettings.getLineSize());
        out.println(gridSettings.getHexSize());
        out.println(gridPanel.getAliveCells().size());
        for(Point point: gridPanel.getAliveCells()){
            out.println(point.x + " " + point.y);
        }
        out.println();
        out.close();
        gridSettings.setChanges(false);
        gridSettings.setCurrentFile(file);
    }

    public void load(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] fieldSizeLine;
        int width;
        int height;
        int lineWidth;
        int cellSize;
        int aliveCellNumber;
        ArrayList<Point> aliveCells = new ArrayList<>();
        try {
            fieldSizeLine = parseTwo(getNextLine(br));
            width = Integer.parseInt(fieldSizeLine[0]);
            height = Integer.parseInt(fieldSizeLine[1]);
            lineWidth = Integer.parseInt(parseOne(getNextLine(br)));
            cellSize = Integer.parseInt(parseOne(getNextLine(br)));
            aliveCellNumber = Integer.parseInt(parseOne(getNextLine(br)));
            for (int i = 0; i < aliveCellNumber; i++) {
                String[] cords = parseTwo(getNextLine(br));
                int x = Integer.parseInt(cords[0]);
                int y = Integer.parseInt(cords[1]);
                if(x>=width || y>=height){
                    throw new IOException(x + " " + y);
                }
                aliveCells.add(new Point(y,x));
            }
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
        gridSettings.setLineSize(lineWidth);
        gridSettings.setHexSize(cellSize);
        gameView.fillBoard(width, height, aliveCells);
        gridSettings.setCurrentFile(file);
        gridSettings.setChanges(false);
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
