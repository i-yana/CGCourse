package ru.nsu.fit.g13205.zharkova.view;


import ru.nsu.fit.g13205.zharkova.model.Cell;
import ru.nsu.fit.g13205.zharkova.model.Properties;

/**
 * Created by Yana on 21.02.16.
 */
public interface GridViewListener {
    void handleChanges(int gridHeight, int gridWidth, Cell[][] cells, double[][] impacts);

    void handleUpdate();

    void changeProperty(Properties properties);

    void handleRecountQuery();
}
