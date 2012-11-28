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

    @Autowired
    CIBotStatusPanel statusPanel;

    @Autowired
    CIBotJobOverviewPanel jobOverviewPanel;


    private JLabel connectedLabel;


    public void initialize() {
        Preconditions.checkState(ciModel != null, "Model not set");

        setLayout(new GridBagLayout());
        initConnectedLabel();

        getContentPane().add(statusPanel, constraintsBuilder().build());
        getContentPane().add(jobOverviewPanel, constraintsBuilder().gridx(2).weightx(0.1d).build());

        initGlassPanel();

        setIconImage(resources.getLogoIcon().getImage());
        setTitle("ciBOT");
        getContentPane().setBackground(BG_COLOR);

        setSize(685, 475);
        centerWindow(this);
        addExitWindowListener(this);

        setVisible(true);

        ciModel.addObserver(this);

        LOG.info("Initialized");
    }


    //---- java.util.Observer ----//

    @Override
    public void update(Observable obs, Object arg) {
        CIModel model = (CIModel) obs;
        synchronized (model) {
            updateStatusPanel(model);
            updateJobOverviewPanel(model);
            validate();
            repaint();
        }
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

    private void initConnectedLabel() {
        connectedLabel = new JLabel();
        forceSize(connectedLabel, CONNECTION_ICON_SIZE);
    }


    private void initGlassPanel() {
        JPanel glassPanel = new JPanel(new GridBagLayout());
        glassPanel.setBackground(BG_COLOR);

        glassPanel.add(connectedLabel,
                constraintsBuilder()
                        .anchor(GridBagConstraints.SOUTHWEST)
                        .insets(5, 5, 0, 0).build());

        forceSize(glassPanel, reDimension(CONNECTION_ICON_SIZE, 10, 10));
        getLayeredPane().add(glassPanel, JLayeredPane.POPUP_LAYER);

    }


    private void updateJobOverviewPanel(CIModel model) {
        jobOverviewPanel.removeAll();
        List<String> jobKeys = Lists.newArrayList(model.getJobKeys());
        Collections.sort(jobKeys);
        for (String jobKey : jobKeys) {
            BuildStatus status = model.getStatusForJob(jobKey);
            jobOverviewPanel.addJobLabel(jobKey, status);
        }
        jobOverviewPanel.addDummyToOverviewPanel();
    }


    private void updateStatusPanel(CIModel model) {
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