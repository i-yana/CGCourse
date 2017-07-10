package ru.nsu.g13205.zharkova;

import javax.swing.*;

/**
 * Created by Yana on 16.03.16.
 */
public class AboutDialog {
    private static final String text = "Author: Yana Zharkova \nVersion: 1.0\n\nCopyright (c) 2016";

    public AboutDialog(MainView parent){
        JOptionPane.showMessageDialog(parent, text, "Filter", JOptionPane.INFORMATION_MESSAGE);
    }
}
