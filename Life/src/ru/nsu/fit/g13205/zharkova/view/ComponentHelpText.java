package ru.nsu.fit.g13205.zharkova.view;


import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yana on 15.02.16.
 */
class ComponentHelpText extends MouseAdapter {
    JComponent menuItem;
    JComponent toolbarItem;
    String helpText;
    StatusBar statusBar;
    public ComponentHelpText(StatusBar statusBar, JComponent menuItem, JComponent toolbarItem, String helpText)
    {
        this.menuItem = menuItem;
        this.toolbarItem = toolbarItem;
        this.helpText = helpText;
        this.statusBar = statusBar;
        toolbarItem.addMouseListener(this);
        menuItem.addMouseListener(this);
    }

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
            toolbarItem.setToolTipText(helpText);
        }catch (NullPointerException ignored){}
    }

    @Override
    public void mouseExited(MouseEvent e) {
        statusBar.setMessage("Ready");
    }
}
