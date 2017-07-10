package ru.nsu.fit.g13205.zharkova.controller;

import ru.nsu.fit.g13205.zharkova.model.Cell;
import ru.nsu.fit.g13205.zharkova.model.Field;
import ru.nsu.fit.g13205.zharkova.model.ModelListener;
import ru.nsu.fit.g13205.zharkova.model.Properties;
import ru.nsu.fit.g13205.zharkova.view.GameView;
import ru.nsu.fit.g13205.zharkova.view.GridViewListener;

import java.util.ArrayList;


/**
 * Created by Yana on 21.02.16.
 */
public class GridController implements GridViewListener, ModelListener {

    private GameView view;
    private Field field;

    public GridController(){
        view = new GameView();
        view.subscribeOnChange(this);
        field = new Field();
        field.subscribeOnChanges(this);
        view.fillBoard(80,50,new ArrayList<>());
    }

    @Override
    public void handleChanges(int gridHeight, int gridWidth, Cell[][] cells, double[][] impacts) {
        field.setGrid(gridWidth, gridHeight, cells, impacts);
    }

    @Override
    public void handleUpdate() {
        field.update();
    }

    @Override
    public void changeProperty(Properties properties) {
        field.setProperties(properties);
    }

    @Override
    public void handleRecountQuery() {
        field.recountImpacts();
    }

    @Override
    public void handleUpdateComplete() {
        view.updateGrid();
    }

    public static void main(String[] args) {
        GridController gridController = new GridController();
    }
}
