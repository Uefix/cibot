package com.cibot.thumbi;

/**
 * @author Uefix
 */
public interface ThumbiConnectionListener {

    void connected(Object connectionInfo);

    void disconnected(Object connectionInfo);
}
