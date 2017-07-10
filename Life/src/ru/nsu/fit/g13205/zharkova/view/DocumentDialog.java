package ru.nsu.fit.g13205.zharkova.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yana on 14.02.16.
 */
public class DocumentDialog {
    private InputPanel ip;

    public DocumentDialog(GameView gameView){
        super();
        ip = new InputPanel();
        do {
            int res = JOptionPane.showConfirmDialog(gameView, ip, "Life", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int w = Integer.parseInt(ip.getWidthText());
                    int h = Integer.parseInt(ip.getHeightText());
                    //gameView.getGridSettings().setDefault();
                    if(w <= 0 || h <= 0){
                        throw new NumberFormatException();
                    }
                    gameView.fillBoard(w,h, new ArrayList<>());
                    gameView.getGridSettings().setCurrentFile(null);
                    gameView.getGridSettings().setChanges(true);
                } catch (NumberFormatException e) {
                    showWarningDialog("Please, enter valid number");
                    continue;
                }
                break;
            } else {
                break;
            }
        }while(true);
    }

    private void showWarningDialog(String reason) {
        JOptionPane.showMessageDialog(ip, reason, null, JOptionPane.WARNING_MESSAGE);
    }

    static class InputPanel extends JPanel {

        private JTextField width;
        private JTextField height;

        public InputPanel() {
            super(new GridBagLayout());
            JLabel lblFN = new JLabel("Width:");
            JLabel lblLN = new JLabel("Height:");
            width = new JTextField("20",10);
            height = new JTextField("20",10);
            add(lblFN, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
            add(width, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            add(lblLN, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
            add(height, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        }

        public JTextField getFocusableJTextField(){
            return width;
        }
        public String getWidthText(){
            return width.getText();
        }

        public String getHeightText(){
            return height.getText();
        }
    }
}
