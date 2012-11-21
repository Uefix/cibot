package com.cibot.cimodel;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Observable;

/**
 * This cimodel contains the current build status observed by all CIBots.
 *
 * @author Uefix
 * @author j-n00b
 */
@Component
public class CIModel extends Observable {


    private static final Logger LOG = LoggerFactory.getLogger(CIModel.class);


    private BuildStatus currentStatus = BuildStatus.UNKNOWN;


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
        Preconditions.checkArgument(currentStatus != null, "Given status must not be null");

        BuildStatus oldStatus = this.currentStatus;
        this.currentStatus = currentStatus;

        if (!oldStatus.equals(currentStatus)) {
            LOG.info("build status has changed from {} to {}", oldStatus, currentStatus);
            setChanged();
            notifyObservers();
        }
    }
}
