package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.feedreader.BuildStatusChecker;
import com.cibot.model.BuildStatus;
import com.cibot.model.CIBotModel;
import com.cibot.util.CIBotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author j-n00b
 */
public class CIBotController {

    private static final Logger LOG = LoggerFactory.getLogger(CIBotController.class);


    private CIBotModel model;

    private CIBotConfiguration configuration;

    private BuildStatusChecker buildStatusChecker;


    private final Runnable runnable;


    public CIBotController() {
        this.runnable =
                new Runnable() {
                    @Override
                    public void run() {
                        final long sleepTime = 30 * 1000L;
                        while (true) {
                            // sleep before getting the first status
                            CIBotUtil.sleep(sleepTime);
                            try {
                                BuildStatus status = buildStatusChecker.getCurrentBuildStatus();
                                LOG.info("Current build status: {}", status);
                                synchronized (model) {
                                    model.setCurrentStatus(status);
                                }
                            }
                            // TODO CheckBuildStatusException?
                            catch (RuntimeException e) {
                                LOG.error("Exception while checking status: " + e.getMessage(), e);
                            }
                        }
                    }
                };
    }


    public void startTheShow() {
        new Thread(runnable).start();
    }


    public void setConfiguration(CIBotConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setModel(CIBotModel model) {
        this.model = model;
    }

    public void setBuildStatusChecker(BuildStatusChecker buildStatusChecker) {
        this.buildStatusChecker = buildStatusChecker;
    }
}
