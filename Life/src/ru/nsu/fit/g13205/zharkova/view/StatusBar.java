package ru.nsu.fit.g13205.zharkova.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yana on 15.02.16.
 */
class StatusBar extends JLabel {
    public StatusBar(){
        super();
        super.setPreferredSize(new Dimension(100,20));
        setMessage("Ready");
    }
    public void setMessage(String message) {
        setText(" "+message);
    }
}
