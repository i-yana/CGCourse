package ru.nsu.fit.g13205.zharkova.raytracing.view;

import javafx.util.Pair;
import ru.nsu.fit.g13205.zharkova.raytracing.io.FileUtils;
import ru.nsu.fit.g13205.zharkova.raytracing.io.RenderFileReader;
import ru.nsu.fit.g13205.zharkova.raytracing.io.RenderFileWriter;
import ru.nsu.fit.g13205.zharkova.raytracing.io.SceneFileReader;
import ru.nsu.fit.g13205.zharkova.raytracing.model.RenderSetting;
import ru.nsu.fit.g13205.zharkova.raytracing.model.Scene;
import ru.nsu.fit.g13205.zharkova.raytracing.model.raytracing.Raytracing;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Yana on 14.05.16.
 */
public class Frame extends JFrame implements PanelListener {
    private View3D view3D;
    private JProgressBar pbProgress = new JProgressBar();
    JButton openSceneFile;
    JButton openRender;
    JButton save;
    JButton init;
    JButton renderSetting;
    JButton selectView;
    JButton renderScene;
    JButton saveImage;
    JMenuItem startItem;
    JMenuItem viewItem;
    JMenuItem settingItem;
    JMenuItem initItem;
    JToolBar toolBar = new JToolBar();
    private WorldWaiter worldWaiter;
    public int HEIGHT=0;

    public Frame() {
        super("FIT_13205_Zharkova_Raytracing");
        setMinimumSize(new Dimension(450,450));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createStatusBar();
        createMenuBar();
        createToolBar();
        worldWaiter = new WorldWaiter();
        view3D = new View3D(this);
        add(view3D, BorderLayout.CENTER);
        pbProgress.setString("");
        pbProgress.setStringPainted(true);
        add(pbProgress, BorderLayout.SOUTH);
        pack();
        HEIGHT = toolBar.getHeight() + pbProgress.getHeight()+getJMenuBar().getHeight();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("Open scene...");
        newItem.addActionListener(e -> openScene());
        fileMenu.add(newItem);

        JMenuItem renderFileItem = new JMenuItem("Open render...");
        renderFileItem.addActionListener(e -> openRender());
        fileMenu.add(renderFileItem);

        JMenuItem saveItem = new JMenuItem("Save render...");
        saveItem.addActionListener(e -> saveRender());
        fileMenu.add(saveItem);

        fileMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(exit);

        JMenu renderMenu = new JMenu("Rendering");
        startItem = new JMenuItem("Start...");
        startItem.addActionListener(e -> {
            render();
        });
        renderMenu.add(startItem);

        settingItem = new JMenuItem("Render Properties...");
        settingItem.addActionListener(e -> {
            renderSetting();
        });
        renderMenu.add(settingItem);

        JMenu sceneMenu = new JMenu("Scene");
        viewItem = new JMenuItem("Select view");
        viewItem.addActionListener(e -> {
            viewItem.setEnabled(false);
            selectView.setEnabled(false);
            renderScene.setEnabled(true);
            renderSetting.setEnabled(true);
            startItem.setEnabled(true);
            settingItem.setEnabled(true);
            view3D.setRender(false);
            view3D.sceneRotate(true);
            view3D.repaint();
        });
        viewItem.setEnabled(false);
        sceneMenu.add(viewItem);

        initItem = new JMenuItem("Init");
        initItem.addActionListener(e -> {
            if(view3D.getScene()==null){
                JOptionPane.showMessageDialog(this, "Please, load scene.", "Raytracing", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view3D.init();
        });
        sceneMenu.add(initItem);

        JMenu about = new JMenu("About");
        JMenuItem aboutAuthor = new JMenuItem("About author");
        aboutAuthor.addActionListener(e->openAbout());
        about.add(aboutAuthor);

        menuBar.add(fileMenu);
        menuBar.add(renderMenu);
        menuBar.add(sceneMenu);
        menuBar.add(about);
        setJMenuBar(menuBar);
    }

    private void openAbout() {
        new AboutDialog(this);
    }

    private void render(){
        if(view3D.getScene()==null){
            JOptionPane.showMessageDialog(this, "Please, load scene.", "Raytracing", JOptionPane.WARNING_MESSAGE);
            return;
        }
        view3D.sceneRotate(false);
        Raytracing raytracing = new Raytracing(view3D.getDimension(), view3D.getRenderSetting(), view3D.getScene(), view3D.getSceneMatrix(), worldWaiter);
        raytracing.doWork();
    }

    private void renderSetting(){
        if(view3D.getRenderSetting()==null){
            JOptionPane.showMessageDialog(this, "Please, load scene.", "Raytracing", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new SettingDialog(Frame.this, view3D.getRenderSetting());
    }


    private void openScene(){
        Pair<Scene, RenderSetting> parameters = openSceneFile();
        if (parameters != null) {
            initItem.setEnabled(true);
            init.setEnabled(true);
            view3D.createArea(parameters.getKey(), parameters.getValue());
            initButtons();
        }
    }

    private void openRender(){
        RenderSetting renderSetting = openRenderFile();
        if (renderSetting != null) {
            view3D.setRenderSettings(renderSetting);
            initButtons();
            view3D.setRender(false);
            view3D.repaint();
        }
    }

    private void createToolBar() {

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        openSceneFile = new JButton(new ImageIcon("./icons/open.gif"));
        openSceneFile.setToolTipText("Open scene file");
        openSceneFile.addActionListener(e -> {
            openScene();
        });
        toolBar.add(openSceneFile);
        openRender = new JButton(new ImageIcon("./icons/open.gif"));
        openRender.setToolTipText("Open render file");
        openRender.addActionListener(e -> {
            openRender();
        });
        toolBar.add(openRender);
        save = new JButton(new ImageIcon("./icons/save.gif"));
        save.setToolTipText("Save render file");
        save.addActionListener(e -> {
            saveRender();
        });

        toolBar.add(save);
        toolBar.addSeparator();
        renderSetting = new JButton(new ImageIcon("./icons/function.gif"));
        renderSetting.setToolTipText("Render settings");
        renderSetting.addActionListener(e -> {
            renderSetting();
        });
        toolBar.add(renderSetting);
        init = new JButton(new ImageIcon("./icons/init.gif"));
        init.setToolTipText("Set camera in center of scene");
        init.addActionListener(e -> {
            if(view3D.getScene()==null){
                JOptionPane.showMessageDialog(this, "Please, load scene.", "Raytracing", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view3D.init();
        });
        toolBar.add(init);


        renderScene = new JButton(new ImageIcon("./icons/color.gif"));
        renderScene.setToolTipText("Render scene");
        selectView = new JButton(new ImageIcon("./icons/axis.gif"));
        selectView.setToolTipText("Select wireframe mode");
        selectView.addActionListener(e -> {
            if(selectView.isEnabled()) {
                selectView.setEnabled(false);
                viewItem.setEnabled(false);
                renderScene.setEnabled(true);
                startItem.setEnabled(true);
                settingItem.setEnabled(true);
                renderSetting.setEnabled(true);
                view3D.setRender(false);
                view3D.sceneRotate(true);
                view3D.repaint();
            }
        });
        selectView.setEnabled(false);
        toolBar.add(selectView);


        renderScene.addActionListener(e -> {
            render();
        });
        toolBar.add(renderScene);

        saveImage = new JButton(new ImageIcon("./icons/sharp.gif"));
        saveImage.setToolTipText("Save image PNG");
        saveImage.addActionListener(e -> {
            BufferedImage image = view3D.getCurrentImage();
            if(image==null){
                JOptionPane.showMessageDialog(this, "No image.", "Raytracing", JOptionPane.WARNING_MESSAGE);
                return;
            }
            saveBMP(image);
        });
        toolBar.add(saveImage);


        add(toolBar, BorderLayout.NORTH);
    }

    private void initButtons() {
        init.setEnabled(true);
        initItem.setEnabled(true);
        renderSetting.setEnabled(true);
        settingItem.setEnabled(true);
        renderScene.setEnabled(true);
        startItem.setEnabled(true);
        selectView.setEnabled(false);
        viewItem.setEnabled(false);
        view3D.sceneRotate(true);
    }

    private void saveBMP(BufferedImage image) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.bmp)", "bmp");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(FileUtils.getDataDirectory());

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".bmp")) {
                file = new File(file.getAbsolutePath() + ".bmp");
            }
            try {
                ImageIO.write(image, "PNG", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveRender() {
        if(view3D.getRenderSetting()==null){
            JOptionPane.showMessageDialog(this, "Please, load scene.", "Raytracing", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.render)", "render");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(FileUtils.getDataDirectory());

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".render")) {
                file = new File(file.getAbsolutePath() + ".render");
            }
            try {
                RenderFileWriter writer = new RenderFileWriter(file);
                writer.write(view3D.getRenderSetting(), view3D.getSceneMatrix());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createStatusBar() {
        StatusBar statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JFileChooser createFileChooser(String extension) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*." + extension + ")", extension);
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(FileUtils.getDataDirectory());
        return fileChooser;
    }


    private Pair<Scene, RenderSetting> openSceneFile() {
        JFileChooser fileChooser = createFileChooser("scene");
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            Scene scene;
            RenderSetting renderSetting = null;
            File rendFile = null;
            File file = fileChooser.getSelectedFile();
            String dir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/"));
            String pref = file.getName().substring(0, file.getName().indexOf("."));
            File directory = new File(dir);
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                assert files != null;
                for (File f : files) {
                    if (f.getName().startsWith(pref) && f.getName().endsWith(".render")) {
                        rendFile = f;
                        RenderFileReader renderFileReader = new RenderFileReader(f);
                        try {
                            renderSetting = renderFileReader.read();
                            break;
                        } catch (IOException | ParserConfigurationException e) {
                            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
            try {
                SceneFileReader configFileReader = new SceneFileReader(file);
                scene = configFileReader.read();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid line: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Loading files:\n").append(file.getName());
            if (renderSetting != null) {
                sb.append(", ").append(rendFile.getName());
            }
            //JOptionPane.showMessageDialog(this, sb.toString(), "Error", JOptionPane.WARNING_MESSAGE);
            return new Pair<>(scene, renderSetting);
        }
        return null;
    }

    private RenderSetting openRenderFile() {
        JFileChooser fileChooser = createFileChooser("render");
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            RenderSetting properties;
            try {
                RenderFileReader configFileReader = new RenderFileReader(file);
                properties = configFileReader.read();
                if (properties.getDepth() > RenderSetting.MAX_DEPTH) {
                    JOptionPane.showMessageDialog(this, "Raytracing depth will be " + RenderSetting.MAX_DEPTH, "Raytracing", JOptionPane.WARNING_MESSAGE);
                    properties.setMaxDepth();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid line: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            //JOptionPane.showMessageDialog(this, "File " + file + "was loaded", "Error", JOptionPane.WARNING_MESSAGE);
            return properties;
        }
        return null;
    }

    public void updateView3D() {
        view3D.paintObjects();
        view3D.setRender(false);
    }

    @Override
    public void handleResizeMainView(int w, int h) {
        view3D.setPreferredSize(new Dimension(w, h));
        view3D.setSize(w,h);
        pack();
    }

    public class WorldWaiter implements WorldListener {
        private int count;
        private BufferedImage image;
        private long lastTimePainted;

        @Override
        public void traceStarted() {

            image = view3D.createTextureImage();
            pbProgress.setMinimum(0);
            pbProgress.setMaximum(image.getWidth() * image.getHeight());
            count = 0;
            EventQueue.invokeLater(() -> {
                        buttonEnable(false);
                        pbProgress.setString("Drawing...");
                    }
            );
            lastTimePainted = System.currentTimeMillis();
        }

        @Override
        public void pixelTraced() {
            count++;
            if (System.currentTimeMillis() - lastTimePainted > 500) {
                pbProgress.setValue(count);
                pbProgress.setString(String.format("Drawing : %.2f%% - pixel %d of %d",
                        count * 100.0 / pbProgress.getMaximum(), count, pbProgress.getMaximum()));
                lastTimePainted = System.currentTimeMillis();
            }
        }

        @Override
        public void traceFinished(float[][] red, float[][] green, float[][] blue) {
            EventQueue.invokeLater(() -> {
                        view3D.setRender(true);
                        renderScene.setEnabled(false);
                        startItem.setEnabled(false);
                        selectView.setEnabled(true);
                        viewItem.setEnabled(true);
                        init.setEnabled(true);
                        initItem.setEnabled(true);
                        pbProgress.setValue(0);
                        pbProgress.setString("Done!");
                    }
            );
            for (int i = 0; i < red.length; i++) {
                for (int j = 0; j < red[i].length; j++) {
                    image.setRGB(j,i, new Color(red[i][j], green[i][j], blue[i][j]).getRGB());
                }
            }
            repaint();
        }


    }

    private void buttonEnable(boolean b) {
        selectView.setEnabled(b);
        viewItem.setEnabled(b);
        renderScene.setEnabled(b);
        startItem.setEnabled(b);
        settingItem.setEnabled(b);
        init.setEnabled(b);
        initItem.setEnabled(b);
        renderSetting.setEnabled(b);
    }
}