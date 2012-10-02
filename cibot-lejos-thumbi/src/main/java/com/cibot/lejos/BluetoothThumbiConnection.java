package com.cibot.lejos;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * @author Uefix
 */
public class BluetoothThumbiConnection extends BaseThumbiConnection<BTConnection> {


    @Override
    protected BTConnection createConnection(int timeout) {
        BTConnection connection = Bluetooth.waitForConnection(timeout, NXTConnection.PACKET);

        ThumbiLogger.log("address: " + connection.getAddress());
        ThumbiLogger.log("signal strength: " + connection.getSignalStrength());
        return connection;
    }
}