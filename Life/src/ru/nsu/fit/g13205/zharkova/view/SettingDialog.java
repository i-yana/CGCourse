package ru.nsu.fit.g13205.zharkova.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Yana on 15.02.16.
 */
public class SettingDialog extends JDialog {

    private static final int MAX_CELL_SIZE = 100;
    private static final int MIN_CELL_SIZE = 6;
    private static final int MAX_LINE_SIZE = 30;
    private GameView gameView;
    private GridSettings gridSettings;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField lineSizeField;
    private JTextField cellSizeField;
    private JSlider lineSizeSlider;
    private JSlider cellSizeSlider;
    private JRadioButton rButtonXor;
    private JRadioButton rButtonReplace;
    private JCheckBox impact;

    public SettingDialog(GameView gameView) {
        super();
        setModal(true);
        setResizable(false);
        this.gameView = gameView;
        this.gridSettings = gameView.getGridSettings();
        Box mainBox = Box.createVerticalBox();
        Box rButtonBox = Box.createHorizontalBox();
        rButtonXor = new JRadioButton("XOR");
        rButtonReplace = new JRadioButton("Replace");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rButtonXor);
        bg.add(rButtonReplace);
        rButtonBox.add(rButtonXor);
        rButtonBox.add(rButtonReplace);
        rButtonBox.setBorder(new TitledBorder("Mode"));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(rButtonBox);

        Box fieldSizeBox = Box.createVerticalBox();
        Box widthBox = Box.createHorizontalBox();
        JLabel widthLabel = new JLabel(" width");
        widthField = new JTextField(5);
        widthBox.add(widthLabel);
        widthBox.add(widthField);

        Box heightBox = Box.createHorizontalBox();
        JLabel heightLabel = new JLabel("height");
        heightField = new JTextField(5);
        heightBox.add(heightLabel);
        heightBox.add(heightField);

        fieldSizeBox.add(widthBox);
        fieldSizeBox.add(heightBox);
        fieldSizeBox.setBorder(new TitledBorder("Field"));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(fieldSizeBox);
        Box checkBox = Box.createHorizontalBox();
        checkBox.setBorder(new TitledBorder("Impact"));
        impact = new JCheckBox("Impact");
        checkBox.add(impact);
        checkBox.add(Box.createHorizontalStrut(60));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(checkBox);

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(20));
        box.add(mainBox);
        box.add(Box.createHorizontalStrut(20));

        Box sliderBox = Box.createVerticalBox();
        lineSizeSlider = new JSlider(JSlider.VERTICAL, 1 , MAX_LINE_SIZE, 1);
        lineSizeSlider.addChangeListener(e -> lineSizeField.setText(String.valueOf(lineSizeSlider.getValue())));
        Box lineTextBox = Box.createHorizontalBox();
        JLabel lineSizeLabel = new JLabel("line:");
        lineSizeField = new JTextField(3);
        lineSizeField.addActionListener(e -> {
            try {
                lineSizeSlider.setValue(Integer.parseInt(lineSizeField.getText()));
            }catch (NumberFormatException ignored){};
        });
        lineSizeSlider.setMajorTickSpacing(MAX_LINE_SIZE-1);
        lineSizeSlider.setPaintLabels(true);
        lineTextBox.add(lineSizeLabel);
        lineTextBox.add(lineSizeField);
        sliderBox.add(Box.createVerticalStrut(20));
        sliderBox.add(lineSizeSlider);
        sliderBox.add(lineTextBox);
        box.add(sliderBox);
        box.add(Box.createHorizontalStrut(20));

        Box cellSliderBox = Box.createVerticalBox();
        cellSliderBox.add(Box.createVerticalStrut(20));
        cellSizeSlider = new JSlider(JSlider.VERTICAL, MIN_CELL_SIZE, MAX_CELL_SIZE, MIN_CELL_SIZE);
        cellSizeSlider.addChangeListener(e -> cellSizeField.setText(String.valueOf(cellSizeSlider.getValue())));
        Box cellTextBox = Box.createHorizontalBox();
        JLabel cellSizeLabel = new JLabel("cell:");
        cellSizeField = new JTextField(3);
        cellSizeField.addActionListener(e -> {
            try {
                cellSizeSlider.setValue(Integer.parseInt(cellSizeField.getText()));
            }catch (NumberFormatException ignored){}
        });
        cellSizeSlider.setMajorTickSpacing(MAX_CELL_SIZE-MIN_CELL_SIZE);
        cellSizeSlider.setPaintLabels(true);
        cellTextBox.add(cellSizeLabel);
        cellTextBox.add(cellSizeField);
        cellSliderBox.add(cellSizeSlider);
        cellSliderBox.add(cellTextBox);
        box.add(cellSliderBox);
        box.add(Box.createHorizontalStrut(20));

        Box main = Box.createVerticalBox();
        main.add(box);
        main.add(Box.createVerticalStrut(10));

        Box buttonBox = Box.createHorizontalBox();
        JButton ok = new JButton("OK");
        ok.addActionListener(e ->{
            try {
                int width;
                int height;
                int lineSize;
                int cellSize;
                try {
                    width = Integer.parseInt(widthField.getText());
                    height = Integer.parseInt(heightField.getText());
                    lineSize = Integer.parseInt(lineSizeField.getText());
                    cellSize = Integer.parseInt(cellSizeField.getText());
                } catch (NumberFormatException ex) {
                    throw new NumberFormatException("Please, enter number!");
                }
                boolean replace = rButtonReplace.isSelected();
                boolean impactMod = impact.isSelected();
                if (lineSize <= 0) {
                    throw new NumberFormatException("Line size should be greater than zero");
                }
                if(cellSize < MIN_CELL_SIZE){
                    throw new NumberFormatException("Cell size should be greater than "+ MIN_CELL_SIZE);
                }
                if(width < 0 || height < 0){
                    throw new NumberFormatException("Width and height should not be negative");
                }
                if(cellSize > MAX_CELL_SIZE || lineSize > MAX_LINE_SIZE || width+height+cellSize>140){
                    throw new NumberFormatException("Too large size!\n\nExpected:\nCell < "+ MAX_CELL_SIZE + "\nLine < "+ MAX_LINE_SIZE+"\nWidth + Height + Cell <= 140");
                }
                gridSettings.setHexSize(cellSize);
                gridSettings.setLineSize(lineSize);
                gridSettings.setReplace(replace);
                gridSettings.setImpact(impactMod);
                gameView.setButtons();
                if (width > 0 && height > 0) {
                    gameView.setSetting(width, height);
                    gridSettings.setChanges(true);
                }
                setVisible(false);
                dispose();
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonBox.add(ok);
        buttonBox.add(Box.createHorizontalStrut(15));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        buttonBox.add(cancel);

        main.add(buttonBox);
        main.add(Box.createVerticalStrut(10));
        setContentPane(main);
        init();
        pack();
        setLocationRelativeTo(gameView);
        setVisible(true);
    }

    private void init(){
        if(gridSettings.isReplace()){
            rButtonReplace.setSelected(true);
        }
        else{
            rButtonXor.setSelected(true);
        }
        if(gridSettings.isImpact()){
            impact.setSelected(true);
        }
        widthField.setText(String.valueOf(gameView.getGridWidth()));
        heightField.setText(String.valueOf(gameView.getGridHeight()));
        cellSizeSlider.setValue(gridSettings.getHexSize());
        cellSizeField.setText(String.valueOf(gridSettings.getHexSize()));
        lineSizeSlider.setValue(gridSettings.getLineSize());
        lineSizeField.setText(String.valueOf(gridSettings.getLineSize()));
    }
}
