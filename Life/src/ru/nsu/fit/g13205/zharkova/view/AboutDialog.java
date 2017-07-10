package ru.nsu.fit.g13205.zharkova.view;

import javax.swing.*;

/**
 * Created by Yana on 23.02.16.
 */
public class AboutDialog {

    private static final String text = "Author: Yana Zharkova \nVersion: 1.0\n\nCopyright (c) 2016";

    public AboutDialog(GameView parent){
        JOptionPane.showMessageDialog(parent, text, "Life", JOptionPane.INFORMATION_MESSAGE);
    }
}
