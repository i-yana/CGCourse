package ru.nsu.fit.g13205.zharkova;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Yana on 23.03.16.
 */
class StatusBar extends JPanel{
    private JLabel hint;
    private JTextField forXCord;
    private JTextField forYCord;
    private JTextField forZCord;

    public StatusBar(){
        super();
        super.setPreferredSize(new Dimension(100, 20));
        this.setLayout(new BorderLayout());
        this.hint = new JLabel("Ready");
        JPanel rightPanel = new JPanel(new GridLayout(0,3));
        Box xBox = Box.createHorizontalBox();
        JLabel x = new JLabel("x = ");
        xBox.add(x);
        forXCord = new JTextField(7);
        forXCord.setFocusable(false);
        forXCord.setEditable(false);
        xBox.add(forXCord);
        Box yBox = Box.createHorizontalBox();
        JLabel y = new JLabel(", y = ");
        yBox.add(y);
        forYCord = new JTextField(7);
        forYCord.setFocusable(false);
        forYCord.setEditable(false);
        yBox.add(forYCord);
        Box zBox = Box.createHorizontalBox();
        JLabel z = new JLabel(", z = ");
        zBox.add(z);
        forZCord = new JTextField(7);
        forZCord.setFocusable(false);
        forZCord.setEditable(false);
        zBox.add(forZCord);
        rightPanel.add(xBox);
        rightPanel.add(yBox);
        rightPanel.add(zBox);
        add(hint, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    public void setMessage(String message) {
        hint.setText(" " + message);
    }
    public void setCords(Cord cords, Double z){
        if(cords == null){
            forXCord.setText("-");
            forYCord.setText("-");
            forZCord.setText("-");
            return;
        }
        String text = Tools.eraseLastZero(new DecimalFormat(Tools.FORMAT).format(cords.x));
        forXCord.setText(text);
        text = Tools.eraseLastZero(new DecimalFormat(Tools.FORMAT).format(cords.y));
        forYCord.setText(text);
        text = Tools.eraseLastZero(new DecimalFormat(Tools.FORMAT).format(z));
        forZCord.setText(text);
    }
}