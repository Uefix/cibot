package com.cibot.gui;

import com.cibot.cimodel.BuildStatus;
import com.cibot.cimodel.CIModel;
import com.cibot.thumbi.ThumbiConnectionListener;
import com.cibot.thumbi.ThumbiConnectionType;
import com.cibot.util.CIBotUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Uefix
 */
public class CIBotFrame extends JFrame implements Observer, ThumbiConnectionListener {


    private static final Logger LOG = LoggerFactory.getLogger(CIBotFrame.class);


    private final Dimension CONNECTION_ICON_SIZE = new Dimension(50, 67);


    @Autowired
    private CIModel ciModel;


    private JLabel thumbLabel;

    private JLabel connectedLabel;


    private ImageIcon thumbUpIcon;

    private ImageIcon thumbDownIcon;

    private ImageIcon jenkinsUnavailableIcon;

    private ImageIcon blueToothIcon;

    private ImageIcon usbIcon;


    private JPanel glassPanel;


    public void initialize() {
        Preconditions.checkState(ciModel != null, "Model not set");

        loadIcons();

        thumbLabel = new JLabel(thumbUpIcon);

        initConnectedLabel();

        initLayout();
        initGlassPanel();

        setIconImage(thumbUpIcon.getImage());
        setTitle("ciBOT");
        getContentPane().setBackground(Color.WHITE);
        setSize(475, 475);
        centerFrame();

        updateBuildStatus(ciModel);
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



    private void initLayout() {
        setLayout(new GridBagLayout());

        GridBagConstraints con =
                new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

        add(thumbLabel, con);
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
            window.showConnectedIcon(ThumbiConnectionType.BLUETOOTH);

            window.ciModel.setOverallStatus(BuildStatus.BUILD_OK);
            CIBotUtil.sleep(1500);
            window.showConnectedIcon(null);

            CIBotUtil.sleep(1500);
            window.showConnectedIcon(ThumbiConnectionType.USB);

            CIBotUtil.sleep(1500);
            window.showConnectedIcon(null);
            window.ciModel.setOverallStatus(BuildStatus.UNKNOWN);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
