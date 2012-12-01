package com.cibot.gui;

import com.cibot.cimodel.BuildStatus;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.cibot.gui.GUIResources.*;
import static com.cibot.gui.GUIUtil.constraintsBuilder;
import static com.cibot.gui.GUIUtil.forceSize;

/**
 * @author Uefix
 */
public class CIBotJobOverviewPanel extends JPanel {


    public void initialize() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        addJobLabel("Awaiting CI status...", null);
        addDummyToOverviewPanel();
    }


    public void addJobLabel(String jobName, BuildStatus status) {
        Color bgColor = BG_COLOR;
        Color fgColor = Color.BLACK;
        Color borderColor = Color.BLACK;
        Font font = JOB_NAME_FONT;

        if (status != null) {
            switch (status) {
                case BUILD_OK:
                    bgColor = Color.GREEN.darker().darker();
                    fgColor = BG_COLOR;
                    break;

                case BUILD_FAILED:
                    bgColor = Color.RED;
                    fgColor = Color.BLACK;
                    break;

                case BUILD_UNSTABLE:
                    bgColor = Color.RED;
                    fgColor = Color.YELLOW;
                    font = JOB_NAME_FONT_ITALIC;
                    break;

                case UNKNOWN:
                    bgColor = Color.BLACK;
                    fgColor = Color.YELLOW;
                    font = JOB_NAME_FONT_ITALIC;
                    break;

            }
        } else {
            font = JOB_NAME_FONT_ITALIC;
        }

        JLabel label = new JLabel(jobName);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setOpaque(true);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new LineBorder(borderColor, 1, false));



        forceSize(label, JOB_LABEL_SIZE);

        add(label, constraintsBuilder().gridy(getComponentCount() + 1).weighty(0)
                .fillHorizontal().anchorNorth().insets(1, 1, 1, 0).build());
    }


    public void addDummyToOverviewPanel() {
        JLabel dummyLabel = new JLabel();
        dummyLabel.setOpaque(false);
        add(dummyLabel, constraintsBuilder().gridy(getComponentCount() + 1).anchorNorth().insets(1, 1, 1, 0).build());
    }
}