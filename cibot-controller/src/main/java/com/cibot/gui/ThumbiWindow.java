package com.cibot.gui;

import com.cibot.model.BuildStatus;
import com.cibot.model.CIBotModel;
import com.cibot.thumbi.ConnectionListener;
import com.cibot.util.CIBotUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ThumbiWindow extends JFrame implements Observer, ConnectionListener {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbiWindow.class);


    private final Dimension BLUETOOTH_ICON_SIZE = new Dimension(50, 67);


    private CIBotModel model;


    private JLabel thumbLabel;

    private JLabel blueToothLabel;

    private ImageIcon thumbUpIcon;

    private ImageIcon thumbDownIcon;

    private ImageIcon blueToothIcon;

    private JPanel glassPanel;


    public void initialize() {
        Preconditions.checkState(model != null, "Model not set");

        loadIcons();

        thumbLabel = new JLabel(thumbUpIcon);

        initBlueToothLabel();
        initLayout();
        initGlassPanel();

        setIconImage(thumbUpIcon.getImage());
        setTitle("CIBot");
        setBackground(Color.WHITE);
        setSize(475, 475);
        centerFrame();

        updateBuildStatus(model);
        model.addObserver(this);

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
        CIBotModel model = (CIBotModel) obs;
        updateBuildStatus(model);
        thumbLabel.validate();
        thumbLabel.repaint();
    }


    //---- ConnectionListener ----//

    @Override
    public void connected(Object connectionInfo) {
        changeBlueToothConnectedIcon(true);
    }

    @Override
    public void disconnected(Object connectionInfo) {
        changeBlueToothConnectedIcon(false);
    }


    //---- Internal ----//


    private void loadIcons() {
        final ClassLoader classLoader = getClass().getClassLoader();

        URL thumbUpUrl = classLoader.getResource("thumbup.jpg");
        thumbUpIcon = new ImageIcon(thumbUpUrl);

        URL thumbDownUrl = classLoader.getResource("thumbdown.jpg");
        thumbDownIcon = new ImageIcon(thumbDownUrl);

        URL blueToothUrl = classLoader.getResource("bluetooth.png");
        blueToothIcon = new ImageIcon(blueToothUrl);
    }


    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((int) screenSize.getWidth() - getWidth()) >> 1;
        int y = ((int) screenSize.getHeight() - getHeight()) >> 1;
        setLocation(x, y);
    }


    private void initBlueToothLabel() {
        blueToothLabel = new JLabel();
        blueToothLabel.setSize(BLUETOOTH_ICON_SIZE);
        blueToothLabel.setMinimumSize(BLUETOOTH_ICON_SIZE);
        blueToothLabel.setMaximumSize(BLUETOOTH_ICON_SIZE);
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
                new Dimension((int) BLUETOOTH_ICON_SIZE.getWidth() + 10,
                        (int) BLUETOOTH_ICON_SIZE.getHeight() + 10);

        GridBagConstraints con =
                new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                        GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0);

        glassPanel.add(blueToothLabel, con);
        glassPanel.setSize(glassPanelSize);
        glassPanel.setMinimumSize(glassPanelSize);

        getLayeredPane().add(glassPanel, JLayeredPane.POPUP_LAYER);

    }


    private void updateBuildStatus(CIBotModel model) {
        switch (model.getCurrentStatus()) {
            case BUILD_OK:
                thumbLabel.setIcon(thumbUpIcon);
                LOG.info("Build ok");
                break;

            default:
                thumbLabel.setIcon(thumbDownIcon);
                LOG.info("Build NOT ok");
                break;
        }
    }


    private void changeBlueToothConnectedIcon(boolean connected) {
        if (!connected) {
            blueToothLabel.setIcon(null);
        } else {
            blueToothLabel.setIcon(blueToothIcon);
        }
        blueToothLabel.validate();
        blueToothLabel.repaint();
    }


    //---- Getter and Setter ----//

    public void setModel(CIBotModel model) {
        this.model = model;
    }


    //---- Hauptstrecke ;) ----//

    public static void main(String[] args) {

        try {
            CIBotModel model = new CIBotModel();

            ThumbiWindow window = new ThumbiWindow();
            window.setModel(model);
            window.initialize();

            CIBotUtil.sleep(1500);
            model.setCurrentStatus(BuildStatus.BUILD_OK);

            window.changeBlueToothConnectedIcon(true);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
