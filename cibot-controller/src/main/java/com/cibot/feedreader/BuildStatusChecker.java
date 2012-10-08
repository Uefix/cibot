package com.cibot.feedreader;

import com.cibot.cimodel.BuildStatus;

/**
 * Utility to determine the current status of a CI build.
 * 
 * @author j-n00b
 */
public interface BuildStatusChecker {

    /**
     * Returns the status of the latest CI build as defined in {@link BuildStatus}
     * <p>
     * <strong>Note:</strong> If the feed does not contain any entries, {@link BuildStatus#BUILD_UNSTABLE} is returned!
     * </p>
     *  
     * @return the current status of the build
     * @throws IllegalStateException if checking the build status fails for any reason
     */
    BuildStatus getCurrentBuildStatus() throws RuntimeException;
}