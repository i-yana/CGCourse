package ru.nsu.fit.g13205.zharkova.wf.view;

import javax.swing.*;

/**
 * Created by Yana on 05.04.16.
 */
public class AboutDialog {
    private static final String text = "Author: Yana Zharkova \nVersion: 1.0\n\nCopyright (c) 2016";

    public AboutDialog(MainView parent){
        JOptionPane.showMessageDialog(parent, text, "WF", JOptionPane.INFORMATION_MESSAGE);
    }
}
