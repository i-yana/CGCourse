package ru.nsu.g13205.zharkova;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * Created by Yana on 15.03.16.
 */
public class SettingDialog extends JDialog {

    private static final String SOBEL = "Sobel";
    private static final String ROBERTS = "Roberts";
    private static final String OUTLINE = "Outline";
    private static final String GAMMA = "Gamma";
    private static final String ROTATION = "Rotation";
    private final BufferedImage oldPixelImage;

    private JSlider slider;
    private JTextField edit;
    private JLabel label;
    private PaintArea paintArea;
    private String algorithm;
    private JButton apply = new JButton("Update");
    private JButton ok = new JButton("OK");
    private JButton cancel = new JButton("Cancel");

    private BufferedImage oldImage;
    private int minVal;
    private int maxVal;


    private void showWarningDialog(String reason) {
        JOptionPane.showMessageDialog(this, reason, null, JOptionPane.WARNING_MESSAGE);
    }

    public SettingDialog(PaintArea paintArea, String algorithm, int minVal, int maxVal, int startVal, String labelText) {
        super();
        setModal(true);
        setResizable(false);
        setLayout(new BorderLayout());
        this.paintArea = paintArea;
        this.algorithm = algorithm;
        this.slider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, startVal);
        this.label = new JLabel(labelText + ": ");
        this.edit = new JTextField(5);
        this.oldImage = paintArea.getTransformImage();
        this.oldPixelImage = paintArea.getPixelImage();
        this.maxVal = maxVal;
        this.minVal = minVal;
        init();

        slider.addChangeListener(e -> {
            setLabel(slider.getValue());
            paint();
        });
        apply.addActionListener(e -> applyAction());
        edit.addActionListener(e -> applyAction());
        ok.addActionListener(e -> {
            applyAction();
            setVisible(false);
            dispose();
        });
        cancel.addActionListener(e -> cancelAction());
        setLabel(startVal);
        paint();
        pack();
        setLocationRelativeTo(paintArea);
        setVisible(true);
    }

    private void applyAction(){
        try {
            double newValue = Double.parseDouble(edit.getText());
            if(algorithm.equals(GAMMA)){
                if (newValue > maxVal/100 || newValue < minVal/100) {
                    throw new NumberFormatException("Value should be between " + minVal/100 + " and " + maxVal/100);
                }
            }
            if (newValue > maxVal || newValue < minVal) {
                throw new NumberFormatException("Value should be between " + minVal + " and " + maxVal);
            }
            setSlider(newValue);
        } catch (NumberFormatException ex) {
            showWarningDialog(ex.getMessage());
        }
    }

    private void cancelAction(){
        paintArea.setOldImage(oldImage, oldPixelImage);
        setVisible(false);
        dispose();
    }


    private void init() {
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(15));

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(10));
        hBox.add(label);
        hBox.add(edit);
        hBox.add(slider);
        hBox.add(Box.createHorizontalStrut(10));

        mainBox.add(hBox);
        mainBox.add(Box.createVerticalStrut(10));
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(apply);
        buttonBox.add(ok);
        buttonBox.add(cancel);
        mainBox.add(buttonBox);

        mainBox.add(Box.createVerticalStrut(15));
        add(mainBox);
    }

    private void setLabel(int value) {
        switch (algorithm) {
            case GAMMA:
                edit.setText(String.format(Locale.ENGLISH,"%.2f", (double) value / 100));
                break;
            default:
                edit.setText(String.valueOf(value));
                break;
        }
    }

    private void setSlider(double value) {
        switch (algorithm) {
            case GAMMA:
                slider.setValue((int)(value*100));
                break;
            default:
                slider.setValue((int) value);
                break;
        }
    }

    private void paint() {
        switch (algorithm) {
            case SOBEL:
                paintArea.sobelFilter(slider.getValue());
                break;
            case ROBERTS:
                paintArea.robertsFilter(slider.getValue());
                break;
            case OUTLINE:
                paintArea.selectOutline(slider.getValue());
                break;
            case ROTATION:
                paintArea.turn(slider.getValue());
                break;
            case GAMMA:
                paintArea.gammaCorrection(slider.getValue() / 100.);
                break;
        }
    }
}
