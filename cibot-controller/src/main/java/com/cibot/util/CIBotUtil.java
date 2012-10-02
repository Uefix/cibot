package com.cibot.util;

/**
 * User: Uefix
 * Date: 01.10.12
 * Time: 07:04
 */
public final class CIBotUtil {

    private CIBotUtil() {}

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
