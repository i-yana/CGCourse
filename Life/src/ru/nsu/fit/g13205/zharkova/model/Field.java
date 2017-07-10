package ru.nsu.fit.g13205.zharkova.model;

import ru.nsu.fit.g13205.zharkova.controller.GridController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yana on 11.02.16.
 */
public class Field implements ObservableModel{

    private double lifeBegin = DefaultSetting.DEFAULT_LIVE_BEGIN;
    private double lifeEnd = DefaultSetting.DEFAULT_LIVE_END;
    private double birthBegin = DefaultSetting.DEFAULT_BIRTH_BEGIN;
    private double birthEnd = DefaultSetting.DEFAULT_BIRTH_END;
    private double fstImpact = DefaultSetting.DEFAULT_FST_IMPACT;
    private double sndImpact = DefaultSetting.DEFAULT_SND_IMPACT;

    private int width;
    private int height;
    private Cell[][] field;
    private double[][] impacts;

    private List<ModelListener> listeners = new ArrayList<>();

    public Field(){
        this.width = 0;
        this.height = 0;
        field = null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int i, int j) {
        if(i < 0 || i >= height || j < 0 || j >= (i % 2 == 0 ? width : width - 1)){
            return null;
        }
        return field[i][j];
    }

    public int countFirstOrderNeighbors(int i, int j) {
        int nbrCount = 0;
        if(getCell(i + 1, j) == Cell.ALIVE){
            nbrCount++;
        }
        if(getCell(i, j - 1) == Cell.ALIVE){
            nbrCount++;
        }
        if(i % 2 == 0){
            if(getCell(i - 1, j - 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i - 1, j) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j - 1) == Cell.ALIVE){
                nbrCount++;
            }
        }
        else{
            if(getCell(i - 1, j) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i - 1, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
        }
        return nbrCount;
    }

    public int countSecondOrderNeighbors(int i, int j) {
        int nbrCount = 0;
        if(getCell(i - 2, j) == Cell.ALIVE){
            nbrCount++;
        }
        if(getCell(i + 2, j) == Cell.ALIVE) {
            nbrCount++;
        }
        if(i % 2 == 0){
            if(getCell(i - 1, j - 2) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i - 1, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j + 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j - 2) == Cell.ALIVE){
                nbrCount++;
            }
        }
        else{
            if(getCell(i - 1, j - 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i - 1, j + 2) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j - 1) == Cell.ALIVE){
                nbrCount++;
            }
            if(getCell(i + 1, j + 2) == Cell.ALIVE){
                nbrCount++;
            }
        }
        return nbrCount;
    }

    public void update(){

        int height = getHeight();
        int width = getWidth();
        if(height == 0 || width == 0){
            return;
        }
        Cell[][] temp = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < (i % 2 == 0 ? width : width - 1); j++) {
                double impact = impacts[i][j];
                if(field[i][j] == Cell.ALIVE){
                    if(lifeBegin <= impact && impact <= lifeEnd){
                        temp[i][j] = Cell.ALIVE;
                    }
                    else if(impact < lifeBegin){
                        temp[i][j] = Cell.DEAD;
                    }
                    else if(impact > lifeEnd){
                        temp[i][j] = Cell.DEAD;
                    }
                    else {
                        temp[i][j] = Cell.ALIVE;
                    }
                }
                if(field[i][j] == Cell.DEAD){
                    if(birthBegin <= impact && impact <= birthEnd){
                        temp[i][j] = Cell.ALIVE;
                    }
                    else{
                        temp[i][j] = Cell.DEAD;
                    }
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = temp[i][j];
            }
        }
        notifyAboutUpdateComplete();
    }

    @Override
    public void notifyAboutUpdateComplete() {
        for(ModelListener listener: listeners){
            listener.handleUpdateComplete();
        }
    }

    public void subscribeOnChanges(GridController gridController) {
        listeners.add(gridController);
    }

    public void setProperties(Properties properties) {
        this.lifeBegin = properties.getLiveBegin();
        this.lifeEnd = properties.getLiveEnd();
        this.birthBegin = properties.getBirthBegin();
        this.birthEnd = properties.getBirthEnd();
        this.fstImpact = properties.getFstImpact();
        this.sndImpact = properties.getSndImpact();
    }

    public void setGrid(int gridWidth, int gridHeight, Cell[][] cells, double[][] impacts) {
        this.width = gridWidth;
        this.height = gridHeight;
        this.field = cells;
        this.impacts = impacts;

    }

    public void recountImpacts() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < (i % 2 == 0 ? width : width - 1); j++) {
                int fstCount = countFirstOrderNeighbors(i, j);
                int sndCount = countSecondOrderNeighbors(i, j);
                double tmp = fstImpact * fstCount + sndImpact * sndCount;
                double impact = new BigDecimal(tmp).setScale(1, RoundingMode.HALF_UP).doubleValue();
                impacts[i][j] = impact;
            }
        }
    }
}
