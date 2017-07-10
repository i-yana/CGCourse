package ru.nsu.fit.g13205.zharkova;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yana on 29.03.16.
 */
public class SettingsDialog extends JDialog {

    private PaintArea paintArea;
    private static String FOO = "x*x-y*y";
    ArrayList<JLabel> labels = new ArrayList<>();
    JTextField funcArea;
    JPanel colorPanel;
    JComboBox<Integer> comboBox;
    final Properties properties;
    int colors;
    Color isoline;

    private JTextField kText = new JTextField("", 5);
    private JTextField mText = new JTextField("", 5);
    private JTextField aText = new JTextField(String.valueOf(Boundary.DEFAULT_A), 5);
    private JTextField bText = new JTextField(String.valueOf(Boundary.DEFAULT_B), 5);
    private JTextField cText = new JTextField(String.valueOf(Boundary.DEFAULT_C), 5);
    private JTextField dText = new JTextField(String.valueOf(Boundary.DEFAULT_D), 5);

    public SettingsDialog(PaintArea paintArea) {
        super();
        setModal(true);
        setSize(500, 750);
        setResizable(false);
        this.paintArea = paintArea;
        if(paintArea.getProperties() != null) {
            this.properties = new Properties(paintArea.getProperties());
        }
        else {
            this.properties = Properties.DEFAULT_PROPERTIES;
        }
        ArrayList<Color> p = properties.getColors();
        for (Color aP : p) {
            JLabel l = new JLabel();
            l.setBackground(aP);
            l.setOpaque(true);
            labels.add(p.indexOf(aP), l);
        }
        for (int i = labels.size(); i < 15; i++) {
            JLabel l = new JLabel();
            l.setBackground(Properties.DEFAULT_PROPERTIES.getColors().get(i));
            l.setOpaque(true);
            labels.add(i, l);
        }
        this.isoline = properties.getIsoColor();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        Box main = Box.createVerticalBox();
        Box funcBox = Box.createVerticalBox();
        funcBox.add(Box.createVerticalStrut(10));
        Box funcTextBox = Box.createHorizontalBox();
        funcTextBox.add(Box.createHorizontalStrut(10));
        JLabel f = new JLabel("f(x,y) = ");
        funcTextBox.add(f);
        funcArea = new JTextField(FOO);
        funcTextBox.add(funcArea);
        funcTextBox.add(Box.createHorizontalStrut(10));
        funcBox.add(funcTextBox);
        funcBox.add(Box.createVerticalStrut(10));
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "function");
        title.setTitleJustification(TitledBorder.LEFT);
        funcBox.setBorder(title);
        main.add(funcBox);

        Box secondBox = Box.createHorizontalBox();
        Box gridBox = Box.createVerticalBox();
        gridBox.add(Box.createVerticalStrut(10));

        JPanel paramsPanel = new JPanel(new GridLayout(2,1));

        kText.setText(String.valueOf(properties.getK()));
        mText.setText(String.valueOf(properties.getM()));

        Box kBox = Box.createHorizontalBox();
        kBox.add(Box.createHorizontalStrut(20));
        kBox.add(new JLabel("{x1 < x2 < .. < xk}  k ="));
        kBox.add(kText);
        kBox.add(Box.createHorizontalStrut(20));
        Box mBox = Box.createHorizontalBox();
        mBox.add(Box.createHorizontalStrut(20));
        mBox.add(new JLabel("{y1 < y2 < .. < ym} m ="));
        mBox.add(mText);
        mBox.add(Box.createHorizontalStrut(20));

        paramsPanel.add(kBox);
        paramsPanel.add(mBox);

        gridBox.add(paramsPanel);
        gridBox.add(Box.createVerticalStrut(10));
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Grid");
        title.setTitleJustification(TitledBorder.LEFT);
        gridBox.setBorder(title);
        secondBox.add(gridBox);

        Box borderBox = Box.createVerticalBox();
        borderBox.add(Box.createVerticalStrut(10));

        paramsPanel = new JPanel(new GridLayout(2, 1));

        Boundary b = properties.getBoundary();
        String text = Tools.sub(String.valueOf(b.getA()), 7);
        aText.setText(text);
        text = Tools.sub(String.valueOf(b.getB()), 7);
        bText.setText(text);
        text = Tools.sub(String.valueOf(b.getC()), 7);
        cText.setText(text);
        text = Tools.sub(String.valueOf(b.getD()), 7);
        dText.setText(text);

        Box ab = Box.createHorizontalBox();
        ab.add(Box.createHorizontalStrut(20));
        ab.add(new JLabel("a ="));
        ab.add(aText);
        ab.add(new JLabel("b ="));
        ab.add(bText);
        ab.add(Box.createHorizontalStrut(20));

        Box cd = Box.createHorizontalBox();
        cd.add(Box.createHorizontalStrut(20));
        cd.add(new JLabel("c ="));
        cd.add(cText);
        cd.add(new JLabel("d ="));
        cd.add(dText);
        cd.add(Box.createHorizontalStrut(20));

        paramsPanel.add(ab);
        paramsPanel.add(cd);
        borderBox.add(paramsPanel);
        borderBox.add(Box.createVerticalStrut(10));
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Borders");
        title.setTitleJustification(TitledBorder.LEFT);
        borderBox.setBorder(title);
        secondBox.add(borderBox);

        colorPanel = new JPanel();
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Choose colors");
        title.setTitleJustification(TitledBorder.LEFT);
        colorPanel.setBorder(title);

        Box thirdBox = Box.createHorizontalBox();
        JLabel colorLabel = new JLabel("Color number ");
        thirdBox.add(Box.createHorizontalStrut(20));
        thirdBox.add(colorLabel);
        comboBox = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        comboBox.setMaximumSize(new Dimension(100,25));
        thirdBox.add(comboBox);
        comboBox.addActionListener(e -> {
            drawPanel();
        });
        comboBox.setSelectedItem(properties.getColors().size());
        JPanel buttons = new JPanel(new GridLayout(1,2));
        JButton ok = new JButton("OK");
        ok.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleOk();
                }
            }
        });
        ok.addActionListener(e -> {
            handleOk();
        });
        buttons.add(ok);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        buttons.add(cancel);

        main.add(secondBox);
        main.add(Box.createVerticalStrut(10));
        main.add(thirdBox);
        main.add(Box.createVerticalStrut(10));
        main.add(colorPanel);
        main.add(Box.createVerticalStrut(10));
        main.add(buttons);
        add(main);
        pack();
        setLocationRelativeTo(paintArea);
        ok.requestFocusInWindow();
        setVisible(true);
    }

    private void handleOk(){
        try{
            int k = Integer.parseInt(kText.getText());
            int m = Integer.parseInt(mText.getText());
            double a = Double.parseDouble(aText.getText());
            double b = Double.parseDouble(bText.getText());
            double c = Double.parseDouble(cText.getText());
            double d = Double.parseDouble(dText.getText());

            if(k<2 || m<2 || a >= b || c >= d){
                throw new NumberFormatException();
            }
            ArrayList<Color> outColors = new ArrayList<>();
            for (int i = 0; i < colors; i++) {
                outColors.add(i, labels.get(i).getBackground());
            }
            Properties out = new Properties(funcArea.getText(), k, m, colors, outColors, isoline, new Boundary(a, b, c, d));
            paintArea.createMap(out);
            FOO = funcArea.getText();
            setVisible(false);
            dispose();
        }catch (NumberFormatException e){

            JOptionPane.showMessageDialog(this, "Parameters should be numbers:\nk > 2, m > 2\na < b, c < d", "Warning", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e){
            JOptionPane.showConfirmDialog(null, "Uncorrected function: " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    private void drawPanel(){
        colors = (int) comboBox.getSelectedItem();
        Component[] components = colorPanel.getComponents();
        for (Component component : components) {
            colorPanel.remove(component);
        }
        colorPanel.setLayout(new GridLayout(colors+1,3));
        JLabel isoLabel = new JLabel();
        isoLabel.setBackground(isoline);
        isoLabel.setOpaque(true);
        JButton changeIsButton = new JButton("...");
        changeIsButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(SettingsDialog.this, "Choose a color", isoLabel.getBackground() );
            if(color!=null) {
                isoLabel.setBackground(color);
                isoline = color;
            }
            pack();
            setLocationRelativeTo(paintArea);
            repaint();
        });
        colorPanel.add(new JLabel("             Isoline Color "));
        colorPanel.add(isoLabel);
        colorPanel.add(changeIsButton);
        for (int i = 0; i < colors; i++) {
            JButton changeButton = new JButton("...");
            final int finalI = i;
            changeButton.addActionListener(e -> {
               Color color = JColorChooser.showDialog(SettingsDialog.this, "Choose a color", labels.get(finalI).getBackground());
                if(color!=null) {
                    labels.get(finalI).setBackground(color);
                }
                pack();
                setLocationRelativeTo(paintArea);
                repaint();
            });
            colorPanel.add(new JLabel("             Color " + (i + 1)));
            colorPanel.add(labels.get(i));
            colorPanel.add(changeButton);
        }
        pack();
        setLocationRelativeTo(paintArea);
        repaint();
    }
}
