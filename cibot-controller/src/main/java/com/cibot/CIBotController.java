package com.cibot;

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
                BuildStatus status = buildStatusChecker.getCurrentBuildStatus();

                LOG.info("Current build status: {}", status);

                synchronized (ciModel) {
                    ciModel.setCurrentStatus(status);
                }
            } catch (RuntimeException e) {
                LOG.error("Exception while checking status: " + e.getMessage(), e);
            }
        }
    }
}