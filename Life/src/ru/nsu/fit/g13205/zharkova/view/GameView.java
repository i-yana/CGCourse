package ru.nsu.fit.g13205.zharkova.view;

import ru.nsu.cg.FileUtils;
import ru.nsu.fit.g13205.zharkova.model.DefaultSetting;
import ru.nsu.fit.g13205.zharkova.model.Properties;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 11.02.16.
 */
public class GameView extends JFrame{

    private GridPanel gridPanel;
    private JToolBar toolBar;
    private StatusBar statusBar;
    private FileLoader fileLoader;
    private GridSettings gridSettings;
    private Timer timer;
    private JScrollPane pane;
    private JButton replace;
    private JButton xor;
    private JButton impactB;
    private JButton newDocument;
    private JButton open;
    private JButton save;
    private JButton saveAs;
    private JButton setting;
    private JButton gameOpt;
    private JButton next;
    private JButton play;
    private JButton pause;
    private JButton delete;
    private JButton about;
    private JButton help;

    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem gridSettingsItem;
    private JMenuItem gameSettingsItem;
    private JMenuItem defaultSettingsItem;
    private JMenuItem exitItem;

    private JMenuItem nextStepItem;
    private JMenuItem playItem;
    private JMenuItem pauseItem;
    private JMenuItem deleteItem;

    private JCheckBoxMenuItem toolbarItem;
    private JCheckBoxMenuItem statusBarItem;

    private JMenuItem aboutItem;
    private JMenuItem helpItem;

    public GameView(){
        super("FIT_13205_Zharkova_Life");
        this.setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                openSaveDialog();
            }
        });
        setLayout(new BorderLayout());
        gridSettings = new GridSettings();
        createMenuBar();
        createToolBar();
        createStatusBar();
        createPaintArea();
        setButtons();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        fileLoader = new FileLoader(this);
        timer = new Timer(DefaultSetting.TIMER, e -> gridPanel.notifyAboutUpdate());
    }

    @Override
    public void paintComponents(Graphics graphics){
        super.paintComponents(graphics);
        pane.updateUI();
    }

    @Override
    public void repaint(){
        super.repaint();
        pane.updateUI();
    }

    private void openSaveDialog() {
        if (gridSettings.isChanges()){
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                save();
                if(gridSettings.isChanges()){
                    return;
                }
            }
            if(result == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        System.exit(0);
    }

    private void createStatusBar() {
        statusBar = new StatusBar();
        new ComponentHelpText(statusBar, newItem, newDocument, "Create new document");
        new ComponentHelpText(statusBar, openItem, open, "Open document");
        new ComponentHelpText(statusBar, saveItem, save, "Save document");
        new ComponentHelpText(statusBar, saveAsItem, saveAs, "Save as...");
        new ComponentHelpText(statusBar, setting, gridSettingsItem, "Change grid parameters...");
        new ComponentHelpText(statusBar, gameOpt, gameSettingsItem, "Change Game parameters...");
        new ComponentHelpText(statusBar, impactB, "Show/hide impact");
        new ComponentHelpText(statusBar, replace, "XOR mode");
        new ComponentHelpText(statusBar, xor, "Replace mode");
        new ComponentHelpText(statusBar, defaultSettingsItem, "Set Default settings...");
        new ComponentHelpText(statusBar, exitItem, "Exit...");
        new ComponentHelpText(statusBar, next, nextStepItem, "Next step...");
        new ComponentHelpText(statusBar, play, playItem, "Play...");
        new ComponentHelpText(statusBar, pause, pauseItem, "Pause");
        new ComponentHelpText(statusBar, delete, deleteItem, "Clear field");
        new ComponentHelpText(statusBar, toolbarItem, "Show/hide Toolbar");
        new ComponentHelpText(statusBar, statusBarItem, "Show/hide Status Bar");
        new ComponentHelpText(statusBar, about, aboutItem, "About program");
        new ComponentHelpText(statusBar, help, helpItem, "Show game rules");
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    public void createPaintArea(){
        gridPanel = new GridPanel(gridSettings);
        pane = new JScrollPane(gridPanel);
        add(pane, BorderLayout.CENTER);
    }

    public void fillBoard(int gridWidth, int gridHeight, ArrayList<Point> aliveCells) {
        gridPanel.createNewGrid(gridWidth, gridHeight, aliveCells);
        repaint();
    }

    private void createMenuBar(){

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        newItem = new JMenuItem("New...");
        newItem.addActionListener(e -> createDocumentDialog());
        fileMenu.add(newItem);

        openItem = new JMenuItem("Open...");
        openItem.addActionListener(e -> openFileDialog());
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(e -> save());

        saveAsItem = new JMenuItem("Save As...");
        fileMenu.add(saveAsItem);
        saveAsItem.addActionListener(e -> saveAs());

        fileMenu.addSeparator();

        gridSettingsItem = new JMenuItem("Configure grid...");
        gridSettingsItem.addActionListener(e -> openSettingDialog());
        fileMenu.add(gridSettingsItem);

        gameSettingsItem = new JMenuItem("Game options...");
        gameSettingsItem.addActionListener(e -> openGameOptionsDialog());
        fileMenu.add(gameSettingsItem);

        defaultSettingsItem = new JMenuItem("Default Settings");
        defaultSettingsItem.addActionListener(e -> {
            gridSettings.setDefault();
            gridPanel.setProperties(Properties.DEFAULT_SETTINGS);
            gridPanel.setSetting(gridPanel.getGridWidth(), gridPanel.getGridHeight());
            JOptionPane.showMessageDialog(gridPanel, "Default settings installed!", "Life", JOptionPane.INFORMATION_MESSAGE);
        });
        fileMenu.add(defaultSettingsItem);
        fileMenu.addSeparator();
        exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(e -> openSaveDialog());

        JMenu editMenu = new JMenu("Edit");
        nextStepItem = new JMenuItem("Next step", new ImageIcon("./icons/Right.gif"));
        nextStepItem.addActionListener(e -> gridPanel.notifyAboutUpdate());
        editMenu.add(nextStepItem);

        playItem = new JMenuItem("Play...", new ImageIcon("./icons/Play.gif"));
        playItem.addActionListener(e -> startTimer());
        editMenu.add(playItem);

        pauseItem = new JMenuItem("Pause", new ImageIcon("./icons/pause1.gif"));
        pauseItem.addActionListener(e -> pauseTimer());
        editMenu.add(pauseItem);
        editMenu.addSeparator();
        deleteItem = new JMenuItem("Clear");
        deleteItem.addActionListener(e -> {
            gridPanel.fillGrid(new ArrayList<>());
            gridPanel.updateImage();
        });
        editMenu.add(deleteItem);
        JMenu viewMenu = new JMenu("View");
        toolbarItem = new JCheckBoxMenuItem("Toolbar");
        toolbarItem.setSelected(true);
        toolbarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!toolbarItem.isSelected()) {
                    toolBar.setVisible(false);
                }
                if (toolbarItem.isSelected()) {
                    toolBar.setVisible(true);
                }
            }
        });
        statusBarItem = new JCheckBoxMenuItem("Status Bar");
        statusBarItem.setSelected(true);
        statusBarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!statusBarItem.isSelected()) {
                    statusBar.setVisible(false);
                }
                if (statusBarItem.isSelected()) {
                    statusBar.setVisible(true);
                }
            }
        });
        viewMenu.add(toolbarItem);
        viewMenu.add(statusBarItem);
        JMenu helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About program...");
        aboutItem.addActionListener(e -> openAboutDialog());
        helpMenu.add(aboutItem);

        helpItem = new JMenuItem("Help...");
        helpItem.addActionListener(e -> openHelpDialog());
        helpMenu.add(helpItem);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void openAboutDialog() {
        new AboutDialog(this);
    }

    private void openHelpDialog() {
        new HelpDialog(this);
    }

    private void pauseTimer() {
        timer.stop();
        gridPanel.setFocusable(true);
        setButtonsEnabled(true);
    }

    private void startTimer() {
        timer.restart();
        gridPanel.setFocusable(false);
        setButtonsEnabled(false);
    }

    private void setButtonsEnabled(boolean enabled){
        newDocument.setEnabled(enabled);
        newItem.setEnabled(enabled);
        open.setEnabled(enabled);
        openItem.setEnabled(enabled);
        setting.setEnabled(enabled);
        defaultSettingsItem.setEnabled(enabled);
        gameSettingsItem.setEnabled(enabled);
        gridSettingsItem.setEnabled(enabled);
        gameOpt.setEnabled(enabled);
        next.setEnabled(enabled);
        nextStepItem.setEnabled(enabled);
        play.setEnabled(enabled);
        playItem.setEnabled(enabled);
        delete.setEnabled(enabled);
        deleteItem.setEnabled(enabled);
    }


    private void openGameOptionsDialog() {
        GameOptions gameOptions = new GameOptions(gridPanel);
        gameOptions.setLocationRelativeTo(this);
        gameOptions.openDialog();
    }

    private void openSettingDialog() {
        new SettingDialog(this);
        repaint();
    }

    public void createToolBar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        newDocument = new JButton(new ImageIcon("./icons/new.gif"));
        newDocument.addActionListener(e -> createDocumentDialog());
        toolBar.add(newDocument);
        open = new JButton(new ImageIcon("./icons/op.gif"));
        open.addActionListener(e -> openFileDialog());
        toolBar.add(open);
        save = new JButton(new ImageIcon("./icons/sav.gif"));
        save.addActionListener(e -> save());
        toolBar.add(save);
        saveAs = new JButton(new ImageIcon("./icons/save-as.gif"));
        saveAs.addActionListener(e -> saveAs());
        toolBar.add(saveAs);
        toolBar.addSeparator();
        impactB = new JButton(new ImageIcon("./icons/impact.gif"));
        toolBar.add(impactB);
        impactB.addActionListener(e -> {
            if(impactB.isSelected()){
                impactB.setSelected(false);
                gridPanel.eraseImpact();
                gridSettings.setImpact(false);
                gridPanel.updateImage();
            }
            else {
                impactB.setSelected(true);
                gridSettings.setImpact(true);
                gridPanel.updateImage();
            }
        });
        replace = new JButton(new ImageIcon("./icons/xor.gif"));
        xor = new JButton(new ImageIcon("./icons/replace.gif"));
        xor.addActionListener(e -> {
            gridSettings.setReplace(false);
            xor.setSelected(true);
            replace.setSelected(false);
        });
        toolBar.add(xor);
        replace.addActionListener(e -> {
            gridSettings.setReplace(true);
            replace.setSelected(true);
            xor.setSelected(false);
        });
        toolBar.add(replace);

        gameOpt = new JButton(new ImageIcon("./icons/setting.gif"));
        gameOpt.addActionListener(e -> openGameOptionsDialog());
        toolBar.add(gameOpt);

        setting = new JButton(new ImageIcon("./icons/game-op.gif"));
        setting.addActionListener(e -> openSettingDialog());
        toolBar.add(setting);

        toolBar.addSeparator();
        next = new JButton(new ImageIcon("./icons/next.gif"));
        toolBar.add(next);
        next.addActionListener(e -> gridPanel.notifyAboutUpdate());
        play = new JButton(new ImageIcon("./icons/pl.gif"));
        play.addActionListener(e -> startTimer());
        toolBar.add(play);
        pause = new JButton(new ImageIcon("./icons/Pause.gif"));
        pause.addActionListener(e -> pauseTimer());
        toolBar.add(pause);
        delete = new JButton(new ImageIcon("./icons/delete.gif"));
        delete.addActionListener(e -> {
            gridPanel.fillGrid(new ArrayList<>());
            gridPanel.updateImage();
        });
        toolBar.add(delete);
        toolBar.addSeparator();
        about = new JButton(new ImageIcon("./icons/info.gif"));
        about.addActionListener(e -> openAboutDialog());
        toolBar.add(about);
        help = new JButton(new ImageIcon("./icons/help.gif"));
        help.addActionListener(e -> openHelpDialog());
        toolBar.add(help);
        add(toolBar, BorderLayout.NORTH);
    }

    public void setButtons() {
        if(gridSettings.isReplace()){
            replace.doClick();
        }
        else {
            xor.doClick();
        }
        if(gridSettings.isImpact()){
            if(!impactB.isSelected()){
                impactB.doClick();
            }
        }
        else {
            if(impactB.isSelected()){
                impactB.doClick();
            }
        }
    }

    private void openFileDialog() {
        if (gridSettings.isChanges()){
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                save();
                if(gridSettings.isChanges()){
                    return;
                }
            }
            if(result == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(FileUtils.getDataDirectory());
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                fileLoader.load(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Unknown file format", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void save() {
        if(gridSettings.getCurrentFile() == null){
            saveAs();
            return;
        }
        if(gridSettings.isChanges()){
            try {
                fileLoader.save(gridSettings.getCurrentFile(), gridPanel);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void saveAs(){
        if(gridPanel.isEmpty()){
            JOptionPane.showMessageDialog(this, "Empty field", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.txt)", "txt");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(FileUtils.getDataDirectory());
        do {
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    if (!file.getAbsolutePath().endsWith(".txt")) {
                        file = new File(file.getAbsolutePath() + ".txt");
                    }
                    fileLoader.save(file, gridPanel);
                    break;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
            }
            break;
        } while (true);
    }

    private void createDocumentDialog(){
        if (gridSettings.isChanges()){
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION){
                save();
                if(gridSettings.isChanges()){
                    return;
                }
            }
            if(result == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        new DocumentDialog(this);
    }

    public void subscribeOnChange(GridViewListener listener){
        gridPanel.subscribeToChange(listener);
    }

    public void updateGrid() {
        gridPanel.updateImage();
    }

    public GridSettings getGridSettings() {
        return gridSettings;
    }

    public int getGridWidth() {
        return gridPanel.getGridWidth();
    }

    public int getGridHeight() {
        return gridPanel.getGridHeight();
    }

    public void setSetting(int width, int height) {
        gridPanel.setSetting(width, height);
    }
}
