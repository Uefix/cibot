package com.cibot.model;

/**
 * Defines the possible states a single CI build can adopt.
 * 
 * @author j-n00b
 */
public enum BuildStatus {

    /** Indicates that the build is okay and stable */
    BUILD_OK,

    /** Indicates that the build is unstable because some tests have failed */
    BUILD_UNSTABLE,

    /** Indicates that the build is broken because the code does not compile */
    BUILD_FAILED;
}
