package ru.nsu.fit.g13205.zharkova.wf.view;

import javafx.util.Pair;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Point3D;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Properties;
import ru.nsu.fit.g13205.zharkova.wf.model.RotationObject;
import ru.nsu.fit.g13205.zharkova.wf.model.ViewParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Yana on 29.03.16.
 */
public class SettingsDialog extends JDialog {

    private final MainView parent;
    private JButton ok;
    private SplineArea splineArea;
    private JPanel settingsPanel;
    private JPanel buttonPanel;
    final Properties properties;
    private JButton label = new JButton("Color");
    private int objectNumber = 0;
    private JRadioButton scene;
    private JRadioButton object;
    Box panelBox;

    Pair<JLabel, JSpinner> a;
    Pair<JLabel, JSpinner> b;
    Pair<JLabel, JSpinner> c;
    Pair<JLabel, JSpinner> d;
    Pair<JLabel, JSpinner> n;
    Pair<JLabel, JSpinner> m;
    Pair<JLabel, JSpinner> k;
    Pair<JLabel, JSpinner> cX;
    Pair<JLabel, JSpinner> cY;
    Pair<JLabel, JSpinner> cZ;


    public SettingsDialog(MainView parent, Properties properties) {
        super();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                parent.getView3D().setChooseObject(null);
                parent.getView3D().sceneRotate(true);
                parent.enableSettingButton();
            }
        });
        this.properties = properties;
        this.parent = parent;
        this.splineArea = new SplineArea(this.properties, parent.getView3D());
        splineArea.createArea(0);
        //parent.getView3D().setChooseObject(objectNumber);
        //parent.getView3D().sceneRotate(false);
        panelBox = Box.createVerticalBox();
        this.settingsPanel = new JPanel(new GridLayout(3,1));
        this.buttonPanel = new JPanel(new FlowLayout());
        setLayout(new BorderLayout());
        initPanel();
        initButtonPanel();
        panelBox.add(splineArea);
        panelBox.add(settingsPanel);
        panelBox.add(buttonPanel);
        Box okBox = Box.createHorizontalBox();
        okBox.add(ok);

        panelBox.add(okBox);
        add(panelBox, BorderLayout.CENTER);

        pack();
        setResizable(false);

        setVisible(true);
    }

    private void initButtonPanel() {
        ok = new JButton("OK");
        ok.addActionListener(e -> {
            setVisible(false);
            dispose();
            parent.getView3D().setChooseObject(null);
            parent.getView3D().sceneRotate(true);
            parent.enableSettingButton();
        });
        JButton zoomIn = new JButton(new ImageIcon("./icons/zoom.gif"));
        zoomIn.addActionListener(e -> splineArea.zoomIn());
        JButton zoomOut = new JButton(new ImageIcon("./icons/zoomout.gif"));
        zoomOut.addActionListener(e -> splineArea.zoomOut());
        JButton addNewObj = new JButton(new ImageIcon("./icons/add.gif"));
        addNewObj.addActionListener(e -> {
            RotationObject r = new RotationObject(Color.WHITE, new Point3D(0,0,0), new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}},Properties.DEFAULT_POINTS);
            r.setParams(10,10,10,0.0,1.0,0.0,6.28);
            properties.getRotationObjects().add(r);
            objectNumber = properties.getRotationObjects().size()-1;
            splineArea.createArea(properties.getRotationObjects().size()-1);
            changeSpinersValue(objectNumber);
            label.setBackground(properties.getRotationObjects().get(objectNumber).getObjColor());
            if(object.isSelected()) {
                parent.getView3D().setChooseObject(objectNumber);
            }
        });
        JButton removeObj = new JButton(new ImageIcon("./icons/remove.gif"));
        removeObj.addActionListener(e -> {
            try {
                properties.getRotationObjects().remove(objectNumber);
                objectNumber = 0;
                splineArea.createArea(objectNumber);
                changeSpinersValue(objectNumber);
                label.setBackground(properties.getRotationObjects().get(objectNumber).getObjColor());
                if(object.isSelected()) {
                    parent.getView3D().setChooseObject(objectNumber);
                }
            }catch (IndexOutOfBoundsException ex){
                parent.getView3D().setChooseObject(null);
            }
        });
        JButton previous = new JButton(new ImageIcon("./icons/left.gif"));
        previous.addActionListener(e -> {
            if(objectNumber>0){
                objectNumber--;
                splineArea.createArea(objectNumber);
                label.setBackground(properties.getRotationObjects().get(objectNumber).getObjColor());
                changeSpinersValue(objectNumber);
                if(object.isSelected()) {
                    parent.getView3D().setChooseObject(objectNumber);
                }
            }
        });
        JButton next = new JButton(new ImageIcon("./icons/right.gif"));
        next.addActionListener(e -> {
            if(objectNumber+1<properties.getRotationObjects().size()){
                objectNumber++;
                splineArea.createArea(objectNumber);
                label.setBackground(properties.getRotationObjects().get(objectNumber).getObjColor());
                changeSpinersValue(objectNumber);
                if(object.isSelected()) {
                    parent.getView3D().setChooseObject(objectNumber);
                }
            }
        });
        JCheckBox autoscale = new JCheckBox("Auto scale");
        autoscale.setSelected(true);
        autoscale.addActionListener(e -> {
            if(((JCheckBox)e.getSource()).isSelected()) {
                splineArea.setAutoScale(true);
            }
            else {
                splineArea.setAutoScale(false);
            }
        });
        JCheckBox showLines = new JCheckBox("Show lines");
        showLines.setSelected(true);
        showLines.addActionListener(e -> {
            if(((JCheckBox)e.getSource()).isSelected()) {
                splineArea.showLines(true);
            }
            else {
                splineArea.showLines(false);
            }
        });
        scene = new JRadioButton("Rotate Scene");
        scene.setSelected(true);
        object = new JRadioButton("Rotate Figure");
        ButtonGroup bg = new ButtonGroup();
        bg.add(scene);
        bg.add(object);
        scene.addActionListener(e->{
            parent.getView3D().setChooseObject(null);
            parent.getView3D().sceneRotate(true);
        });
        object.addActionListener(e->{
            parent.getView3D().sceneRotate(false);
            parent.getView3D().setChooseObject(objectNumber);
        });

        Box box = Box.createVerticalBox();
        box.add(autoscale);
        box.add(showLines);
        Box box1 = Box.createVerticalBox();
        box1.add(scene);
        box1.add(object);
        buttonPanel.add(box);
        buttonPanel.add(zoomIn);
        buttonPanel.add(zoomOut);
        buttonPanel.add(addNewObj);
        buttonPanel.add(removeObj);
        buttonPanel.add(previous);
        buttonPanel.add(next);
        buttonPanel.add(box1);

    }

    private void changeSpinersValue(int i) {
        try {
            RotationObject rotationObject = properties.getRotationObjects().get(i);
            n.getValue().setValue(rotationObject.getN());
            m.getValue().setValue(rotationObject.getM());
            k.getValue().setValue(rotationObject.getK());
            a.getValue().setValue(rotationObject.getA());
            b.getValue().setValue(rotationObject.getB());
            c.getValue().setValue(rotationObject.getC());
            d.getValue().setValue(rotationObject.getD());
            cX.getValue().setValue(rotationObject.getCentralPoint().getX());
            cY.getValue().setValue(rotationObject.getCentralPoint().getY());
            cZ.getValue().setValue(rotationObject.getCentralPoint().getZ());
        }catch (IndexOutOfBoundsException ignored){}
    }

    private void initPanel() {
        int nVal,mVal,kVal;
        double aVal, bVal, cVal, dVal, cxVal, cyVal, czVal;
        nVal = mVal = kVal = 1;
        aVal = bVal = cVal = dVal = cxVal = cyVal = czVal = 0.0;
        try {
            RotationObject rotationObject = properties.getRotationObjects().get(objectNumber);
            nVal = rotationObject.getN();
            mVal = rotationObject.getM();
            kVal = rotationObject.getK();
            aVal = rotationObject.getA();
            bVal = rotationObject.getB();
            cVal = rotationObject.getC();
            dVal = rotationObject.getD();
            cxVal = rotationObject.getCentralPoint().getX();
            cyVal = rotationObject.getCentralPoint().getY();
            czVal = rotationObject.getCentralPoint().getZ();
        }catch (IndexOutOfBoundsException ignored){};
        n = createSpinner("n", nVal, 1,100,1, "setN");
        Box nBox = Box.createHorizontalBox();
        nBox.add(Box.createHorizontalStrut(6));
        nBox.add(n.getKey());
        nBox.add(n.getValue());
        settingsPanel.add(nBox);

        m = createSpinner("m", mVal, 1,100,1, "setM");
        Box mBox = Box.createHorizontalBox();
        mBox.add(m.getKey());
        mBox.add(m.getValue());
        settingsPanel.add(mBox);

        k = createSpinner("k", kVal, 1,100,1, "setK");
        Box kBox = Box.createHorizontalBox();
        kBox.add(Box.createHorizontalStrut(8));
        kBox.add(k.getKey());
        kBox.add(k.getValue());
        settingsPanel.add(kBox);

        initColorChooser();

        cX = createSpinner("cX", cxVal, -100, 100, 0.1, "setCX");
        Box cxBox = Box.createHorizontalBox();
        cxBox.add(cX.getKey());
        cxBox.add(cX.getValue());
        settingsPanel.add(cxBox);

        a = createSpinner("a", aVal, 0.0, 1.0, 0.01, "setA");
        Box aBox = Box.createHorizontalBox();
        aBox.add(Box.createHorizontalStrut(7));
        aBox.add(a.getKey());
        aBox.add(a.getValue());
        settingsPanel.add(aBox);

        b = createSpinner("b", bVal, 0.0,1.0,0.01, "setB");
        Box bBox = Box.createHorizontalBox();
        bBox.add(Box.createHorizontalStrut(5));
        bBox.add(b.getKey());
        bBox.add(b.getValue());
        settingsPanel.add(bBox);

        c = createSpinner("c", cVal, 0.0,6.28,0.01, "setC");
        Box cBox = Box.createHorizontalBox();
        cBox.add(Box.createHorizontalStrut(9));
        cBox.add(c.getKey());
        cBox.add(c.getValue());
        settingsPanel.add(cBox);

        d = createSpinner("d", dVal, 0.0,6.28,0.01, "setD");
        Box dBox = Box.createHorizontalBox();
        dBox.add(Box.createHorizontalStrut(6));
        dBox.add(d.getKey());
        dBox.add(d.getValue());
        settingsPanel.add(dBox);

        cY = createSpinner("cY", cyVal, -100, 100, 0.1, "setCY");
        Box cyBox = Box.createHorizontalBox();
        cyBox.add(cY.getKey());
        cyBox.add(cY.getValue());
        settingsPanel.add(cyBox);

        createViewParamPanel();

        cZ = createSpinner("cZ", czVal, -100, 100, 0.1, "setCZ");
        Box czBox = Box.createHorizontalBox();
        czBox.add(cZ.getKey());
        czBox.add(cZ.getValue());
        settingsPanel.add(czBox);
    }

    private void initColorChooser() {
        label.setMinimumSize(new Dimension(70, 20));
        label.setMaximumSize(new Dimension(70, 20));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        try {
            label.setBackground(properties.getRotationObjects().get(objectNumber).getObjColor());
        }catch (IndexOutOfBoundsException ex){
            label.setBackground(Color.WHITE);
        }
        label.setOpaque(true);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Color color = JColorChooser.showDialog(SettingsDialog.this, "Choose a color", ((JButton) e.getSource()).getBackground());
                if(color!=null){
                    ((JButton)e.getSource()).setBackground(color);
                    properties.getRotationObjects().get(objectNumber).setColor(color);
                    splineArea.notifyAboutPropertiesChange();
                    repaint();
                }
            }
        });
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(18));
        box.add(label);
        settingsPanel.add(box);
    }

    private Pair<JLabel, JSpinner> createSpinner(String parameter, double value, double min, double max, double step, String methodName) {
        JLabel label = new JLabel(parameter);
        SpinnerModel spinnerModel = new SpinnerNumberModel(value, min, max, step);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            try {
                Method method = properties.getRotationObjects().get(objectNumber).getClass().getMethod(methodName, double.class);
                Double val = (Double) ((JSpinner) e.getSource()).getValue();
                if(parameter.equals("a") && val>properties.getRotationObjects().get(objectNumber).getB()){
                    spinner.setValue(properties.getRotationObjects().get(objectNumber).getB());
                }
                if(parameter.equals("b") && val<properties.getRotationObjects().get(objectNumber).getA()){
                    spinner.setValue(properties.getRotationObjects().get(objectNumber).getA());
                }
                if(parameter.equals("c") && val>properties.getRotationObjects().get(objectNumber).getD()){
                    spinner.setValue(properties.getRotationObjects().get(objectNumber).getD());
                }
                if(parameter.equals("d") && val<properties.getRotationObjects().get(objectNumber).getC()){
                    spinner.setValue(properties.getRotationObjects().get(objectNumber).getC());
                }
                method.invoke(properties.getRotationObjects().get(objectNumber), (Double) ((JSpinner) e.getSource()).getValue());
                splineArea.notifyAboutPropertiesChange();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IndexOutOfBoundsException e1) {
                e1.printStackTrace();
            }

        });
        return new Pair<>(label, spinner);
    }

    private Pair<JLabel, JSpinner> createSpinner(String parameter, int value, int min, int max, int step, String methodName) {
        JLabel label = new JLabel(parameter);
        SpinnerModel spinnerModel = new SpinnerNumberModel(value, min, max, step);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            try {
                Method method = properties.getRotationObjects().get(objectNumber).getClass().getMethod(methodName, int.class);
                method.invoke(properties.getRotationObjects().get(objectNumber), (Integer)((JSpinner)e.getSource()).getValue());
                splineArea.notifyAboutPropertiesChange();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IndexOutOfBoundsException e1) {

            }

        });
        return new Pair<>(label, spinner);
    }

    private void createViewParamPanel(){
        ViewParameters parameters = properties.getViewParameters();

        JLabel labelZN = new JLabel("zn");
        SpinnerModel spinnerModel = new SpinnerNumberModel(parameters.getZn(), -100, 100, 0.1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            parameters.setZN((Double) ((JSpinner) e.getSource()).getValue());
            parent.getView3D().paintObjects();
        });
        Box znBox = Box.createHorizontalBox();
        znBox.add(labelZN);
        znBox.add(spinner);
        settingsPanel.add(znBox);

        JLabel labelZF = new JLabel("zf");
        SpinnerModel spinnerModel1 = new SpinnerNumberModel(parameters.getZf(), -100, 100, 0.1);
        JSpinner spinner1 = new JSpinner(spinnerModel1);
        spinner1.addChangeListener(e -> {
            parameters.setZF((Double) ((JSpinner) e.getSource()).getValue());
            parent.getView3D().paintObjects();
        });
        Box zfBox = Box.createHorizontalBox();
        zfBox.add(labelZF);
        zfBox.add(spinner1);
        settingsPanel.add(zfBox);

        JLabel labelSW = new JLabel("sw");
        SpinnerModel spinnerModel2 = new SpinnerNumberModel(parameters.getSw(), -100, 100, 1);
        JSpinner spinner2 = new JSpinner(spinnerModel2);
        spinner2.addChangeListener(e -> {
            parameters.setSW((Double) ((JSpinner) e.getSource()).getValue());
            parent.getView3D().paintObjects();
        });
        Box swBox = Box.createHorizontalBox();
        swBox.add(labelSW);
        swBox.add(spinner2);
        settingsPanel.add(swBox);

        JLabel labelSH = new JLabel("sh");
        SpinnerModel spinnerModel3 = new SpinnerNumberModel(parameters.getSh(), -100, 100, 1);
        JSpinner spinner3 = new JSpinner(spinnerModel3);
        spinner3.addChangeListener(e -> {
            parameters.setSH((Double) ((JSpinner) e.getSource()).getValue());
            parent.getView3D().paintObjects();
        });
        Box shBox = Box.createHorizontalBox();
        shBox.add(labelSH);
        shBox.add(spinner3);
        settingsPanel.add(shBox);
    }
}
