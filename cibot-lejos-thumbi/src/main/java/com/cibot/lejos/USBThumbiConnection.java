package com.cibot.lejos;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * @author Uefix
 */
public class USBThumbiConnection extends BaseThumbiConnection<USBConnection> {

    @Override
    protected USBConnection createConnection(int timeoutInMillis) {
        USBConnection usbConnection = USB.waitForConnection(timeoutInMillis, NXTConnection.PACKET);
        return usbConnection;
    }
}
