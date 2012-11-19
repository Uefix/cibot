package com.cibot.cimodel;

/**
 * Defines the possible states a single CI build can adopt.
 *
 * @author Uefix
 * @author j-n00b
 */
public enum BuildStatus {

    /** Indicates that the status is unknown and can't be determined. */
    UNKNOWN,

    /** Indicates that the build is okay and stable */
    BUILD_OK,

    /** Indicates that the build is unstable because some tests have failed */
    BUILD_UNSTABLE,

    /** Indicates that the build is broken because the code does not compile */
    BUILD_FAILED;


}
