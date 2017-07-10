package ru.nsu.fit.g13205.zharkova.wf.view;

import ru.nsu.fit.g13205.zharkova.wf.model.*;
import ru.nsu.fit.g13205.zharkova.wf.model.Box;
import ru.nsu.fit.g13205.zharkova.wf.model.matrix.Matrix;
import ru.nsu.fit.g13205.zharkova.wf.model.matrix.MouseWheel;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Clipper;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Edge3D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Yana on 14.04.16.
 */
public class View3D extends JPanel{

    private static final ArrayList<Color> axisColors = new ArrayList<>();
    private BufferedImage image3D;
    private Properties properties;
    private Integer chooseObject = null;
    private SceneRotateMouse sceneRotateMouse;
    private double[][] normMatrix;
    private double[][]shiftMatrix;
    private Function f = new Function(-1,1,-1,1,0,0,700,700);
    private Camera camera;
    private Box boxEdge;

    static {
        axisColors.add(Color.red);
        axisColors.add(Color.green);
        axisColors.add(Color.blue);
    }

    public View3D(){
        super();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(740, 740));
        image3D = new BufferedImage(700,700, BufferedImage.TYPE_INT_RGB);
        this.camera = new Camera(new Point3D(0,0,-10), new Point3D(0,0,10));
        this.boxEdge = new Box();
        MouseHandler mouse = new MouseHandler();
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.sceneRotateMouse = new SceneRotateMouse(this);
        this.addMouseListener(sceneRotateMouse);
        this.addMouseMotionListener(sceneRotateMouse);
        MouseWheel mouseWheel = new MouseWheel(this);
        this.addMouseWheelListener(mouseWheel);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image3D, 10, 10, null);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(new BasicStroke(3));
        g.setColor(Color.WHITE);
        g.drawLine(20,20, 20,720);
        g.drawLine(20, 20, 720, 20);
        g.drawLine(720,20,720,720);
        g.drawLine(20,720,720,720);
    }

    public void createArea(Properties properties) {
        this.properties = properties;
        ArrayList<RotationObject> rotationObjects = properties.getRotationObjects();
        for (RotationObject rotationObject: rotationObjects){
            rotationObject.buildSpline();
        }
        createBox();
        paintObjects();
        repaint();
    }


    private Point3D render3D(Point3D point3D, double[][] objectMatrix){
        Point3D newPoint = Matrix.mul(objectMatrix, point3D);
        double w = newPoint.getW();
        return new Point3D(newPoint.getX()/w, newPoint.getY()/w, newPoint.getZ()*w, w);
    }

    public void paintObject(RotationObject rotationObject) {
        ViewParameters v = properties.getViewParameters();
        double[][] objectMatrix = Matrix.mul(normMatrix, shiftMatrix);

        double[][] matrix  = Matrix.buildMatrix(objectMatrix, properties.getSceneMatrix(), camera, properties.getViewParameters());

        Graphics g = image3D.getGraphics();
        ArrayList<ArrayList<Point3D>> segments = rotationObject.getRotatedSegments();
        ArrayList<ArrayList<Point3D>> circles = rotationObject.getRotatedCircles();
        g.setColor(rotationObject.getObjColor());
        for (int i = 0; i < segments.size(); i++) {
            ArrayList<Point3D> oneSegment = segments.get(i);
            for (int j = 1; j < oneSegment.size(); j++) {

                Point3D point1 = render3D(oneSegment.get(j - 1), matrix);
                Point3D point2 = render3D(oneSegment.get(j), matrix);
                if(Clipper.tryPoint(point1, point2, v.getZn(), v.getZf(),v.getSw(), v.getSh())){
                    Point p1 = f.getPixel(point1.getX(), point1.getY());
                    Point p2 = f.getPixel(point2.getX(), point2.getY());
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        for (int j = 0; j < circles.size(); j++) {
            ArrayList<Point3D> oneCircle = circles.get(j);
            for (int i = 1; i < oneCircle.size(); i++) {
                Point3D point1 = render3D(oneCircle.get(i - 1), matrix);
                Point3D point2 = render3D(oneCircle.get(i), matrix);
                if(Clipper.tryPoint(point1, point2, v.getZn(), v.getZf(),v.getSw(), v.getSh())){
                    Point p1 = f.getPixel(point1.getX(), point1.getY());
                    Point p2 = f.getPixel(point2.getX(), point2.getY());
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
        g.setColor(Color.red);
        ArrayList<Edge3D> axis = rotationObject.getAxis();
        int i = 0;
        for (Edge3D edge3D: axis){
            g.setColor(axisColors.get(i++));
            Point3D point1 = render3D(edge3D.getP1(), matrix);
            Point3D point2 = render3D(edge3D.getP2(), matrix);
            Point p1 = f.getPixel(point1.getX(), point1.getY());
            Point p2 = f.getPixel(point2.getX(),point2.getY());
            g.drawLine(p2.x, p2.y, p1.x, p1.y);

        }
        repaint();
    }

    private void clearImage() {
        for (int i = 0; i < image3D.getHeight(); i++) {
            for (int j = 0; j < image3D.getWidth(); j++) {
                image3D.setRGB(j,i,Color.BLACK.getRGB());
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void paintObjects() {
        clearImage();
        createBox();
        ArrayList<RotationObject> rotationObjects = new ArrayList<>(properties.getRotationObjects());
        rotationObjects.sort(new Comparator<RotationObject>() {
            @Override
            public int compare(RotationObject o1, RotationObject o2) {
                double[][] vector = new double[4][1];
                vector[0][0] = 0;
                vector[1][0] = 0;
                vector[2][0] = 0;
                vector[3][0] = 1;
                double[][] matrix1 = Matrix.mul(Matrix.mul(properties.getSceneMatrix(), o1.getEulerAngle()), vector);
                double[][] matrix2 = Matrix.mul(Matrix.mul(properties.getSceneMatrix(), o2.getEulerAngle()), vector);
                if (matrix1[2][0] < matrix2[2][0]) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        rotationObjects.forEach(View3D.this::paintObject);
        paintSceneAxis();
        repaint();
    }

    private void paintSceneAxis() {
        ViewParameters v = properties.getViewParameters();
        double[][] matrix  = Matrix.buildMatrix(new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}}, properties.getSceneMatrix(), camera, properties.getViewParameters());

        Graphics g = image3D.getGraphics();
        g.setColor(axisColors.get(0));
        Point3D point1 = render3D(new Point3D(0, 0, 0), matrix);
        Point3D point2 = render3D(new Point3D(1, 0, 0), matrix);
        Point p1 = f.getPixel(point1.getX(), point1.getY());
        Point p2 = f.getPixel(point2.getX(), point2.getY());
        g.drawLine(p1.x, p1.y, p2.x, p2.y);



        g.setColor(axisColors.get(1));
        point1 = render3D(new Point3D(0, 0, 0), matrix);
        point2 = render3D(new Point3D(0, 1, 0), matrix);
        p1 = f.getPixel(point1.getX(), point1.getY());
        p2 = f.getPixel(point2.getX(), point2.getY());
        g.drawLine(p1.x, p1.y, p2.x, p2.y);

        g.setColor(axisColors.get(2));
        point1 = render3D(new Point3D(0, 0, 0), matrix);
        point2 = render3D(new Point3D(0, 0, 1), matrix);
        p1 = f.getPixel(point1.getX(), point1.getY());
        p2 = f.getPixel(point2.getX(), point2.getY());
        g.drawLine(p1.x, p1.y, p2.x, p2.y);


        g.setColor(Color.WHITE);
        for (Edge3D edge3D: boxEdge.getEdges()){
            point1 = render3D(edge3D.getP1(), matrix);
            point2 = render3D(edge3D.getP2(), matrix);
            if(Clipper.tryPoint(point1, point2, v.getZn(), v.getZf(),v.getSw(), v.getSh())){
                p1 = f.getPixel(point1.getX(), point1.getY());
                p2 = f.getPixel(point2.getX(), point2.getY());
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public void setChooseObject(Integer chooseObject) {
        this.chooseObject = chooseObject;
    }

    public void sceneRotate(boolean isRotate) {
        sceneRotateMouse.enableRotate(isRotate);
    }

    public void createBox() {

        double maxX, maxY, maxZ;
        double minX, minY, minZ;
        maxX = maxY = maxZ = -1000000;
        minX = minY = minZ = 1000000;
        for (RotationObject r : properties.getRotationObjects()){
            Point3D maxs = r.getMaxPoint();
            if(maxs.getX()>maxX){
                maxX = maxs.getX();
            }
            if(maxs.getY()>maxY){
                maxY = maxs.getY();
            }
            if(maxs.getZ()>maxZ){
                maxZ = maxs.getZ();
            }

            Point3D mins = r.getMinPoint();
            if(mins.getX()<minX){
                minX = mins.getX();
            }
            if(mins.getY()<minY){
                minY = mins.getY();
            }
            if(mins.getZ()<minZ){
                minZ = mins.getZ();
            }
        }
        double deltaX = (maxX-minX)/2;
        double deltaY = (maxY-minY)/2;
        double deltaZ = (maxZ-minZ)/2;

        double ratio = deltaX>deltaY?deltaX:deltaY;
        ratio = ratio>deltaZ?ratio:deltaZ;

        shiftMatrix = new double[][]{
                {
                        1,0,0,-(minX+(maxX-minX)/2)
                },
                {
                        0,1,0,-(minY+(maxY-minY)/2)
                },
                {
                        0,0,1,-(minZ+(maxZ-minZ)/2)
                },
                {
                        0, 0, 0, 1
                }
        };

        normMatrix = new double[][]{
                {
                        1d/ratio,0,0,0
                },
                {
                        0,1d/ratio,0,0
                },
                {
                        0,0,1d/ratio,0
                },
                {
                        0,0,0,1
                }
        };
    }


    public void resetRotateScene() {
        properties.initSceneMatrix();
        paintObjects();
    }

    class MouseHandler extends MouseAdapter{
        Point point;
        @Override
        public void mouseDragged(MouseEvent e) {
            if(chooseObject==null){
                return;
            }
            try {
                Point p = e.getPoint();
                int deltaX = point.x - p.x;
                int deltaY = point.y - p.y;
                properties.getRotationObjects().get(chooseObject).changeMatrix(deltaY * 0.01, deltaX * 0.01, 0);
                paintObjects();
                point = p;
            }catch (NullPointerException ignored){}
        }

        @Override
        public void mousePressed(MouseEvent e){
            point = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e){
            point = null;
        }
    }
}
