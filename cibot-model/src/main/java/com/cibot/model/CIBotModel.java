package com.cibot.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

/**
 * This model contains the current build status observed by all CIBots.
 * 
 * @author j-n00b
 */
public class CIBotModel extends Observable {

    private static final Logger LOG = LoggerFactory.getLogger(CIBotModel.class);

    private BuildStatus currentStatus = BuildStatus.BUILD_FAILED;

    /**
     * Gets the currentStatus
     * 
     * @return the currentStatus
     */
    public BuildStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets the currentStatus
     * 
     * @param currentStatus the currentStatus to set
     */
    public void setCurrentStatus(BuildStatus currentStatus) {
        // we do not accept null as current status!
        if (currentStatus == null) {
            return;
        }

        BuildStatus oldStatus = this.currentStatus;
        this.currentStatus = currentStatus;

        if (!oldStatus.equals(currentStatus)) {
            LOG.info("build status has changed from {} to {}", oldStatus, currentStatus);
            setChanged();
            notifyObservers();
        }
    }
}
