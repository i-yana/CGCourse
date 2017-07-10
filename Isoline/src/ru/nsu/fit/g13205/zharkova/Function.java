package ru.nsu.fit.g13205.zharkova;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 23.03.16.
 */
public interface Function {

    public Cord getCord(int u, int v);

    public Point getPixel(double x, double y);

    public double getZ(Cord funcCord) throws IOException;

    public double getMin();

    public double getMaxY();

    public ArrayList<Double> getLevels();

    public Cord[][] getFunctionGrid();

    public Point[][] getPixelGrid();
}
