package com.cibot.util;

import java.net.URL;

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


    public static URL getResource(String name) {
        return CIBotUtil.class.getClassLoader().getResource(name);
    }
}
