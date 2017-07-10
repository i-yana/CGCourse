package ru.nsu.fit.g13205.zharkova.view;

import ru.nsu.fit.g13205.zharkova.model.Cell;
import ru.nsu.fit.g13205.zharkova.model.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yana on 14.02.16.
 */
public class GridPanel extends JPanel implements ObservableView {

    public static final Color BORDER_COLOR = Color.BLACK;
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color ALIVE_COLOR = Color.YELLOW;
    public static final Color DEAD_COLOR = Color.PINK;

    private List<GridViewListener> listeners = new ArrayList<>();
    private MouseHandler mouseHandler;

    private Properties properties;
    private GridSettings gridSettings;
    private int gridWidth;
    private int gridHeight;
    private HashMap<Point, Point> cells;
    private Cell[][] cellField;
    private int pixelAreaWidth;
    private int pixelAreaHeight;
    private BufferedImage image;
    private double[][] impacts;

    public GridPanel(GridSettings gridSettings){
        super(new BorderLayout());
        setBackground(Color.white);
        mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        this.gridWidth = 0;
        this.gridHeight = 0;
        this.gridSettings = gridSettings;
        this.cells = new HashMap<>();
        this.properties = Properties.DEFAULT_SETTINGS;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isEmpty()){
            return;
        }
        g.drawImage(image, 0, 0, null);
    }

    public void createNewGrid(int gridWidth, int gridHeight, ArrayList<Point> aliveCells) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cells = new HashMap<>();
        calculateCenters();
        calculatePixelAreaSize();
        setPreferredSize(new Dimension(pixelAreaWidth, pixelAreaHeight));
        this.cellField = new Cell[gridHeight][gridWidth];
        this.impacts = new double[gridHeight][gridWidth];
        fillGrid(aliveCells);
        createImage();
        notifyAboutGridChanges();
        recountImpact();
        paintImpact();
    }

    private void createImage(){
        BufferedImage image = new BufferedImage(pixelAreaWidth,pixelAreaHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, pixelAreaWidth, pixelAreaHeight);
        for(Point point: cells.keySet()){
            Graphic.drawHex(cells.get(point), gridSettings.getHexSize(), gridSettings.getLineSize(), image, point.x, gridHeight-1);
        }
        for(Point point: cells.keySet()){
            Graphic.fill(cells.get(point), cellField[point.x][point.y] == Cell.DEAD ? DEAD_COLOR : ALIVE_COLOR, image);
        }
        this.image = image;
    }

    public void updateImage() {
        for(Point point: cells.keySet()){
            Graphic.fill(cells.get(point), cellField[point.x][point.y] == Cell.DEAD ? DEAD_COLOR : ALIVE_COLOR, image);
        }
        recountImpact();
        paintImpact();
        repaint();
    }

    private void calculatePixelAreaSize(){
        int size = gridSettings.getHexSize()+Math.round(gridSettings.getLineSize()/2);
        this.pixelAreaHeight = Math.round((size + size / 2) * gridHeight + 2 * gridSettings.getLineSize() + size/2);
        int steps = gridWidth;
        int x = 0;
        for (int i = 0; i < steps; i++) {
            x = (int) Math.round(x + size * Math.sqrt(3)/2);
            x = (int) Math.round(x + size * Math.sqrt(3)/2);
        }
        this.pixelAreaWidth = x + gridSettings.getLineSize();
    }

    private void calculateCenters(){
        cells.clear();
        int lineSize = gridSettings.getLineSize();
        int size = gridSettings.getHexSize()+Math.round(lineSize/2);
        int startX = Math.round(Math.round(lineSize/2+Math.sqrt(3)/2 * size));
        int y = Math.round(lineSize/2) + size;
        int x;
        for (int i = 0; i < gridHeight; i++) {
            x = i%2==0?startX: Math.round(Math.round(startX + Math.sqrt(3) / 2 * size));
            for (int j = 0; j < (i%2==0?gridWidth:gridWidth-1); j++) {
                cells.put(new Point(i,j), new Point(x,y));
                x = (int) Math.round(x + size * Math.sqrt(3)/2);
                x = (int) Math.round(x + size * Math.sqrt(3)/2);
            }
            y = Math.round(y + size+size/2);
        }
    }

    public int getGridWidth(){
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public boolean isEmpty() {
        return gridHeight==0 || gridWidth==0;
    }

    @Override
    public void notifyAboutGridChanges() {
        for (GridViewListener listener: listeners){
            listener.handleChanges(gridHeight, gridWidth, cellField, impacts);
        }
    }

    @Override
    public void notifyAboutUpdate() {
        eraseImpact();
        recountImpact();
        for (GridViewListener listener: listeners){
            listener.handleUpdate();
        }
    }

    public void setFocusable(boolean focusable){
        if(!focusable) {
            removeMouseListener(mouseHandler);
            removeMouseMotionListener(mouseHandler);
        }
        else {
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }
    }
    @Override
    public void notifyAboutPropertiesChanges() {
        eraseImpact();
        for (GridViewListener listener: listeners){
            listener.changeProperty(properties);
        }
        recountImpact();
        paintImpact();
        repaint();
    }

    public void subscribeToChange(GridViewListener listener) {
        listeners.add(listener);
    }

    public ArrayList<Point> getAliveCells() {
        ArrayList<Point> aliveCells = new ArrayList<>();
        for (int i = 0; i < gridHeight; i++){
            for(int j = 0; j < (i % 2 == 0 ? gridWidth : gridWidth - 1); j++){
                if(cellField[i][j] == Cell.ALIVE){
                    aliveCells.add(new Point(j,i));
                }
            }
        }
        return new ArrayList<>(aliveCells);
    }


    private void tst(int x, int y){
        int lineSize = gridSettings.getLineSize();
        int size = gridSettings.getHexSize()+Math.round(lineSize/2);
        int segmentWidth = (int) Math.round(size * Math.sqrt(3)/2) + (int) Math.round(size * Math.sqrt(3)/2);
        int segmentHeight = (int) Math.round(size * 1.5);
        int nSegmX = x/segmentWidth;
        int nSegmY = y/segmentHeight;
        System.out.println("segment's number " + nSegmY + " " + nSegmX);
        int sectPixX = x%segmentWidth;
        int sectPixY = y%segmentHeight;
        System.out.println("pixel "+ sectPixX + " " + sectPixY);
        boolean sectType;
        if(nSegmY % 2 == 0){
            sectType = true;
        }
        else {
            sectType = false;
        }
        System.out.println(size + " " + segmentWidth);
        double k = Math.tan(Math.toRadians(30));
        System.out.println("k="+k);
        if(sectType){
            if(sectPixY<(-k*sectPixX + (double)size/2)){
                System.out.println("yes left");
                System.out.println(-k*sectPixX + (double)size/2);
                nSegmY--;
                nSegmX--;
            }
            if(sectPixY<(k*sectPixX -(double)size/2)){
                System.out.println("yes right");
                System.out.println(k*sectPixX -(double)size/2);
                nSegmY--;
            }
        }
        else {
            if(sectPixX > (double)segmentWidth/2){
                if(sectPixY < size - k * sectPixX){
                    nSegmY--;
                }

            }
            else if(sectPixY < k * sectPixX) {
                nSegmY--;
            }
            else {
                nSegmX--;
            }

        }
        System.out.println(nSegmY + " " + nSegmX);
    }

    private Point calculateNearestCenter(int x, int y){

        int lineSize = gridSettings.getLineSize();
        int size = gridSettings.getHexSize()+Math.round(lineSize/2);
        int segmentWidth = (int) Math.round(size * Math.sqrt(3)/2) + (int) Math.round(size * Math.sqrt(3)/2);
        int segmentHeight = (int) Math.round(size * 1.5);
        int nSegmX = x/segmentWidth;
        int nSegmY = y/segmentHeight;
        int sectPixX = x%segmentWidth;
        int sectPixY = y%segmentHeight;
        boolean sectType = nSegmY % 2 == 0;
        double k = Math.tan(Math.toRadians(30));
        if(sectType){
            if(sectPixY<(-k*sectPixX + (double)size/2+(double)lineSize/2)){
                nSegmY--;
                nSegmX--;
            }
            if(sectPixY<(k*sectPixX -Math.round(size/2))){
                nSegmY--;
            }
        }
        else {
            if(sectPixX > (double)(segmentWidth/2)){
                if(sectPixY < size - k * sectPixX+(double)lineSize/2){
                    nSegmY--;
                }

            }
            else if(sectPixY < k * sectPixX+(double)lineSize/2) {
                nSegmY--;
            }
            else {
                nSegmX--;
            }

        }
        return new Point(nSegmY, nSegmX);
    }

    public void setSetting(int width, int height) {
        if (isEmpty()) {
            createNewGrid(width, height, new ArrayList<>());
            return;
        }
        int oldWidth = gridWidth;
        int oldHeight = gridHeight;
        this.gridWidth = width;
        this.gridHeight = height;
        calculateCenters();
        calculatePixelAreaSize();
        setPreferredSize(new Dimension(pixelAreaWidth, pixelAreaHeight));

        Cell[][] tmp = new Cell[gridHeight][gridWidth];
        this.impacts = new double[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < (i % 2 == 0 ? gridWidth : gridWidth - 1); j++) {
                try {
                    if (i < oldHeight && j < (i % 2 == 0 ? oldWidth : oldWidth - 1)) {
                        tmp[i][j] = cellField[i][j];
                    } else {
                        tmp[i][j] = Cell.DEAD;
                    }
                } catch (Exception e) {
                    tmp[i][j] = Cell.DEAD;
                }
            }
        }
        this.cellField = tmp;
        notifyAboutGridChanges();
        createImage();
        recountImpact();
        paintImpact();
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        notifyAboutPropertiesChanges();
    }

    public Properties getProperties() {
        return properties;
    }

    public void fillGrid(ArrayList<Point> aliveCells) {
        if(image != null) {
            eraseImpact();
        }
        for (int i = 0; i < gridHeight; i++){
            for(int j = 0; j < (i % 2 == 0 ? gridWidth : gridWidth - 1); j++){
                cellField[i][j] = Cell.DEAD;
            }
        }
        for(Point point: aliveCells){
            cellField[point.x][point.y] = Cell.ALIVE;
        }
    }

    public void eraseImpact(){
        if(gridSettings.isImpact() && gridSettings.getHexSize()>=30 && image != null) {
            Graphics g = image.getGraphics();
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            for (int i = 0; i < gridHeight; i++) {
                for (int j = 0; j < (i % 2 == 0 ? gridWidth : gridWidth - 1); j++) {
                    if (cellField[i][j] == Cell.ALIVE) {
                        g.setColor(ALIVE_COLOR);
                    }
                    if (cellField[i][j] == Cell.DEAD) {
                        g.setColor(DEAD_COLOR);
                    }
                    Point pixCord = cells.get(new Point(i, j));
                    String imLabel = String.valueOf(impacts[i][j]);
                    if (imLabel.endsWith(".0")) {
                        imLabel = imLabel.replace(".0", "");
                    }
                    g.drawString(imLabel, pixCord.x - imLabel.length() * 8 / 2, pixCord.y+7);
                }
            }
        }
    }

    public void paintImpact() {
        if(gridSettings.isImpact() && gridSettings.getHexSize()>=30 && image != null) {
            Graphics g = image.getGraphics();
            g.setColor(Color.darkGray);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            for (int i = 0; i < gridHeight; i++) {
                for (int j = 0; j < (i % 2 == 0 ? gridWidth : gridWidth - 1); j++) {
                    Point pixCord = cells.get(new Point(i, j));
                    String imLabel = String.valueOf(impacts[i][j]);
                    if (imLabel.endsWith(".0")) {
                        imLabel = imLabel.replace(".0", "");
                    }
                    g.drawString(imLabel, pixCord.x - imLabel.length() * 8 / 2, pixCord.y+7);
                }
            }
        }
    }

    class MouseHandler extends MouseAdapter implements MouseMotionListener {
        volatile Point lastFilledPoint = null;

        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            try {
                int rgb = image.getRGB(point.x, point.y);
                if (rgb == ALIVE_COLOR.getRGB() && gridSettings.isReplace()) {
                    Point cord = calculateNearestCenter(point.x, point.y);
                    eraseImpact();
                    Graphic.fill(cells.get(cord), DEAD_COLOR, image);
                    cellField[cord.x][cord.y] = Cell.DEAD;
                    recountImpact();
                    paintImpact();
                    repaint();
                    gridSettings.setChanges(true);
                }
                if (rgb == DEAD_COLOR.getRGB()) {
                    Point cord = calculateNearestCenter(point.x, point.y);
                    eraseImpact();
                    Graphic.fill(cells.get(cord), ALIVE_COLOR, image);
                    cellField[cord.x][cord.y] = Cell.ALIVE;
                    recountImpact();
                    paintImpact();
                    repaint();
                    gridSettings.setChanges(true);
                }
            }catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lastFilledPoint = null;
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            Point point = e.getPoint();
            try {
                int rgb = image.getRGB(point.x, point.y);
                if (rgb == ALIVE_COLOR.getRGB() && gridSettings.isReplace()) {
                    Point cord = calculateNearestCenter(point.x, point.y);
                    if (cord.equals(lastFilledPoint)) {
                        return;
                    }
                    eraseImpact();
                    Graphic.fill(cells.get(cord), DEAD_COLOR, image);
                    lastFilledPoint = cord;
                    cellField[cord.x][cord.y] = Cell.DEAD;
                    recountImpact();
                    paintImpact();
                    repaint();
                    gridSettings.setChanges(true);
                    return;
                }
                if (rgb == DEAD_COLOR.getRGB()) {
                    Point cord = calculateNearestCenter(point.x, point.y);
                    if (cord.equals(lastFilledPoint)) {
                        return;
                    }
                    eraseImpact();
                    Graphic.fill(cells.get(cord), ALIVE_COLOR, image);
                    lastFilledPoint = cord;
                    cellField[cord.x][cord.y] = Cell.ALIVE;
                    recountImpact();
                    paintImpact();
                    repaint();
                    gridSettings.setChanges(true);
                }
                if(rgb == BORDER_COLOR.getRGB()){
                    lastFilledPoint = null;
                }
            }catch (NullPointerException | ArrayIndexOutOfBoundsException ignored){}
        }
    }

    private void recountImpact() {
        for (GridViewListener listener: listeners){
            listener.handleRecountQuery();
        }
    }

}
