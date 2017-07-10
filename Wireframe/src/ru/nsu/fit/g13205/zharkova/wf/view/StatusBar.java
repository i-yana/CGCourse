package ru.nsu.fit.g13205.zharkova.wf.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yana on 23.03.16.
 */
class StatusBar extends JPanel{
    private JLabel hint;

    public StatusBar(){
        super();
        super.setPreferredSize(new Dimension(100, 20));
        this.setLayout(new BorderLayout());
        this.hint = new JLabel("Ready");

    }
    public void setMessage(String message) {
        hint.setText(" " + message);
    }

}