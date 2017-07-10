package ru.nsu.g13205.zharkova;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Yana on 04.03.16.
 */
public class MainView extends JFrame{

    private PaintArea paintArea;

    private JCheckBoxMenuItem select;
    private JCheckBoxMenuItem pixelSizeSelectionItem;
    private JButton selectButton;
    private JButton pixelButton;
    private JButton newDocument;
    private JButton save;
    private JButton leftArrow;
    private JButton rightArrow;
    private JButton zoom;
    private JButton rotate;
    private JButton gray;
    private JButton negative;
    private JButton gamma;
    private JButton floyd;
    private JButton ordered;
    private JButton blur;
    private JButton sharp;
    private JButton aqua;
    private JButton emboss;
    private JButton roberts;
    private JButton sobel;
    private JButton about;
    private JButton help;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem replaceCBItem;
    private JMenuItem replaceBCItem;
    private JMenuItem grayItem;
    private JMenuItem negativeItem;
    private JMenuItem disItem;
    private JMenuItem odItem;
    private JMenuItem sItem;
    private JMenuItem shItem;
    private JMenuItem aquaItem;
    private JMenuItem emItem;
    private JMenuItem isItem;
    private JMenuItem sobItem;
    private JMenuItem gammaItem;
    private JMenuItem zoomItem;
    private JMenuItem rotateItem;
    private JMenuItem aboutItem;
    private JMenuItem helpItem;

    public MainView(){
        super("FIT_13205_Zharkova_Filter");
        this.setSize(1200, 600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction();
            }
        });
        setLayout(new BorderLayout());
        createMenuBar();
        createToolBar();
        createStatusBar();
        createPaintArea();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void createPaintArea() {
        paintArea = new PaintArea();
        JScrollPane pane = new JScrollPane(paintArea);
        add(pane, BorderLayout.CENTER);
    }

    public void openBMP(){
        if (paintArea.getTransformImage()!=null){
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                saveBMP();
            }
            if(result == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.bmp)", "bmp");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(FileUtils.getDataDirectory());
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BMPImageReader bmpImageReader = new BMPImageReader(file);
                if(selectButton.isSelected()){
                    selectButton.doClick();
                }
                paintArea.pasteImage(bmpImageReader.readBMP());
            } catch (FileBMPException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void saveBMP() {
        if(paintArea.getTransformImage() == null){
            showWarningDialog("Not C Image");
            return;
        }
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.bmp)", "bmp");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(FileUtils.getDataDirectory());

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".bmp")) {
                file = new File(file.getAbsolutePath() + ".bmp");
            }
            BMPImageWriter bmpImageWriter = new BMPImageWriter(file);
            bmpImageWriter.saveBMP(paintArea.getTransformImage());
        }
    }

    private void exitAction(){
        if (paintArea.getTransformImage()!=null){
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                saveBMP();
            }
            if(result == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        System.exit(0);
    }

    private void createStatusBar() {
        StatusBar statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
        new ComponentHelpText(statusBar, openItem, newDocument, "Open new BMP image");
        new ComponentHelpText(statusBar, saveItem, save, "Save transformed image");
        new ComponentHelpText(statusBar, leftArrow, replaceCBItem, "Paste C -> B");
        new ComponentHelpText(statusBar, rightArrow, replaceBCItem, "Paste B->C");
        new ComponentHelpText(statusBar, zoom, zoomItem, "Zoom x2");
        new ComponentHelpText(statusBar, rotate, rotateItem, "Rotate image");
        new ComponentHelpText(statusBar, gray, grayItem, "Black/white Filter");
        new ComponentHelpText(statusBar, negative, negativeItem, "Negative filter");
        new ComponentHelpText(statusBar, gamma, gammaItem, "Gamma correction filter");
        new ComponentHelpText(statusBar, floyd, disItem, "Floyd-Steinberg dithering filter");
        new ComponentHelpText(statusBar, ordered, odItem, "Ordered Dithering filter");
        new ComponentHelpText(statusBar, shItem, sharp, "Sharpness filter");
        new ComponentHelpText(statusBar, aqua, aquaItem, "Aqua filter");
        new ComponentHelpText(statusBar, emboss, emItem, "Emboss filter");
        new ComponentHelpText(statusBar, blur, sItem, "Blur filter");
        new ComponentHelpText(statusBar, roberts, isItem, "Roberts outline filter");
        new ComponentHelpText(statusBar, sobel, sobItem, "Sobel outline filter");
        new ComponentHelpText(statusBar, about, aboutItem, "About program");
        new ComponentHelpText(statusBar, help, helpItem, "Show game rules");
        new ComponentHelpText(statusBar, select, selectButton, "Select clip image");
        new ComponentHelpText(statusBar, pixelSizeSelectionItem, pixelButton, "Pixelize mode");
    }

    private void createToolBar() {
        JToolBar northToolBar = new JToolBar();
        northToolBar.setFloatable(false);
        northToolBar.setRollover(true);
        newDocument = new JButton(new ImageIcon("./icons/open.gif"));
        newDocument.addActionListener(e -> openBMP());
        northToolBar.add(newDocument);

        save = new JButton(new ImageIcon("./icons/save.gif"));
        save.addActionListener(e -> saveBMP());
        northToolBar.add(save);

        northToolBar.addSeparator();

        selectButton = new JButton(new ImageIcon("./icons/select.gif"));
        selectButton.addActionListener(e -> {
            if(selectButton.isSelected()){
                selectButton.setSelected(false);
                select.setSelected(false);
                selectOutline();
            }
            else {
                selectButton.setSelected(true);
                select.setSelected(true);
                selectOutline();
            }
        });
        northToolBar.add(selectButton);

        pixelButton = new JButton(new ImageIcon("./icons/pixel.gif"));
        pixelButton.addActionListener(e -> {
            if(pixelButton.isSelected()){
                pixelButton.setSelected(false);
                pixelSizeSelectionItem.setSelected(false);
                pixelizeMode();
            }
            else {
                pixelButton.setSelected(true);
                pixelSizeSelectionItem.setSelected(true);
                pixelizeMode();
            }
        });
        northToolBar.add(pixelButton);
        northToolBar.addSeparator();
        leftArrow = new JButton(new ImageIcon("./icons/left.gif"));
        leftArrow.addActionListener(e -> CtoB());
        northToolBar.add(leftArrow);

        rightArrow = new JButton(new ImageIcon("./icons/right.gif"));
        rightArrow.addActionListener(e -> BtoC());
        northToolBar.add(rightArrow);

        zoom = new JButton(new ImageIcon("./icons/zoom.gif"));
        zoom.addActionListener(e -> zoom());
        northToolBar.add(zoom);

        rotate = new JButton(new ImageIcon("./icons/turn.gif"));
        rotate.addActionListener(e -> openAlgorithmDialog("Rotation", 0, 360, 0, "Angle"));
        northToolBar.add(rotate);

        northToolBar.addSeparator();

        help = new JButton(new ImageIcon("./icons/help.gif"));
        help.addActionListener(e -> openHelpDialog());
        northToolBar.add(help);

        about = new JButton(new ImageIcon("./icons/about.gif"));
        about.addActionListener(e -> showAbout());
        northToolBar.add(about);

        northToolBar.addSeparator();

        JToolBar westToolBar = new JToolBar(JToolBar.VERTICAL);
        westToolBar.setFloatable(false);
        westToolBar.setRollover(true);

        gray = new JButton(new ImageIcon("./icons/interface.gif"));
        gray.addActionListener(e -> grayFilter());
        westToolBar.add(gray);

        negative = new JButton(new ImageIcon("./icons/gray.gif"));
        negative.addActionListener(e -> negativeFilter());
        westToolBar.add(negative);

        gamma = new JButton(new ImageIcon("./icons/gamma.gif"));
        gamma.addActionListener(e -> openAlgorithmDialog("Gamma", 0, 1000, 100, "Gamma"));
        westToolBar.add(gamma);

        westToolBar.addSeparator();

        floyd = new JButton(new ImageIcon("./icons/dith.gif"));
        floyd.addActionListener(e -> openDitheringDialog("Floyd Dithering"));
        westToolBar.add(floyd);

        ordered = new JButton(new ImageIcon("./icons/order.gif"));
        ordered.addActionListener(e -> openDitheringDialog("Ordered Dithering"));
        westToolBar.add(ordered);

        westToolBar.addSeparator();

        blur = new JButton(new ImageIcon("./icons/blur.gif"));
        blur.addActionListener(e -> blurFilter());
        westToolBar.add(blur);

        sharp = new JButton(new ImageIcon("./icons/sharp.gif"));
        sharp.addActionListener(e -> sharpnessFilter());
        westToolBar.add(sharp);

        aqua = new JButton(new ImageIcon("./icons/brush.gif"));
        aqua.addActionListener(e -> aquaFilter());
        westToolBar.add(aqua);

        emboss = new JButton(new ImageIcon("./icons/emboss.gif"));
        emboss.addActionListener(e -> embossFilter());
        westToolBar.add(emboss);

        westToolBar.addSeparator();

        roberts = new JButton(new ImageIcon("./icons/outline.gif"));
        roberts.addActionListener(e -> openAlgorithmDialog("Roberts", 0, 360, 7, "Threshold"));
        westToolBar.add(roberts);

        sobel = new JButton(new ImageIcon("./icons/outline.gif"));
        sobel.addActionListener(e -> openAlgorithmDialog("Sobel", 0, 360, 7, "Threshold"));
        westToolBar.add(sobel);

        add(northToolBar, BorderLayout.NORTH);
        add(westToolBar, BorderLayout.WEST);

    }

    private void selectOutline(){
        if(select.isSelected()) {
            paintArea.setSelect(true);
        }
        if(!select.isSelected()){
            paintArea.setSelect(false);
        }
    }

    private void pixelizeMode(){
        if(pixelSizeSelectionItem.isSelected()){
            paintArea.setIsPixelSize(true);
        }
        else{
            paintArea.setIsPixelSize(false);
        }
    }

    private void BtoC(){
        paintArea.replaceBC();
    }

    private void CtoB(){
        paintArea.replace();
    }

   private void openDitheringDialog(String algorithm){
       if(paintArea.isEmptyClip()){
           showWarningDialog("Not Image B");
           return;
       }
       new DitheringDialog(paintArea, algorithm);
   }

    private void openAlgorithmDialog(String algorithm, int min, int max, int start, String label){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        new SettingDialog(paintArea, algorithm, min, max, start, label);
    }

    private void grayFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.grayScale();
    }

    private void negativeFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.negativeScale();
    }

    private void blurFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.blurFilter();
    }

    private void sharpnessFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.sharpnessFilter();
    }

    private void aquaFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.aquaFilter();
    }

    private void embossFilter(){
        if(paintArea.isEmptyClip()){
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.embossFilter();
    }

    private void zoom(){
        if (paintArea.isEmptyClip()) {
            showWarningDialog("Not Image B");
            return;
        }
        paintArea.zoomIn();
    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open...");
        openItem.addActionListener(e -> openBMP());
        fileMenu.add(openItem);
        saveItem = new JMenuItem("Save...");
        saveItem.addActionListener(e -> saveBMP());
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitAction());
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        select = new JCheckBoxMenuItem("Select");
        select.setSelected(false);
        select.addActionListener(e -> {
            if(select.isSelected()){
                selectButton.setSelected(true);
            }
            else {
                selectButton.setSelected(false);
            }
            selectOutline();
        });
        editMenu.add(select);
        pixelSizeSelectionItem = new JCheckBoxMenuItem("Pixelize");
        pixelSizeSelectionItem.setSelected(false);
        pixelSizeSelectionItem.addActionListener(e -> {
            if(pixelSizeSelectionItem.isSelected()){
                pixelButton.setSelected(true);
            }
            else {
                pixelButton.setSelected(false);
            }
            pixelizeMode();
        });
        editMenu.add(pixelSizeSelectionItem);

        JMenuItem pSizeItem = new JMenuItem("Pixel size...");
        pSizeItem.addActionListener(e -> new PixelizeDialog(paintArea));
        editMenu.add(pSizeItem);
        menuBar.add(editMenu);

        JMenu dragDropMenu = new JMenu("Replace");
        replaceCBItem = new JMenuItem("C -> B");
        replaceCBItem.addActionListener(e -> BtoC());
        dragDropMenu.add(replaceCBItem);
        replaceBCItem = new JMenuItem("B -> C");
        replaceBCItem.addActionListener(e -> CtoB());
        dragDropMenu.add(replaceBCItem);
        menuBar.add(dragDropMenu);

        JMenu filterItem = new JMenu("Filter");
        grayItem = new JMenuItem("Gray");
        grayItem.addActionListener(e -> grayFilter());
        filterItem.add(grayItem);

        negativeItem = new JMenuItem("Negative");
        negativeItem.addActionListener(e -> negativeFilter());
        filterItem.add(negativeItem);
        filterItem.addSeparator();

        disItem = new JMenuItem("Floyd Dithering");
        disItem.addActionListener(e -> openDitheringDialog("Floyd Dithering"));
        filterItem.add(disItem);

        odItem = new JMenuItem("Ordered Dithering");
        odItem.addActionListener(e -> openDitheringDialog("Ordered Dithering"));
        filterItem.add(odItem);
        filterItem.addSeparator();

        sItem = new JMenuItem("Blur");
        sItem.addActionListener(e -> blurFilter());
        filterItem.add(sItem);

        shItem = new JMenuItem("Sharpness");
        shItem.addActionListener(e -> sharpnessFilter());
        filterItem.add(shItem);

        aquaItem = new JMenuItem("Aqua");
        aquaItem.addActionListener(e -> aquaFilter());
        filterItem.add(aquaItem);

        emItem = new JMenuItem("Emboss");
        emItem.addActionListener(e -> embossFilter());
        filterItem.add(emItem);
        filterItem.addSeparator();

        isItem = new JMenuItem("Roberts Filter");
        isItem.addActionListener(e -> openAlgorithmDialog("Roberts", 0, 360, 7, "Threshold"));
        filterItem.add(isItem);

        sobItem = new JMenuItem("Sobel Filter");
        sobItem.addActionListener(e -> openAlgorithmDialog("Sobel", 0, 360, 7, "Threshold"));
        filterItem.add(sobItem);
        filterItem.addSeparator();

        gammaItem = new JMenuItem("Gamma correction");
        gammaItem.addActionListener(e -> openAlgorithmDialog("Gamma", 0, 1000, 100, "Gamma"));
        filterItem.add(gammaItem);
        zoomItem = new JMenuItem("Zoom");
        zoomItem.addActionListener(e -> zoom());
        filterItem.add(zoomItem);

        rotateItem = new JMenuItem("Rotation");
        rotateItem.addActionListener(e -> openAlgorithmDialog("Rotation", 0, 360, 0, "Angle"));
        filterItem.add(rotateItem);

        menuBar.add(filterItem);

        JMenu helpMenu = new JMenu("Help");
        helpItem = new JMenuItem("Help...");
        helpItem.addActionListener(e -> openHelpDialog());
        helpMenu.add(helpItem);
        aboutItem = new JMenuItem("About...");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void openHelpDialog() {
        new HelpDialog(this);
    }

    private void showAbout(){
        new AboutDialog(this);
    }


    private void showWarningDialog(String reason) {
        JOptionPane.showMessageDialog(this, reason, null, JOptionPane.WARNING_MESSAGE);
    }


    public static void main(String[] args) {
        MainView mainView = new MainView();
    }

}
