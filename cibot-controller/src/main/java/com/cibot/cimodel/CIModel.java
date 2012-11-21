package com.cibot.cimodel;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * This cimodel contains the current build status observed by all CIBots.
 *
 * @author Uefix
 * @author j-n00b
 */
@Component
public class CIModel extends Observable {


    private static final Logger LOG = LoggerFactory.getLogger(CIModel.class);


    private BuildStatus overallStatus = BuildStatus.UNKNOWN;


    private Map<String,BuildStatus> jobStatusMap = Maps.newHashMap();


    /**
     * Gets the overallStatus
     *
     * @return the overallStatus
     */
    public BuildStatus getOverallStatus() {
        return overallStatus;
    }

    /**
     * Sets the overallStatus
     *
     * @param overallStatus the overallStatus to set
     */
    public void setOverallStatus(BuildStatus overallStatus) {
        Preconditions.checkArgument(overallStatus != null, "Given status must not be null");
        this.overallStatus = overallStatus;
    }



    public void resetStatusMap() {
        jobStatusMap.clear();
    }

    public void setStatusForJob(String jobKey, BuildStatus status) {
        jobStatusMap.put(jobKey, status);
    }

    public BuildStatus getStatusForJob(String jobKey) {
        return jobStatusMap.get(jobKey);
    }

    public Set<String> getJobKeys() {
        return jobStatusMap.keySet();
    }


    public void calculateOverallStatus() {
        boolean failed = false;
        for (String jobName : jobStatusMap.keySet()) {
            BuildStatus status = jobStatusMap.get(jobName);
            switch (status) {
                case UNKNOWN:
                    setOverallStatus(status);
                    return;

                case BUILD_FAILED:
                case BUILD_UNSTABLE:
                    failed = true;
            }

        }
        setOverallStatus(failed ? BuildStatus.BUILD_FAILED : BuildStatus.BUILD_OK);
    }


    public void fireUpdateEvent() {
        setChanged();
        notifyObservers();
    }
}
