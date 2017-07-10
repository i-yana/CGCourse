package ru.nsu.g13205.zharkova;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yana on 16.03.16.
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
