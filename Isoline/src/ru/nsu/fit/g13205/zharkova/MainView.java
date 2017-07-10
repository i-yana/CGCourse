package ru.nsu.fit.g13205.zharkova;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Yana on 22.03.16.
 */
public class MainView extends JFrame implements Observer{

    public static final int MIN_SIZE = 500;
    private PaintArea paintArea;
    private StatusBar statusBar;
    private JButton onOffColorMap;
    private JButton onEmptyMap;
    private JButton onOffInterpolation;
    private JButton onOffInteractiveBuild;
    private JButton onOffGrid;
    private JButton onOffIsoline;
    private JButton move;

    public MainView(){
        super("FIT_13205_Zharkova_Isoline");
        this.setMinimumSize(new Dimension(MIN_SIZE, MIN_SIZE));
        this.setSize(1100, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createStatusBar();
        createMenuBar();
        createToolBar();
        createPaintArea();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void createPaintArea() {
        paintArea = new PaintArea();
        paintArea.addListener(this);
        JScrollPane pane = new JScrollPane(paintArea);
        add(pane, BorderLayout.CENTER);
    }

    private void createStatusBar() {
        statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        JButton newDocument = new JButton(new ImageIcon("./icons/open.gif"));
        new ComponentHelpText(statusBar, newDocument, "Open configure file");
        newDocument.addActionListener(e -> open());
        toolBar.add(newDocument);

        JButton funcSettings = new JButton(new ImageIcon("./icons/function.gif"));
        new ComponentHelpText(statusBar, funcSettings, "Set color and function parameters...");
        funcSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingsDialog();
            }
        });
        toolBar.add(funcSettings);
        toolBar.addSeparator();
        onOffColorMap = new JButton(new ImageIcon("./icons/colorMap.gif"));
        new ComponentHelpText(statusBar, onOffColorMap, "On color map");
        onEmptyMap = new JButton(new ImageIcon("./icons/empty.gif"));
        new ComponentHelpText(statusBar, onEmptyMap, "Off color map");
        onOffInterpolation = new JButton(new ImageIcon("./icons/color.gif"));
        new ComponentHelpText(statusBar, onOffInterpolation, "On interpolation");

        onOffColorMap.setSelected(true);
        onOffColorMap.addActionListener(e -> {
            setColorMode();
        });
        toolBar.add(onOffColorMap);

        onOffInterpolation.setSelected(false);
        onOffInterpolation.addActionListener(e -> {
            setInterpolationMode();
        });
        toolBar.add(onOffInterpolation);

        onEmptyMap.setSelected(false);
        onEmptyMap.addActionListener(e -> {
            setEmptyMode();
        });
        toolBar.add(onEmptyMap);
        toolBar.addSeparator();
        onOffGrid = new JButton(new ImageIcon("./icons/grid.gif"));
        new ComponentHelpText(statusBar, onOffGrid, "On/Off grid");

        onOffGrid.setSelected(false);
        onOffGrid.addActionListener(e -> {
            showGrid();
        });
        toolBar.add(onOffGrid);
        onOffIsoline = new JButton(new ImageIcon("./icons/lines.gif"));
        new ComponentHelpText(statusBar, onOffIsoline, "On/Off isolines");

        onOffIsoline.setSelected(true);
        onOffIsoline.addActionListener(e -> {
            showIsolines();
        });
        toolBar.add(onOffIsoline);
        toolBar.addSeparator();
        onOffInteractiveBuild = new JButton(new ImageIcon("./icons/isoline.gif"));
        new ComponentHelpText(statusBar, onOffInteractiveBuild, "On/Off interactive isolines building");

        onOffInteractiveBuild.setSelected(false);
        onOffInteractiveBuild.addActionListener(e -> {
            setBuildIsolainsMode();
        });
        toolBar.add(onOffInteractiveBuild);

        JButton clearIsolines = new JButton(new ImageIcon("./icons/Eraser.gif"));
        new ComponentHelpText(statusBar, clearIsolines, "Clear isolines");
        clearIsolines.addActionListener(e -> {
            paintArea.eraseIsolines();
            repaint();
        });
        toolBar.add(clearIsolines);
        toolBar.addSeparator();

        move = new JButton(new ImageIcon("./icons/move.gif"));
        new ComponentHelpText(statusBar, move, "Moving");
        move.addActionListener(e -> {
            setMove();
        });
        toolBar.add(move);

        JButton zoomIn = new JButton(new ImageIcon("./icons/zoom.gif"));
        new ComponentHelpText(statusBar, zoomIn, "Zoom in");
        zoomIn.addActionListener(e -> {
            paintArea.zoomIn();
        });
        toolBar.add(zoomIn);

        JButton zoomOut = new JButton(new ImageIcon("./icons/zoomout.gif"));
        new ComponentHelpText(statusBar, zoomOut, "Zoom out");

        zoomOut.addActionListener(e -> {
            paintArea.zoomOut();
        });
        toolBar.add(zoomOut);

        add(toolBar, BorderLayout.NORTH);
    }

    private void openSettingsDialog() {
        new SettingsDialog(paintArea);
    }

    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(FileUtils.getDataDirectory());
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Properties properties;
            try {
                IzoFileReader izoFileReader = new IzoFileReader(file);
                properties = izoFileReader.readProperties();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                paintArea.createMap(properties);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showConfirmDialog(this, "Uncorrected function "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("Open file...");
        new ComponentHelpText(statusBar, newItem, "Open configure file...");
        newItem.addActionListener(e -> open());
        fileMenu.add(newItem);

        JMenuItem setting = new JMenuItem("Function parameters...");
        new ComponentHelpText(statusBar, setting, "Set color and function parameters...");
        setting.addActionListener(e -> {
            new SettingsDialog(paintArea);
        });
        fileMenu.add(setting);

        fileMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        new ComponentHelpText(statusBar, exit, "Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(exit);
        menuBar.add(fileMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem colorItem = new JMenuItem("Color map");
        new ComponentHelpText(statusBar, colorItem, "On color map");
        colorItem.addActionListener(e -> setColorMode());
        viewMenu.add(colorItem);

        JMenuItem interpolationItem = new JMenuItem("Interpolation");
        new ComponentHelpText(statusBar, interpolationItem, "On interpolation");
        interpolationItem.addActionListener(e -> setInterpolationMode());
        viewMenu.add(interpolationItem);

        JMenuItem notColorItem = new JMenuItem("Hide color map");
        new ComponentHelpText(statusBar, notColorItem, "Off color map");
        notColorItem.addActionListener(e -> setEmptyMode());
        viewMenu.add(notColorItem);

        viewMenu.addSeparator();

        JMenuItem gridItem = new JMenuItem("Show/Hide Grid");
        new ComponentHelpText(statusBar, gridItem, "On/Off Grid");
        gridItem.addActionListener(e -> showGrid());
        viewMenu.add(gridItem);

        JMenuItem iItem = new JMenuItem("Show/Hide Isolines");
        new ComponentHelpText(statusBar, iItem, "On/Off Isolines");
        iItem.addActionListener(e -> showIsolines());
        viewMenu.add(iItem);
        menuBar.add(viewMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem isItem = new JMenuItem("Build Isolines");
        new ComponentHelpText(statusBar, isItem, "On/Off interactive isolines building");
        isItem.addActionListener(e -> setBuildIsolainsMode());
        editMenu.add(isItem);

        JMenuItem clItem = new JMenuItem("Clear Isolines");
        new ComponentHelpText(statusBar, clItem, "Clear isolines");
        clItem.addActionListener(e -> {
            paintArea.eraseIsolines();
            repaint();
        });
        editMenu.add(clItem);
        editMenu.addSeparator();

        JMenuItem moveItem = new JMenuItem("Move");
        new ComponentHelpText(statusBar, moveItem, "Moving");
        moveItem.addActionListener(e -> setMove());
        editMenu.add(moveItem);

        JMenuItem zoomOnItem = new JMenuItem("Zoom in");
        new ComponentHelpText(statusBar, zoomOnItem, "Zoom in");
        zoomOnItem.addActionListener(e -> paintArea.zoomIn());
        editMenu.add(zoomOnItem);

        JMenuItem zoomOutItem = new JMenuItem("Zoom out");
        new ComponentHelpText(statusBar, zoomOutItem, "Zoom out");
        zoomOutItem.addActionListener(e -> paintArea.zoomOut());
        editMenu.add(zoomOutItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About...");
        new ComponentHelpText(statusBar, about, "About program and author");
        about.addActionListener(e -> new AboutDialog(this));
        helpMenu.add(about);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void showIsolines() {
        if (!onOffIsoline.isSelected()) {
            onOffIsoline.setSelected(true);
            paintArea.setIsolineMode(true);
        } else {
            onOffIsoline.setSelected(false);
            paintArea.setIsolineMode(false);
        }
        repaint();
    }

    private void setBuildIsolainsMode() {
        if (!onOffInteractiveBuild.isSelected()) {
            onOffInteractiveBuild.setSelected(true);
            paintArea.setConstructionMode(true);
        } else {
            onOffInteractiveBuild.setSelected(false);
            paintArea.setConstructionMode(false);
        }
        repaint();
    }

    private void setEmptyMode() {
        if(!onEmptyMap.isSelected()){
            onEmptyMap.setSelected(true);
            paintArea.setColorMapMode(true);
            onOffInterpolation.setSelected(false);
            paintArea.setInterpolationMode(false);
            onOffColorMap.setSelected(false);
            paintArea.setColorMapMode(false);
        }
        repaint();
    }

    private void setInterpolationMode() {
        if(!onOffInterpolation.isSelected()){
            onOffInterpolation.setSelected(true);
            paintArea.setInterpolationMode(true);
            onOffColorMap.setSelected(false);
            paintArea.setColorMapMode(false);
            onEmptyMap.setSelected(false);
        }
        repaint();
    }

    private void setColorMode() {
        if(!onOffColorMap.isSelected()){
            onOffColorMap.setSelected(true);
            paintArea.setColorMapMode(true);
            onOffInterpolation.setSelected(false);
            paintArea.setInterpolationMode(false);
            onEmptyMap.setSelected(false);
        }
        repaint();
    }

    private void showGrid(){
        if(!onOffGrid.isSelected()){
            onOffGrid.setSelected(true);
            paintArea.setGridMode(true);
        }
        else {
            onOffGrid.setSelected(false);
            paintArea.setGridMode(false);
        }
        repaint();
    }

    private void setMove() {
        if (!move.isSelected()) {
            move.setSelected(true);
            paintArea.setRelocationMode(true);
        } else {
            move.setSelected(false);
            paintArea.setRelocationMode(false);
        }
    }

    public static void main(String[] args) {
        MainView mainView = new MainView();
    }

    @Override
    public void updateStatusBar(Cord cord, Double z) {
        statusBar.setCords(cord, z);
    }
}
