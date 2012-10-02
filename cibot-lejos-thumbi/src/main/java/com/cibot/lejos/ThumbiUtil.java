package com.cibot.lejos;

/**
 * @author Uefix
 */
public final class ThumbiUtil {

    public static void sleep(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (Exception e) {}
    }
}
