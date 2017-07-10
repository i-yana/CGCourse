package ru.nsu.fit.g13205.zharkova.wf.view;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yana on 30.03.16.
 */
class ComponentHelpText extends MouseAdapter {
    JComponent menuItem;
    String helpText;
    StatusBar statusBar;


    public ComponentHelpText(StatusBar statusBar, JComponent menuItem, String helpText)
    {
        this.menuItem = menuItem;
        this.helpText = helpText;
        this.statusBar = statusBar;
        menuItem.addMouseListener(this);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        try {
            statusBar.setMessage(helpText);
            menuItem.setToolTipText(helpText);
        }catch (NullPointerException ignored){}
    }

    @Override
    public void mouseExited(MouseEvent e) {
        statusBar.setMessage("Ready");
    }
}