package com.cibot.lejos;

import java.io.IOException;

/**
 * @author Uefix
 */
public interface ThumbiConnection {


    boolean connect(int timeoutInMillis);

    void disconnect();


    void sendMessage(String message) throws IOException;

    String receiveMessage() throws IOException;
}