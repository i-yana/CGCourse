package ru.nsu.fit.g13205.zharkova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Yana on 22.03.16.
 */
public class PaintArea extends JPanel implements ObservablePanel {

    private int ALIGNMENT = 30;
    private int MAP_WIDTH = 1000;
    private int MAP_HEIGHT = 700;
    private int LEGEND_X_OFFSET = ALIGNMENT+MAP_WIDTH+ALIGNMENT*3;
    private int LEGEND_WIDTH = 50;
    private int LEGEND_HEIGHT = MAP_HEIGHT;
    private static final Image CURSOR_ICON =  new ImageIcon("./icons/custom.gif").getImage();

    private BufferedImage legendImage;
    private BufferedImage colorMapImage;
    private BufferedImage gridLayer;
    private BufferedImage isolineLayer;
    private BufferedImage isoLegendLayer;
    private BufferedImage constructionLayer;
    private BufferedImage legendIsolineLayer;
    private BufferedImage levelImage;
    private BufferedImage interpolateMap;
    private BufferedImage interpolateLegend;
    BufferedImage legGridLayer;

    private boolean isColorMapMode = true;
    private boolean isGridMode = false;
    private boolean isIsolineMode = true;
    private boolean isConstructionMode = false;
    private boolean isInterpolationMode = false;

    private Properties properties;
    private MainFunction function;
    private Legend legend;

    private Observer observer;
    private Double tempLine;
    private ArrayList<Double> isolines;
    private boolean isRelocationMode = false;

    public PaintArea(){
        setBackground(Color.white);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Component c = e.getComponent();
                int w = c.getWidth();
                int h = c.getHeight();
                handleResizing(w, h);
            }
        });
        MouseHandler mouseHandler = new MouseHandler();
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Color Map", MAP_WIDTH / 2, 20);
        g.drawString("Legend", LEGEND_X_OFFSET, 20);
        g.setColor(Color.BLUE);
        g.drawRect(ALIGNMENT - 1, ALIGNMENT - 1, MAP_WIDTH + 1, MAP_HEIGHT + 1);
        g.drawRect(LEGEND_X_OFFSET-1, ALIGNMENT-1, LEGEND_WIDTH+1, LEGEND_HEIGHT+1);
        g.drawImage(levelImage, LEGEND_X_OFFSET+LEGEND_WIDTH, ALIGNMENT+1, null);
        if(properties == null){
            g.drawString("Empty file", MAP_WIDTH/2, ALIGNMENT + MAP_HEIGHT/2);
        }
        if(isColorMapMode) {
            g.drawImage(legendImage, LEGEND_X_OFFSET, ALIGNMENT, null);
            g.drawImage(colorMapImage, ALIGNMENT, ALIGNMENT, null);
        }
        if(isInterpolationMode) {
            g.drawImage(interpolateLegend, LEGEND_X_OFFSET, ALIGNMENT, null);
            g.drawImage(interpolateMap, ALIGNMENT, ALIGNMENT, null);
        }
        if(isGridMode) {
            g.drawImage(gridLayer, ALIGNMENT, ALIGNMENT, null);
            //g.drawImage(legGridLayer, LEGEND_X_OFFSET, ALIGNMENT, null);
        }
        if(isIsolineMode) {
            g.drawImage(isoLegendLayer, LEGEND_X_OFFSET, ALIGNMENT, null);
            g.drawImage(isolineLayer, ALIGNMENT, ALIGNMENT, null);
        }
        if(isConstructionMode){
            g.drawImage(constructionLayer, ALIGNMENT, ALIGNMENT, null);
            g.drawImage(legendIsolineLayer, LEGEND_X_OFFSET, ALIGNMENT, null);
            BufferedImage tempImage = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            if(tempLine!=null && !isRelocationMode) {
                try {
                    Tools.createIsolines(function, tempImage, tempLine, properties.getIsoColor());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(tempImage, ALIGNMENT, ALIGNMENT, null);
            }
        }
    }
    public void createMap(Properties properties) throws IOException {
        if(!properties.equals(this.properties)) {
            this.properties = properties;
            isolines = new ArrayList<>();
        }
        Boundary boundary = properties.getBoundary();
        this.function = new MainFunction(properties.getFunction(),boundary, 0, 0, MAP_WIDTH, MAP_HEIGHT, properties.getColors().size());
        this.legend = new Legend(0, LEGEND_WIDTH, function.getMin(), function.getMaxY(), 0, 0, LEGEND_WIDTH, LEGEND_HEIGHT, properties.getColors().size());
        createColorMap();
        createGrid();
        createInterpolateImage();
        isolineLayer = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        isoLegendLayer = new BufferedImage(LEGEND_WIDTH, LEGEND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        constructionLayer = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        legendIsolineLayer = new BufferedImage(LEGEND_WIDTH, LEGEND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        levelImage = new BufferedImage(LEGEND_WIDTH+ALIGNMENT*2, LEGEND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        ArrayList<Double> functionLevels = function.getLevels();
        Graphics graphics = levelImage.getGraphics();
        graphics.setColor(Color.BLACK);
        ArrayList<Double> levels = Tools.buildColorLevels(properties.getColors().size(), 0 , LEGEND_HEIGHT);
        for (int i = 1; i < functionLevels.size(); i++) {
            Tools.createIsolines(function, isolineLayer, function.getLevels().get(i), properties.getIsoColor());
            Tools.createIsolines(legend, isoLegendLayer, legend.getLevels().get(i), properties.getIsoColor());
            String text = " " + Tools.sub(Tools.eraseLastZero(new DecimalFormat(Tools.FORMAT).format(functionLevels.get(i))), 9);
            graphics.drawString(text,0, levels.get(functionLevels.size()-i).intValue());
        }
        for (Double z: isolines){
            Tools.createIsolines(function, constructionLayer, z, properties.getIsoColor());
            Tools.createIsolines(legend, legendIsolineLayer, z, properties.getIsoColor());
        }
        repaint();
    }

    private void createColorMap() throws IOException {
    //    this.function = new MainFunction(0, 3.0, 0, 3, 0, 0, MAP_WIDTH, MAP_HEIGHT, properties.getColors().size());
        colorMapImage = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        legendImage = new BufferedImage(LEGEND_WIDTH, LEGEND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        fillColorMap(function, colorMapImage);
        fillColorMap(legend, legendImage);
    }

    private void createInterpolateImage() throws IOException {
        interpolateLegend = Tools.interpolateLegend(legend, legendImage);
        interpolateMap = Tools.interpolateMap(function, legend, interpolateLegend, MAP_WIDTH, MAP_HEIGHT);
    }

    public void fillColorMap(Function f, BufferedImage image) throws IOException {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Cord functionCord = f.getCord(j,i);
                double h = f.getZ(functionCord);
                Color color = Tools.findLevel(h, f, properties.getColors());
                image.setRGB(j, i, color.getRGB());
            }
        }
    }

    public void createGrid(){
        gridLayer = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int k = properties.getK();
        int m = properties.getM();
        function.createGrid(k, m);
        Point[][] gridNode = function.getPixelGrid();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                Graphics graphics = gridLayer.getGraphics();
                graphics.setColor(Color.BLACK);
                graphics.drawOval(gridNode[i][j].x-3, gridNode[i][j].y-3, 5,5);
            }
        }
        legGridLayer = new BufferedImage(LEGEND_WIDTH, LEGEND_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        legend.createGrid();
        Point[][] legGridNode = legend.getPixelGrid();
        for (int i = 0; i < legend.getHeight(); i++) {
            for (int j = 0; j < 2; j++) {
                Graphics graphics = legGridLayer.getGraphics();
                graphics.setColor(Color.BLACK);
                graphics.drawOval(legGridNode[i][j].x-3, legGridNode[i][j].y-3, 5,5);
            }
        }
    }

    public void setColorMapMode(boolean isColorMapMode) {
        this.isColorMapMode = isColorMapMode;
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
    }

    public void setIsolineMode(boolean isIsolineMode) {
        this.isIsolineMode = isIsolineMode;
    }

    public void setConstructionMode(boolean isConstructionMode) {
        this.isConstructionMode = isConstructionMode;
    }

    public void setInterpolationMode(boolean isInterpolationMode) {
        this.isInterpolationMode = isInterpolationMode;
    }

    public void setRelocationMode(boolean isRelocationMode) {
        this.isRelocationMode = isRelocationMode;
    }


    private void handleResizing(int w, int h) {
        MAP_HEIGHT = h - ALIGNMENT*2;
        MAP_WIDTH = w - ALIGNMENT*5 - LEGEND_WIDTH;
        LEGEND_HEIGHT = MAP_HEIGHT;
        LEGEND_X_OFFSET = w - ALIGNMENT*3 - LEGEND_WIDTH;
        if(properties!=null) {
            try {
                createMap(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            repaint();
        }
    }

    private void handleMouseEntered(int u, int v){
        try {
            colorMapImage.getRGB(u, v);
            Cord cord = function.getCord(u, v);
            notifyAboutCordsChange(cord, function.getZ(cord));
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            notifyAboutCordsChange(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }

    @Override
    public void addListener(Observer listener) {
        this.observer = listener;
    }

    @Override
    public void notifyAboutCordsChange(Cord cord, Double z) {
        observer.updateStatusBar(cord, z);
    }

    public void eraseIsolines() {
        if(isolines!=null) {
            for (int i = 0; i < constructionLayer.getHeight(); i++) {
                for (int j = 0; j < constructionLayer.getWidth(); j++) {
                    constructionLayer.setRGB(j,i, new Color(0,0,0,0).getRGB());
                }
            }
            for (int i = 0; i < legendIsolineLayer.getHeight(); i++) {
                for (int j = 0; j < legendIsolineLayer.getWidth(); j++) {
                    legendIsolineLayer.setRGB(j,i,new Color(0,0,0,0). getRGB());
                }
            }
            isolines.clear();
        }
    }
    private void setNewCursor(int cursor){
        if(cursor == Cursor.MOVE_CURSOR){
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(CURSOR_ICON, new Point(0,0),"custom cursor"));
        }
        else {
            setCursor(Cursor.getPredefinedCursor(cursor));
        }
        repaint();
    }

    public void zoomOut() {
        if(properties != null){
            Boundary oldBoundary = properties.getBoundary();
            Boundary newBoundary = new Boundary(oldBoundary.getA()-Tools.ZOOM_STEP, oldBoundary.getB()+Tools.ZOOM_STEP, oldBoundary.getC()-Tools.ZOOM_STEP, oldBoundary.getD()+Tools.ZOOM_STEP);
            properties.setBoundary(newBoundary);
            try {
                createMap(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    public void zoomIn() {
        if(properties != null){
            Boundary oldBoundary = properties.getBoundary();
            double a = oldBoundary.getA()+Tools.ZOOM_STEP;
            double b = oldBoundary.getB()-Tools.ZOOM_STEP;
            double c = oldBoundary.getC()+Tools.ZOOM_STEP;
            double d = oldBoundary.getD()-Tools.ZOOM_STEP;
            if(a<b && c<d) {
                Boundary newBoundary = new Boundary(a, b, c, d);
                properties.setBoundary(newBoundary);
                try {
                    createMap(properties);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        }
    }

    class MouseHandler extends MouseAdapter {
        Integer oldX;
        Integer oldY;
        @Override
        public void mousePressed(MouseEvent e) {
            e.getPoint();
            int x = e.getPoint().x - ALIGNMENT - 1;
            int y = e.getPoint().y - ALIGNMENT - 1;
            try {
                if (isConstructionMode && !isRelocationMode) {
                    colorMapImage.getRGB(x, y);
                    tempLine = function.getZ(function.getCord(x, y));
                    repaint();
                }
                if (isRelocationMode) {
                    colorMapImage.getRGB(x, y);
                    this.oldX = x;
                    this.oldY = y;
                    setNewCursor(Cursor.MOVE_CURSOR);
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {} catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            e.getPoint();
            int x = e.getPoint().x - ALIGNMENT - 1;
            int y = e.getPoint().y - ALIGNMENT - 1;
            if (colorMapImage != null) {
                handleMouseEntered(x, y);
            }
            try {
                if (isConstructionMode && !isRelocationMode) {
                    assert colorMapImage != null;
                    colorMapImage.getRGB(x, y);
                    tempLine = function.getZ(function.getCord(x, y));
                    repaint();
                }
                if (isRelocationMode && oldY != null && oldX != null) {
                    assert colorMapImage != null;
                    colorMapImage.getRGB(x, y);
                    setNewCursor(Cursor.MOVE_CURSOR);
                    int deltaX = oldX - x;
                    int deltaY = -1*(oldY - y);
                    Cord newCord = function.getCord(deltaX, MAP_HEIGHT-deltaY);
                    Boundary oldBoundary = properties.getBoundary();
                    double deltaAB = oldBoundary.getB() - oldBoundary.getA();
                    double deltaCD = oldBoundary.getD() - oldBoundary.getC();
                    Boundary boundary = new Boundary(newCord.x, newCord.x+deltaAB, newCord.y, newCord.y + deltaCD);
                    oldX = x;
                    oldY = y;
                    properties.setBoundary(boundary);
                    createMap(properties);
                    repaint();
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
                setNewCursor(Cursor.DEFAULT_CURSOR);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(isConstructionMode && !isRelocationMode) {
                if (tempLine != null) {
                    try{
                    Tools.createIsolines(function, constructionLayer, tempLine, properties.getIsoColor());
                    Tools.createIsolines(legend, legendIsolineLayer, tempLine, properties.getIsoColor());
                    isolines.add(tempLine);
                    tempLine = null;
                    repaint();}
                    catch (IOException e1){
                        e1.printStackTrace();
                    }
                }
            }
            if(isRelocationMode){
                oldX = null;
                oldY = null;
                setNewCursor(Cursor.DEFAULT_CURSOR);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            if (colorMapImage != null) {
                Point point = e.getPoint();
                handleMouseEntered(point.x-ALIGNMENT-1, point.y-ALIGNMENT-1);
            }
        }
    }
}
