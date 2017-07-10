package ru.nsu.fit.g13205.zharkova.raytracing.view;


import javafx.geometry.Point2D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.*;
import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.*;
import ru.nsu.fit.g13205.zharkova.raytracing.model.surfaces.Surface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Yana on 14.05.16.
 */
public class View3D extends JPanel {

    private static final double STEP = 100;
    private static final int PREFERRED_SIZE = 500;
    private static final int OFFSET = 0;
    private Scene scene;
    private RenderSetting renderSetting;
    private BufferedImage image3D;
    private BufferedImage renderedImage;
    private Function f = new Function(-1,1,-1,1,0,0,600,600);
    private double[][] sceneMatrix;
    double[][] rotate;
    private SceneRotateMouse sceneRotateMouse;
    private boolean isRender = false;
    private PanelListener listener;


    public View3D(PanelListener listener){
        super();
        setLayout(null);
        this.listener = listener;
        this.sceneMatrix = new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
        this.rotate = new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
        this.sceneRotateMouse = new SceneRotateMouse(this);
        addMouseListener(sceneRotateMouse);
        addMouseMotionListener(sceneRotateMouse);
        MouseWheel mouseWheel = new MouseWheel(this);
        this.addMouseWheelListener(mouseWheel);
        KeyEventDispatcher keyEventDispatcher = e -> {
            if (e.getKeyCode()== KeyEvent.VK_RIGHT) {
                renderSetting.getCamera().upCamera(0.5);
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                renderSetting.getCamera().downCamera(0.5);

            }
            if(e.getKeyCode() == KeyEvent.VK_UP){
                renderSetting.getCamera().leftCamera(0.5);
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                renderSetting.getCamera().rightCamera(0.5);
            }
            paintObjects();
            return false;
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
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
    }

    private void handleResizing(int w, int h) {
        if(renderSetting!=null) {
            Wireframe wireframe = renderSetting.getWireframe();
            double sw = w/STEP;
            double sh = h/STEP;
            wireframe.setSW(sw);
            wireframe.setSH(sh);
            f = new Function(sw/2d,-sw/2d,sh/2d, -sh/2d,0,0,w,h);
            paintObjects();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image3D==null){
            g.setColor(Color.darkGray);
            g.drawString("No files are open.", getWidth()/2-50, getHeight()/2);
        }
        if(isRender){
            g.drawImage(renderedImage, OFFSET, OFFSET, null);
        }
        else {
            g.drawImage(image3D, OFFSET, OFFSET, null);
        }
    }

    public void createArea(Scene scene, RenderSetting renderSetting){
        isRender = false;
        this.scene = scene;
        if(renderSetting == null){
            this.renderSetting = new RenderSetting();
            init();
            changeDimension();
        }
        else {
            setRenderSettings(renderSetting);
        }
    }

    private Point fromCoordinatesToPixel(Point2D point){
        double x = point.getX(), y = point.getY();
        return f.getPixel(x,y);
    }

    public void setRenderSettings(RenderSetting renderSettings) {
        this.renderSetting = renderSettings;
        this.sceneMatrix = new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
        changeDimension();
    }

    public void paintObjects() {
        if(scene!=null) {
            image3D = new BufferedImage(getWidth(), getHeight(),  BufferedImage.TYPE_INT_RGB);
            scene.getSurfaces().forEach(View3D.this::paintObject);
        }
        repaint();
    }

    private Point3D render3D(Point3D point3D, double[][] objectMatrix){
        Point3D newPoint = Matrix.mul(objectMatrix, point3D);
        double w = newPoint.getW();
        return new Point3D(newPoint.getX()/w, newPoint.getY()/w, -newPoint.getZ(), 1);
    }

    private void paintObject(Surface surface) {
        double[][] matrix  = Matrix.buildMatrix(sceneMatrix, renderSetting.getCamera(), renderSetting.getWireframe());
        Graphics2D g = image3D.createGraphics();
        Intensive intensive = surface.getKd();
        g.setColor(new Color(intensive.red, intensive.green, intensive.blue));
        ArrayList<Edge3D> edges = surface.getEdges();
        for (Edge3D edge : edges) {
            Point3D point1 = render3D(edge.getP1(), matrix);
            Point3D point2 = render3D(edge.getP2(), matrix);
            Point p1 = fromCoordinatesToPixel(new Point2D(point1.getX(), point1.getY()));
            Point p2 = fromCoordinatesToPixel(new Point2D(point2.getX(), point2.getY()));
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void changeDimension() {
        Wireframe wireframe = renderSetting.getWireframe();
        int width = (int) (wireframe.getSw()*STEP);
        int height = (int) (wireframe.getSh()*STEP);
        listener.handleResizeMainView(width, height);
        handleResizing(width,height);
    }


    public void sceneRotate(boolean isRotate) {
        sceneRotateMouse.enableRotate(isRotate);
    }


    public void rotateCamera(double ex, double ey) {
        double[][] matrixX = Matrix.createRyMatrix(ey);
        double[][] matrixY = Matrix.createRzMatrix(ex);
        double[][] matrixZ = Matrix.createRxMatrix(0);
        sceneMatrix = Matrix.mul(matrixZ,Matrix.mul(matrixY,Matrix.mul(matrixX, sceneMatrix)));
    }

    public RenderSetting getRenderSetting() {
        return renderSetting;
    }

    public void init() {
        Point3D maxPoint = scene.getMaxPoint();
        Point3D minPoint = scene.getMinPoint();


        double eyeX = minPoint.x - (maxPoint.z) / Math.tan(Math.PI / 6d);


        sceneMatrix = new double[][]
                {{1d,0,0,0},
                        {0,1d,0,0},
                        {0,0,1d,0},
                        {0,0,0,1d}};

        double zn = (minPoint.x - eyeX) / 2;
        double zf = maxPoint.x - eyeX + (maxPoint.x-minPoint.x)/2;
        renderSetting.setZN(zn);
        renderSetting.setZF(zf);

        Point3D view = minPoint.add(maxPoint.sub(minPoint).div(new Point3D(2d,2d,2d)));
        Point3D eyePoint = new Point3D(eyeX,view.y,view.z);
        Point3D upVector = new Point3D(0,0,1);
        renderSetting.setCamera(new Camera(eyePoint, view, upVector));
        paintObjects();
    }

    public BufferedImage createTextureImage() {
        renderedImage = new BufferedImage(image3D.getWidth(), image3D.getHeight(), image3D.getType());
        Graphics g = renderedImage.getGraphics();
        g.setColor(renderSetting.getColor());
        g.fillRect(0, 0, renderedImage.getWidth(), renderedImage.getHeight());
        return renderedImage;
    }

    public void setRender(boolean render) {
        this.isRender = render;
    }

    public Scene getScene() {
        return scene;
    }

    public Dimension getDimension() {
        return new Dimension(image3D.getWidth(), image3D.getHeight());
    }

    public double[][] getSceneMatrix() {
        return sceneMatrix;
    }

    public BufferedImage getCurrentImage(){
        if(!isRender){
            return image3D;
        }
        return renderedImage;
    }
}
