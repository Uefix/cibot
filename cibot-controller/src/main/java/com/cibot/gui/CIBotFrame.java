package com.cibot.gui;

import com.cibot.cimodel.BuildStatus;
import com.cibot.cimodel.CIModel;
import com.cibot.thumbi.ThumbiConnectionListener;
import com.cibot.thumbi.ThumbiConnectionType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.cibot.gui.GUIResources.*;
import static com.cibot.gui.GUIUtil.*;

/**
 * @author Uefix
 */
public class CIBotFrame extends JFrame implements Observer, ThumbiConnectionListener {


    private static final Logger LOG = LoggerFactory.getLogger(CIBotFrame.class);


    @Autowired
    CIModel ciModel;

    @Autowired
    GUIResources resources;


    private CIBotStatusPanel statusPanel = new CIBotStatusPanel();

    private JLabel connectedLabel;

    private JPanel jobOverviewPanel;



    public void initialize() {
        Preconditions.checkState(ciModel != null, "Model not set");

        setLayout(new GridBagLayout());
        initConnectedLabel();
        initStatusPanel();
        initJobOverviewPanel();
        initGlassPanel();

        setIconImage(resources.getLogoIcon().getImage());
        setTitle("ciBOT");
        getContentPane().setBackground(Color.WHITE);

        setSize(685, 475);
        centerWindow(this);

//        updateBuildStatus(ciModel);
        ciModel.addObserver(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(1);
            }
        });

        setVisible(true);

        LOG.info("Initialized");
    }




    //---- java.util.Observer ----//

    @Override
    public void update(Observable obs, Object arg) {
        CIModel model = (CIModel) obs;
        updateBuildStatus(model);
    }


    //---- ThumbiConnectionListener ----//

    @Override
    public void connected(ThumbiConnectionType connectionType) {
        showConnectedIcon(connectionType);
    }

    @Override
    public void disconnected() {
        showConnectedIcon(null);
    }


    //---- Internal ----//

    private void initStatusPanel() {
        statusPanel.initialize();
        getContentPane().add(statusPanel, constraintsBuilder().build());
    }


    private void initConnectedLabel() {
        connectedLabel = new JLabel();
        forceSize(connectedLabel, CONNECTION_ICON_SIZE);
    }


    private void initJobOverviewPanel() {
        jobOverviewPanel = new JPanel();
        jobOverviewPanel.setOpaque(false);
        jobOverviewPanel.setLayout(new GridBagLayout());

        addJobLabel("Awaiting CI status...", null);
        addDummyToOverviewPanel();

        getContentPane().add(jobOverviewPanel, constraintsBuilder().gridx(2).weightx(0.1d).build());
        getContentPane().invalidate();
    }


    private void addJobLabel(String jobName, BuildStatus status) {
        Color bgColor = Color.WHITE;
        Color fgColor = Color.BLACK;
        Color borderColor = Color.BLACK;
        Font font = JOB_NAME_FONT;

        if (status != null) {
            switch (status) {
                case BUILD_OK:
                    bgColor = Color.GREEN.darker().darker();
                    fgColor = Color.WHITE;
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

        jobOverviewPanel.add(label,
                constraintsBuilder()
                        .gridy(jobOverviewPanel.getComponentCount() + 1).weighty(0)
                        .fillHorizontal().anchorNorth()
                        .insets(1, 1, 1, 0).build());
    }

    private void addDummyToOverviewPanel() {
        JLabel dummyLabel = new JLabel();
        dummyLabel.setOpaque(false);
        jobOverviewPanel.add(dummyLabel, constraintsBuilder()
                        .gridy(jobOverviewPanel.getComponentCount() + 1)
                        .anchorNorth()
                        .insets(1, 1, 1, 0).build());
    }


    private void initGlassPanel() {
        JPanel glassPanel = new JPanel(new GridBagLayout());
        glassPanel.setBackground(Color.WHITE);

        glassPanel.add(connectedLabel,
                constraintsBuilder()
                        .anchor(GridBagConstraints.SOUTHWEST)
                        .insets(5, 5, 0, 0).build());

        forceSize(glassPanel, reDimension(CONNECTION_ICON_SIZE, 10, 10));
        getLayeredPane().add(glassPanel, JLayeredPane.POPUP_LAYER);

    }


    private void updateBuildStatus(CIModel model) {
        synchronized (model) {
            switch (model.getOverallStatus()) {
                case BUILD_OK:
                    statusPanel.setCanvasSVGUrl(resources.getThumbUpUrl());
                    break;

                case UNKNOWN:
                    statusPanel.setCanvasSVGUrl(resources.getJenkinsUnavailableUrl());
                    break;

                default:
                    statusPanel.setCanvasSVGUrl(resources.getThumbDownUrl());
                    break;
            }

            jobOverviewPanel.removeAll();
            List<String> jobKeys = Lists.newArrayList(model.getJobKeys());
            Collections.sort(jobKeys);
            for (String jobKey : jobKeys) {
                BuildStatus status = model.getStatusForJob(jobKey);
                addJobLabel(jobKey, status);
            }
            addDummyToOverviewPanel();

            validate();
            repaint();
        }
    }


    void showConnectedIcon(ThumbiConnectionType connectionType) {
        if (connectionType == null) {
            connectedLabel.setIcon(null);
        } else if (connectionType == ThumbiConnectionType.BLUETOOTH) {
            connectedLabel.setIcon(resources.getBlueToothIcon());
        } else if (connectionType == ThumbiConnectionType.USB) {
            connectedLabel.setIcon(resources.getUsbIcon());
        }
    }
}