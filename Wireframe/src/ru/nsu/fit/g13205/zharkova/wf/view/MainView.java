package ru.nsu.fit.g13205.zharkova.wf.view;

import ru.nsu.fit.g13205.zharkova.wf.model.tools.ConfigFileReader;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.ConfigFileWriter;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.FileUtils;
import ru.nsu.fit.g13205.zharkova.wf.model.tools.Properties;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Yana on 06.04.16.
 */
public class MainView extends JFrame{
    public static final int MIN_SIZE = 500;
    private View3D view3D;
    private StatusBar statusBar;
    private JButton funcSettings;

    public MainView(){
        super("FIT_13205_Zharkova_WF");
        this.setMinimumSize(new Dimension(MIN_SIZE, MIN_SIZE));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createStatusBar();
        createMenuBar();
        createToolBar();
        createPaintArea();
        this.setVisible(true);
        pack();
        this.setLocationRelativeTo(null);

    }

    private void createPaintArea() {
        view3D = new View3D();
        JScrollPane pane = new JScrollPane(view3D);
        add(pane, BorderLayout.CENTER);
        view3D.createArea(new Properties());
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

        JButton save = new JButton(new ImageIcon("./icons/save.gif"));
        new ComponentHelpText(statusBar, save, "Save scene in file");
        save.addActionListener(e->save());
        toolBar.add(save);
        toolBar.addSeparator();

        funcSettings = new JButton(new ImageIcon("./icons/function.gif"));
        new ComponentHelpText(statusBar, funcSettings, "Set color and function parameters...");
        funcSettings.addActionListener(e -> openSettingsDialog());
        toolBar.add(funcSettings);

        JButton init = new JButton(new ImageIcon("./icons/remove.gif"));
        new ComponentHelpText(statusBar, init, "Reset rotate");
        init.addActionListener(e->view3D.resetRotateScene());
        toolBar.add(init);

        toolBar.addSeparator();

        add(toolBar, BorderLayout.NORTH);
    }

    private void save() {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.txt)", "txt");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(FileUtils.getDataDirectory());

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            ConfigFileWriter configFileWriter = new ConfigFileWriter(file);
            try {
                configFileWriter.save(view3D.getProperties());
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

    }

    private void openSettingsDialog() {
        new SettingsDialog(this, view3D.getProperties());
        funcSettings.setEnabled(false);
    }

    public void enableSettingButton(){
        funcSettings.setEnabled(true);
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
                ConfigFileReader configFileReader = new ConfigFileReader(file);
                properties = configFileReader.read();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view3D.createArea(properties);
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
            new SettingsDialog(this,new Properties());
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


        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About...");
        new ComponentHelpText(statusBar, about, "About program and author");
        about.addActionListener(e -> new AboutDialog(this));
        helpMenu.add(about);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }




    public static void main(String[] args) {
        MainView mainView = new MainView();
    }


    public View3D getView3D() {
        return view3D;
    }
}
