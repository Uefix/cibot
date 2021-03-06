package com.cibot.gui;

import com.cibot.cimodel.BuildStatus;
import com.cibot.cimodel.CIModel;
import com.cibot.cimodel.JobKeyComparator;
import com.cibot.config.CIBotConfiguration;
import com.cibot.thumbi.ThumbiConnectionType;
import com.cibot.util.CIBotUtil;

/**
 * @author Uefix
 */
public class CIBotFrameTester {


    //---- Hauptstrecke for tests ----//

    public static void main(String[] args) {
        try {
            CIBotFrame window = new CIBotFrame();
            window.ciModel = new CIModel();
            window.jobKeyComparator = new JobKeyComparator(window.ciModel);

            window.resources = new GUIResources();
            window.resources.configuration = new CIBotConfiguration();
            window.resources.configuration.getGui().getLabelProperties().setFontSize(56);
            window.resources.configuration.getGui().getLabelProperties().setWidth(600);
            window.resources.configuration.getGui().getLabelProperties().setHeight(70);
            window.resources.initialize();

            window.statusPanel = new CIBotStatusPanel();
            window.statusPanel.initialize();

            window.jobOverviewPanel = new CIBotJobOverviewPanel();
            window.jobOverviewPanel.resources = window.resources;
            window.jobOverviewPanel.initialize();

            window.initialize();
            CIBotUtil.sleep(3000);

            for (int i = 0; i < 1; i++) {
                window.ciModel.setStatusForJob("JOB 1", BuildStatus.BUILD_FAILED);
                window.ciModel.setStatusForJob("JOB 2", BuildStatus.BUILD_UNSTABLE);
                window.ciModel.setStatusForJob("JOB 3", BuildStatus.BUILD_OK);
                window.ciModel.calculateOverallStatus();
                window.ciModel.fireUpdateEvent();

                CIBotUtil.sleep(1500);
                window.showConnectedIcon(ThumbiConnectionType.BLUETOOTH);

                window.ciModel.setStatusForJob("JOB 1", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("JOB 2", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("JOB 3", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("JOB 4", BuildStatus.BUILD_OK);
                window.ciModel.calculateOverallStatus();
                window.ciModel.fireUpdateEvent();
                CIBotUtil.sleep(1500);
                window.showConnectedIcon(null);

                CIBotUtil.sleep(1500);
                window.showConnectedIcon(ThumbiConnectionType.USB);

                CIBotUtil.sleep(1500);
                window.showConnectedIcon(null);

                window.ciModel.setStatusForJob("JOB 1", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("JOB 2", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("JOB 3", BuildStatus.UNKNOWN);
                window.ciModel.setStatusForJob("JOB 4", BuildStatus.BUILD_OK);
                window.ciModel.calculateOverallStatus();
                window.ciModel.fireUpdateEvent();

                CIBotUtil.sleep(1500);

                window.ciModel.setStatusForJob("JOB 1", BuildStatus.BUILD_FAILED);
                window.ciModel.setStatusForJob("JOB 2", BuildStatus.BUILD_UNSTABLE);
                window.ciModel.setStatusForJob("JOB 3", BuildStatus.UNKNOWN);
                window.ciModel.setStatusForJob("AB fooBar-NightlyBuild", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("AB fooBar-AllTests", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("XYZ credit", BuildStatus.BUILD_OK);
                window.ciModel.setStatusForJob("XYZ schlumpfIntegration", BuildStatus.BUILD_OK);

                window.ciModel.calculateOverallStatus();
                window.ciModel.fireUpdateEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
