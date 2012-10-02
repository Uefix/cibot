package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.CIBotConfigurationLoader;
import com.cibot.feedreader.JenkinsBuildStatusChecker;
import com.cibot.gui.ThumbiWindow;
import com.cibot.model.CIBotModel;
import com.cibot.thumbi.ThumbiConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

/**
 * @author j-n00b
 * @author Uefix
 */
public class ControllerMain {


    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerMain.class);


    public static void main(String[] args) throws MalformedURLException {
        CIBotConfigurationLoader configurationLoader = new CIBotConfigurationLoader();
        CIBotConfiguration configuration = configurationLoader.loadConfiguration("cibot.xml");

        CIBotModel model = new CIBotModel();

        JenkinsBuildStatusChecker buildStatusChecker = new JenkinsBuildStatusChecker();
        buildStatusChecker.setConfiguration(configuration);
        buildStatusChecker.initialize();

        CIBotController controller = new CIBotController();
        controller.setModel(model);
        controller.setConfiguration(configuration);
        controller.setBuildStatusChecker(buildStatusChecker);
        controller.startTheShow();

        ThumbiWindow thumbiWindow = new ThumbiWindow();
        thumbiWindow.setModel(model);
        thumbiWindow.initialize();

        ThumbiConnector thumbiConnector = new ThumbiConnector();
        thumbiConnector.setConfiguration(configuration);
        thumbiConnector.setModel(model);
        thumbiConnector.setConnectionListener(thumbiWindow);
        thumbiConnector.start();

        LOGGER.info("Controller started.");

        model.addObserver(
                new Observer() {
                    @Override
                    public void update(Observable obs, Object arg) {
                        CIBotModel model = (CIBotModel) obs;
                        LOGGER.info("Current status: {}", model.getCurrentStatus());
                    }
                });
    }
}