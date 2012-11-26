package com.cibot.gui;

import com.cibot.cimodel.BuildStatus;
import com.cibot.cimodel.CIModel;
import com.cibot.thumbi.ThumbiConnectionListener;
import com.cibot.thumbi.ThumbiConnectionType;
import com.cibot.util.CIBotUtil;
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
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Uefix
 */
public class CIBotFrame extends JFrame implements Observer, ThumbiConnectionListener {


    private static final Logger LOG = LoggerFactory.getLogger(CIBotFrame.class);


    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    private static final Dimension CONNECTION_ICON_SIZE = new Dimension(50, 67);

    private static final Font JOB_NAME_FONT = new Font("Arial", Font.BOLD, 14);

    private static final Font JOB_NAME_FONT_ITALIC = new Font("Arial", Font.ITALIC | Font.BOLD, 14);

    private static final Color LIGHT_RED = new Color(255, 55, 55);

    private static final Color LIGHT_GREEN = new Color(55, 255, 55);


    @Autowired
    private CIModel ciModel;

    private JLabel thumbLabel;


    private JLabel connectedLabel;

    private ImageIcon thumbUpIcon;

    private ImageIcon thumbDownIcon;

    private ImageIcon jenkinsUnavailableIcon;

    private ImageIcon blueToothIcon;

    private ImageIcon usbIcon;


    private ImageIcon logoIcon;

    private JPanel glassPanel;

    private JPanel statusPanel;
    private JPanel jobOverviewPanel;


    public void initialize() {
        Preconditions.checkState(ciModel != null, "Model not set");

        loadIcons();
        thumbLabel = new JLabel(jenkinsUnavailableIcon);
        thumbLabel.setMinimumSize(new Dimension(400, 435));
        thumbLabel.setPreferredSize(thumbLabel.getMinimumSize());
        initConnectedLabel();

        setLayout(new GridBagLayout());

        initStatusPanel();
        initJobOverviewPanel();
        initGlassPanel();

        setIconImage(logoIcon.getImage());
        setTitle("ciBOT");
        getContentPane().setBackground(Color.WHITE);
        setSize(685, 475);
        centerFrame();

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
        thumbLabel.validate();
        thumbLabel.repaint();
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


    private void loadIcons() {
        final ClassLoader classLoader = getClass().getClassLoader();

        URL thumbUpUrl = classLoader.getResource("images/thumbup.jpg");
        thumbUpIcon = new ImageIcon(thumbUpUrl);

        URL thumbDownUrl = classLoader.getResource("images/thumbdown.jpg");
        thumbDownIcon = new ImageIcon(thumbDownUrl);

        URL blueToothUrl = classLoader.getResource("images/bluetooth.png");
        blueToothIcon = new ImageIcon(blueToothUrl);

        URL usbUrl = classLoader.getResource("images/usb.png");
        usbIcon = new ImageIcon(usbUrl);

        URL jenkinsUnavailableIconUrl = classLoader.getResource("images/jenkins_unavailable.png");
        jenkinsUnavailableIcon = new ImageIcon(jenkinsUnavailableIconUrl);

        URL logoIconUrl = classLoader.getResource("images/logo.png");
        logoIcon = new ImageIcon(logoIconUrl);
    }


    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((int) screenSize.getWidth() - getWidth()) >> 1;
        int y = ((int) screenSize.getHeight() - getHeight()) >> 1;
        setLocation(x, y);
    }


    private void initConnectedLabel() {
        connectedLabel = new JLabel();
        connectedLabel.setSize(CONNECTION_ICON_SIZE);
        connectedLabel.setMinimumSize(CONNECTION_ICON_SIZE);
        connectedLabel.setMaximumSize(CONNECTION_ICON_SIZE);
    }



    private void initStatusPanel() {
        statusPanel = new JPanel();
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setLayout(new GridBagLayout());

        GridBagConstraints con =
                new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE, ZERO_INSETS, 0, 0);

        statusPanel.add(thumbLabel, con);

        con = new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, ZERO_INSETS, 0, 0);

        getContentPane().add(statusPanel, con);
    }


    private void initJobOverviewPanel() {
        jobOverviewPanel = new JPanel();
        jobOverviewPanel.setBackground(Color.WHITE);
        jobOverviewPanel.setLayout(new GridBagLayout());

        addJobLabel("Awaiting CI status...", null);
        addDummyToOverviewPanel();

        GridBagConstraints con =
                new GridBagConstraints(2, 1, 1, 1, 0.1d, 1.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH, ZERO_INSETS, 0, 0);

        getContentPane().add(jobOverviewPanel, con);
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
        label.setMinimumSize(new Dimension(250, 30));
        label.setPreferredSize(label.getMinimumSize());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new LineBorder(borderColor, 1, false));

        GridBagConstraints con =
                new GridBagConstraints(1, jobOverviewPanel.getComponentCount() + 1, 1, 1, 1.0d, 0.0d,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 0), 0, 0);

        jobOverviewPanel.add(label, con);
    }

    private void addDummyToOverviewPanel() {
        GridBagConstraints con =
                    new GridBagConstraints(1, jobOverviewPanel.getComponentCount() + 1, 1, 1, 1.0d, 1.0d,
                            GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(1, 1, 1, 0), 0, 0);

        jobOverviewPanel.add(new JLabel(), con);
    }


    private void initGlassPanel() {
        glassPanel = new JPanel(new GridBagLayout());

        Dimension glassPanelSize =
                new Dimension((int) CONNECTION_ICON_SIZE.getWidth() + 10,
                        (int) CONNECTION_ICON_SIZE.getHeight() + 10);

        GridBagConstraints con =
                new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                        GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0);

        glassPanel.add(connectedLabel, con);
        glassPanel.setSize(glassPanelSize);
        glassPanel.setMinimumSize(glassPanelSize);
        glassPanel.setBackground(Color.WHITE);

        getLayeredPane().add(glassPanel, JLayeredPane.POPUP_LAYER);

    }


    private void updateBuildStatus(CIModel model) {
        synchronized (model) {
            switch (model.getOverallStatus()) {
                case BUILD_OK:
                    thumbLabel.setIcon(thumbUpIcon);
                    LOG.info("Build ok");
                    break;

                case UNKNOWN:
                    thumbLabel.setIcon(jenkinsUnavailableIcon);
                    LOG.info("Build status unknown");
                    break;

                default:
                    thumbLabel.setIcon(thumbDownIcon);
                    LOG.info("Build NOT ok");
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


    private void showConnectedIcon(ThumbiConnectionType connectionType) {
        if (connectionType == null) {
            connectedLabel.setIcon(null);
        } else if (connectionType == ThumbiConnectionType.BLUETOOTH) {
            connectedLabel.setIcon(blueToothIcon);
        } else if (connectionType == ThumbiConnectionType.USB) {
            connectedLabel.setIcon(usbIcon);
        }
        connectedLabel.validate();
        connectedLabel.repaint();
    }



    //---- Hauptstrecke for tests ----//

    public static void main(String[] args) {
        try {
            CIBotFrame window = new CIBotFrame();
            window.ciModel = new CIModel();
            window.initialize();
            CIBotUtil.sleep(1500);

            window.ciModel.setStatusForJob("AuthE BS-all", BuildStatus.BUILD_FAILED);
            window.ciModel.setStatusForJob("AuthE Nightly", BuildStatus.BUILD_UNSTABLE);
            window.ciModel.setStatusForJob("PHP credit", BuildStatus.BUILD_OK);
            window.ciModel.calculateOverallStatus();
            window.ciModel.fireUpdateEvent();

            CIBotUtil.sleep(1500);
            window.showConnectedIcon(ThumbiConnectionType.BLUETOOTH);

            window.ciModel.setStatusForJob("AuthE BS-all", BuildStatus.BUILD_OK);
            window.ciModel.setStatusForJob("AuthE Nightly", BuildStatus.BUILD_OK);
            window.ciModel.setStatusForJob("PHP credit", BuildStatus.BUILD_OK);
            window.ciModel.setStatusForJob("PHP accountingSystemIntegration", BuildStatus.BUILD_OK);
            window.ciModel.calculateOverallStatus();
            window.ciModel.fireUpdateEvent();
            CIBotUtil.sleep(1500);
            window.showConnectedIcon(null);

            CIBotUtil.sleep(1500);
            window.showConnectedIcon(ThumbiConnectionType.USB);

            CIBotUtil.sleep(1500);
            window.showConnectedIcon(null);

            window.ciModel.setStatusForJob("AuthE BS-all", BuildStatus.BUILD_OK);
            window.ciModel.setStatusForJob("AuthE Nightly", BuildStatus.BUILD_OK);
            window.ciModel.setStatusForJob("PHP credit", BuildStatus.UNKNOWN);
            window.ciModel.setStatusForJob("PHP accountingSystemIntegration", BuildStatus.BUILD_OK);
            window.ciModel.calculateOverallStatus();
            window.ciModel.fireUpdateEvent();

            CIBotUtil.sleep(1500);

            window.ciModel.setStatusForJob("AuthE BS-all", BuildStatus.BUILD_FAILED);
            window.ciModel.setStatusForJob("AuthE Nightly", BuildStatus.BUILD_UNSTABLE);
            window.ciModel.setStatusForJob("PHP credit", BuildStatus.UNKNOWN);
            window.ciModel.setStatusForJob("PHP accountingSystemIntegration", BuildStatus.BUILD_OK);

            window.ciModel.calculateOverallStatus();
            window.ciModel.fireUpdateEvent();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
