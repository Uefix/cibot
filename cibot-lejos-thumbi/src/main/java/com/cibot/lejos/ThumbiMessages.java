package com.cibot.lejos;

/**
 * @author BillSAFE
 */
public final class ThumbiMessages {


    public static final String REQUEST_GET_STATUS = "get_status";


    /** Indicates that the build is unknown (Jenkins unavailable). */
    public static final String RESPONSE_GET_STATUS_UNKNOWN = "UNKNOWN";

    /** Indicates that the build is okay and stable */
    public static final String RESPONSE_GET_STATUS_BUILD_OK = "BUILD_OK";

    /** Indicates that the build is unstable because some tests have failed */
    public static final String RESPONSE_GET_STATUS_BUILD_UNSTABLE = "BUILD_UNSTABLE";

    /** Indicates that the build is broken because the code does not compile */
    public static final String RESPONSE_GET_STATUS_BUILD_FAILED = "BUILD_FAILED";


    public static boolean isBuildUnknown(String serverResponse) {
        return RESPONSE_GET_STATUS_UNKNOWN.equals(serverResponse);
    }

    public static boolean isBuildOk(String serverResponse) {
        return RESPONSE_GET_STATUS_BUILD_OK.equals(serverResponse);
    }

    public static boolean isBuildFailedOrUnstable(String serverResponse) {
        return RESPONSE_GET_STATUS_BUILD_UNSTABLE.equals(serverResponse) || RESPONSE_GET_STATUS_BUILD_FAILED.equals(serverResponse);
    }
}
