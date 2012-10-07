package com.cibot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author j-n00b
 * @author Uefix
 */
public class ControllerMain {


    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerMain.class);

    /*
    public static void main(String[] args) throws MalformedURLException {
        CIBotConfigurationLoader configurationLoader = new CIBotConfigurationLoader();
        CIBotConfiguration configuration = configurationLoader.loadConfiguration("cibot.xml");

        CIModel cimodel = new CIModel();

        JenkinsBuildStatusChecker buildStatusChecker = new JenkinsBuildStatusChecker();
        buildStatusChecker.setConfiguration(configuration);
        buildStatusChecker.initialize();

        CIBotController controller = new CIBotController();
        controller.setCiModel(cimodel);
        controller.setConfiguration(configuration);
        controller.setBuildStatusChecker(buildStatusChecker);
        controller.startTheShow();

        CIBotFrame thumbiWindow = new CIBotFrame();
        thumbiWindow.setCiModel(cimodel);
        thumbiWindow.initialize();

        ThumbiConnector thumbiConnector = new ThumbiConnector();
        thumbiConnector.setConfiguration(configuration);
        thumbiConnector.setCiModel(cimodel);
        thumbiConnector.setConnectionListener(thumbiWindow);
        thumbiConnector.start();

        LOGGER.info("Controller started.");

        cimodel.addObserver(
                new Observer() {
                    @Override
                    public void update(Observable obs, Object arg) {
                        CIModel cimodel = (CIModel) obs;
                        LOGGER.info("Current status: {}", cimodel.getCurrentStatus());
                    }
                });
    }   */
}