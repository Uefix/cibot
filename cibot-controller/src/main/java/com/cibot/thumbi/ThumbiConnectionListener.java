package com.cibot.thumbi;

/**
 * @author Uefix
 */
public interface ThumbiConnectionListener {

    void connected(ThumbiConnectionType connectionType);

    void disconnected();
}
