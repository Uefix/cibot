package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.FeedElement;
import com.cibot.feedreader.BuildStatusChecker;
import com.cibot.cimodel.BuildStatus;
import com.cibot.cimodel.CIModel;
import com.cibot.util.CIBotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Uefix
 * @author j-n00b
 */
@Service
public class CIBotController {


    private static final Logger LOG = LoggerFactory.getLogger(CIBotController.class);


    @Autowired
    private CIModel ciModel;


    @Autowired
    private BuildStatusChecker buildStatusChecker;

    @Autowired
    private CIBotConfiguration configuration;



    public void start() {
        Thread thread =
                new Thread() {
                    @Override
                    public void run() {
                        doBuildStatusCheckLoop();
                    }
                };

        thread.setName("CNTRL");
        thread.start();
    }


    //----  I n t e r n a l  ----//

    private void doBuildStatusCheckLoop() {
        final long SLEEP_TIME = 30 * 1000L;
        while (true) {
            CIBotUtil.sleep(SLEEP_TIME);
            try {
                synchronized (ciModel) {
                    ciModel.resetStatusMap();
                    for (FeedElement feed : configuration.getFeedReader().getFeeds()) {
                        BuildStatus status = buildStatusChecker.getBuildStatus(feed);
                        ciModel.setStatusForJob(feed.getJobName(), status);
                    }
                    ciModel.calculateOverallStatus();
                }
                LOG.info("Current build status: {}", ciModel.getOverallStatus());
            } catch (RuntimeException e) {
                LOG.error("Exception while checking status: " + e.getMessage(), e);
                synchronized (ciModel) {
                    ciModel.setOverallStatus(BuildStatus.UNKNOWN);
                }
            } finally {
                ciModel.fireUpdateEvent();
            }
        }
    }
}