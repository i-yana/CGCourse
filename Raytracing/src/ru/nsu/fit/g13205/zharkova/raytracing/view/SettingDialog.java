package ru.nsu.fit.g13205.zharkova.raytracing.view;

import ru.nsu.fit.g13205.zharkova.raytracing.model.RenderSetting;

import javax.swing.*;
import java.awt.*;


/**
 * Created by Yana on 15.05.16.
 */
public class SettingDialog extends JDialog {

    public SettingDialog(Frame parent, RenderSetting renderSetting){
        super();
        setResizable(false);
        setModal(true);
        setLayout(new BorderLayout());
        Box center = Box.createVerticalBox();
        center.add(Box.createVerticalStrut(10));

        Box horBox = Box.createHorizontalBox();
        horBox.add(Box.createHorizontalStrut(10));
        JButton colorChooser = new JButton("Bg color");
        colorChooser.setBackground(renderSetting.getColor());
        colorChooser.setOpaque(true);
        colorChooser.addActionListener(e->{
            Color color = JColorChooser.showDialog(SettingDialog.this, "Choose a color", ((JButton) e.getSource()).getBackground());
            if(color!=null){
                ((JButton) e.getSource()).setBackground(color);
                repaint();
            }
        });
        horBox.add(colorChooser);
        horBox.add(new JLabel("Gamma"));
        JTextField gamma = new JTextField(String.valueOf(renderSetting.getGamma()), 5);
        horBox.add(gamma);
        horBox.add(new JLabel("Depth"));
        JTextField depth = new JTextField(String.valueOf(renderSetting.getDepth()), 5);
        horBox.add(depth);
        horBox.add(Box.createHorizontalStrut(10));
        center.add(horBox);
        center.add(Box.createVerticalStrut(10));

        Box hor2 = Box.createHorizontalBox();
        hor2.add(Box.createHorizontalStrut(10));
        hor2.add(new JLabel("Quality:    "));
        JRadioButton rough = new JRadioButton("Rough");
        JRadioButton normal = new JRadioButton("Normal");
        JRadioButton fine = new JRadioButton("Fine");
        ButtonGroup group = new ButtonGroup();
        group.add(rough);
        group.add(normal);
        group.add(fine);
        String q = renderSetting.getQuality().toUpperCase();
        if(q.equals("ROUGH")){
            rough.setSelected(true);
        }
        if(q.equals("NORMAL")){
            normal.setSelected(true);
        }
        if(q.equals("FINE")){
            fine.setSelected(true);
        }
        hor2.add(rough);
        hor2.add(normal);
        hor2.add(fine);
        hor2.add(Box.createHorizontalStrut(10));
        center.add(hor2);
        center.add(Box.createVerticalStrut(10));



        JPanel northPanel = new JPanel(new FlowLayout());
        JButton apply = new JButton("Apply");
        apply.addActionListener(e->{
            try {
                double gam = Double.parseDouble(gamma.getText());
                int dep = Integer.parseInt(depth.getText());

                if(dep<1){
                    JOptionPane.showMessageDialog(this, "Raytracing depth must be greater then zero.", "Raytracing", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(dep > RenderSetting.MAX_DEPTH){
                    JOptionPane.showMessageDialog(this, "Raytracing depth will be "+ RenderSetting.MAX_DEPTH, "Raytracing", JOptionPane.WARNING_MESSAGE);
                    dep = RenderSetting.MAX_DEPTH;
                }
                renderSetting.setColor(colorChooser.getBackground());
                renderSetting.setGamma(gam);
                renderSetting.setDepth(dep);

                if(rough.isSelected()){
                    renderSetting.setQuality("rough");
                }
                if(normal.isSelected()){
                    renderSetting.setQuality("normal");
                }
                if(fine.isSelected()){
                    renderSetting.setQuality("fine");
                }
                parent.updateView3D();
            }catch (NumberFormatException ex){
                JOptionPane.showConfirmDialog(SettingDialog.this, ex.getMessage(), "Raytracing", JOptionPane.DEFAULT_OPTION);
            }
        });
        northPanel.add(apply);

        JButton ok = new JButton("OK");
        ok.addActionListener(e->{
            try {
                double gam = Double.parseDouble(gamma.getText());
                int dep = Integer.parseInt(depth.getText());
                if(dep<1){
                    JOptionPane.showMessageDialog(this, "Raytracing depth must be greater then zero.", "Raytracing", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(dep > RenderSetting.MAX_DEPTH){
                    JOptionPane.showMessageDialog(this, "Raytracing depth will be "+ RenderSetting.MAX_DEPTH, "Raytracing", JOptionPane.WARNING_MESSAGE);
                    dep = RenderSetting.MAX_DEPTH;
                }
                renderSetting.setColor(colorChooser.getBackground());
                renderSetting.setGamma(gam);
                renderSetting.setDepth(dep);

                if(rough.isSelected()){
                    renderSetting.setQuality("rough");
                }
                if(normal.isSelected()){
                    renderSetting.setQuality("normal");
                }
                if(fine.isSelected()){
                    renderSetting.setQuality("fine");
                }
                parent.updateView3D();
                setVisible(false);
                dispose();
            }catch (NumberFormatException ex){
                JOptionPane.showConfirmDialog(SettingDialog.this, ex.getMessage(), "Raytracing", JOptionPane.DEFAULT_OPTION);
            }
        });
        northPanel.add(ok);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e->{
            setVisible(false);
            dispose();
        });
        northPanel.add(cancel);

        center.add(Box.createVerticalStrut(10));
        add(center, BorderLayout.CENTER);
        add(northPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
