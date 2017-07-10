package ru.nsu.g13205.zharkova;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yana on 15.03.16.
 */
public class PixelizeDialog extends JDialog{


    public PixelizeDialog(PaintArea paintArea) {
        super();
        setModal(true);
        setResizable(false);
        InputPanel ip = new InputPanel(paintArea.getPixelSize());
        do {
            int res = JOptionPane.showConfirmDialog(this, ip, "Pixelize", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int size = Integer.parseInt(ip.getPixelSize());
                    if(350%size!=0 || size > 10){
                        throw new NumberFormatException("Size should be divider 350 and <= 10");
                    }
                    paintArea.setPixelSize(size);
                    if(paintArea.isPixelSize()){
                        paintArea.repaint();
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

        private JTextField pSizeTF;

        InputPanel(int start) {
            setLayout(new GridLayout(1, 2));
            pSizeTF = new JTextField(String.valueOf(start));
            this.add(new JLabel("Pixel Size: "));
            this.add(pSizeTF);

        }

        public String getPixelSize() {
            return pSizeTF.getText();
        }

    }
}
