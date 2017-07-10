package ru.nsu.fit.g13205.zharkova.view;

import ru.nsu.fit.g13205.zharkova.model.Properties;

import javax.swing.*;

/**
 * Created by Yana on 15.02.16.
 */
public class GameOptions extends JDialog {
    private InputPanel ip;
    private GridPanel gridPanel;

    public GameOptions(GridPanel gridPanel) {
        super();
        setModal(true);
        setResizable(false);
        ip = new InputPanel(gridPanel.getProperties());
        this.gridPanel = gridPanel;
    }


    private void showWarningDialog(String reason) {
        JOptionPane.showMessageDialog(ip, reason, null, JOptionPane.WARNING_MESSAGE);
    }

    public void openDialog() {
        do {
            int res = JOptionPane.showConfirmDialog(this, ip, "Life", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    double liveBegin = Double.parseDouble(ip.getLiveBegin());
                    double liveEnd = Double.parseDouble(ip.getLiveEnd());
                    double birthBegin = Double.parseDouble(ip.getBirthBegin());
                    double birthEnd = Double.parseDouble(ip.getBirthEnd());
                    double fstImpact = Double.parseDouble(ip.getFstImpact());
                    double sndImpact = Double.parseDouble(ip.getSndImpact());
                    if(liveBegin>birthBegin){
                        throw new NumberFormatException("LIVE_BEGIN should not be greater than BIRTH_BEGIN");
                    }
                    if(birthBegin>birthEnd){
                        throw new NumberFormatException("BIRTH_BEGIN should not be greater than BIRTH_END");
                    }
                    if(birthEnd>liveEnd){
                        throw new NumberFormatException("BIRTH_END should not be greater than LIVE_END");
                    }
                    if(fstImpact >100 || sndImpact > 100){
                        throw new NumberFormatException("FIRST_IMPACT and SECOND_IMPACT should be greater than 100");
                    }
                    gridPanel.setProperties(new Properties(liveBegin, liveEnd, birthBegin, birthEnd, fstImpact, sndImpact));
                } catch (NumberFormatException e) {
                    showWarningDialog(e.getMessage());
                    continue;
                }
                break;
            } else {
                break;
            }
        } while (true);
    }

    static class InputPanel extends JPanel {
        public String getLiveBegin() {
            return liveBeginTF.getText();
        }

        public String getLiveEnd() {
            return liveEndTF.getText();
        }

        public String getBirthBegin() {
            return birthBeginTF.getText();
        }

        public String getBirthEnd() {
            return birthEndTF.getText();
        }

        public String getFstImpact() {
            return fstImpactTF.getText();
        }

        public String getSndImpact() {
            return sndImpactTF.getText();
        }

        JTextField liveBeginTF;
        JTextField liveEndTF;
        JTextField birthBeginTF;
        JTextField birthEndTF;
        JTextField fstImpactTF;
        JTextField sndImpactTF;

        InputPanel(Properties properties) {
            super();
            Box mainBox = Box.createVerticalBox();

            Box liveBeginBox = Box.createHorizontalBox();
            Box liveEndBox = Box.createHorizontalBox();
            Box birthBeginBox = Box.createHorizontalBox();
            Box birthEndBox = Box.createHorizontalBox();
            Box fstImpactBox = Box.createHorizontalBox();
            Box sndImpactBox = Box.createHorizontalBox();

            JLabel liveBeginLabel = new JLabel("Live begin:        ");
            JLabel liveEndLabel = new JLabel("Live end:           ");
            JLabel birthBeginLabel = new JLabel("Birth begin:       ");
            JLabel birthEndLabel = new JLabel("Birth end:          ");
            JLabel fstImpactLabel = new JLabel("First impact:     ");
            JLabel sndImpactLabel = new JLabel("Second Impact: ");

            liveBeginTF = new JTextField(String.valueOf(properties.getLiveBegin()), 5);
            liveEndTF = new JTextField(String.valueOf(properties.getLiveEnd()), 5);
            birthBeginTF = new JTextField(String.valueOf(properties.getBirthBegin()), 5);
            birthEndTF = new JTextField(String.valueOf(properties.getBirthEnd()), 5);
            fstImpactTF = new JTextField(String.valueOf(properties.getFstImpact()), 5);
            sndImpactTF = new JTextField(String.valueOf(properties.getSndImpact()), 5);

            liveBeginBox.add(liveBeginLabel);
            liveBeginBox.add(liveBeginTF);

            liveEndBox.add(liveEndLabel);
            liveEndBox.add(liveEndTF);

            birthBeginBox.add(birthBeginLabel);
            birthBeginBox.add(birthBeginTF);

            birthEndBox.add(birthEndLabel);
            birthEndBox.add(birthEndTF);

            fstImpactBox.add(fstImpactLabel);
            fstImpactBox.add(fstImpactTF);

            sndImpactBox.add(sndImpactLabel);
            sndImpactBox.add(sndImpactTF);

            mainBox.add(liveBeginBox);
            mainBox.add(liveEndBox);
            mainBox.add(birthBeginBox);
            mainBox.add(birthEndBox);
            mainBox.add(fstImpactBox);
            mainBox.add(sndImpactBox);
            Box butBox = Box.createHorizontalBox();
            JButton defSet = new JButton("default settings");
            defSet.addActionListener(e -> setDefaultValues());
            butBox.add(defSet);
            mainBox.add(Box.createVerticalStrut(6));
            mainBox.add(butBox);
            add(mainBox);
        }


        private void setDefaultValues() {
            liveBeginTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getLiveBegin()));
            liveEndTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getLiveEnd()));
            birthBeginTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getBirthBegin()));
            birthEndTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getBirthEnd()));
            fstImpactTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getFstImpact()));
            sndImpactTF.setText(String.valueOf(Properties.DEFAULT_SETTINGS.getSndImpact()));

        }
    }
}
