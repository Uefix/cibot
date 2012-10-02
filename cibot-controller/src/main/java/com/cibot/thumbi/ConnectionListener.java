package com.cibot.thumbi;

/**
 * @author Uefix
 */
public interface ConnectionListener {

    void connected(Object connectionInfo);

    void disconnected(Object connectionInfo);
}
