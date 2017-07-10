package ru.nsu.g13205.zharkova;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yana on 14.03.16.
 */
public class DitheringDialog extends JDialog{

    private static final String FLOYD = "Floyd Dithering";
    private static final String ORDERED = "Ordered Dithering";

    public DitheringDialog(PaintArea paintArea, String algorithm) {
        super();
        setModal(true);
        setResizable(false);
        InputPanel ip = new InputPanel();
        do {
            int res = JOptionPane.showConfirmDialog(this, ip, algorithm, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int redPalette = Integer.parseInt(ip.getRedPalette());
                    int greenPalette = Integer.parseInt(ip.getGreenPalette());
                    int bluePalette = Integer.parseInt(ip.getBluePalette());
                    if(redPalette < 2 || redPalette > 256 || greenPalette < 2 || greenPalette > 256 || bluePalette < 2 || bluePalette > 256){
                        throw new NumberFormatException("Color should be between 2 and 256");
                    }
                    if(algorithm.equals(FLOYD)) {
                        paintArea.dithering(redPalette, greenPalette, bluePalette);
                    }
                    if(algorithm.equals(ORDERED)){
                        paintArea.orderedDithering(redPalette, greenPalette, bluePalette);
                    }
                }catch (NumberFormatException e){
                    showWarningDialog(e.getMessage());
                    continue;
                }
                break;
            }
            else break;
        }while (true);
    }

    private void showWarningDialog(String reason) {
        JOptionPane.showMessageDialog(this, reason, null, JOptionPane.WARNING_MESSAGE);
    }

    static class InputPanel extends JPanel {

        private JTextField redTF = new JTextField("2");
        private JTextField greenTF = new JTextField("2");
        private JTextField blueTF = new JTextField("2");

        InputPanel() {
            setLayout(new GridLayout(3, 2));
            this.add(new JLabel("Red Palette"));
            this.add(redTF);
            this.add(new JLabel("Green Palette"));
            this.add(greenTF);
            this.add(new JLabel("Blue Palette"));
            this.add(blueTF);
        }

        public String getRedPalette() {
            return redTF.getText();
        }

        public String getGreenPalette() {
            return greenTF.getText();
        }

        public String getBluePalette() {
            return blueTF.getText();
        }
    }
}
