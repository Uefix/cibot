package com.cibot.lejos;

import lejos.nxt.comm.NXTConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Uefix
 */
public abstract class BaseThumbiConnection<CON extends NXTConnection> implements ThumbiConnection {


    protected DataInputStream inputStream;

    protected DataOutputStream outputStream;

    protected CON connection;


    protected abstract CON createConnection(int timeoutInMillis);


    @Override
    public boolean connect(int timeoutInMillis) {
        connection = createConnection(timeoutInMillis);
        if (connection == null) {
            return false;
        }
        inputStream = connection.openDataInputStream();
        outputStream = connection.openDataOutputStream();
        return true;
    }

    @Override
    public void disconnect() {
        closeInputStream();
        closeOutputStream();
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void sendMessage(String message) throws IOException {
        outputStream.write((message + "\n").getBytes());
        outputStream.flush();
    }

    @Override
    public String receiveMessage() throws IOException {
        String message = inputStream.readLine();
        return message;
    }


    //---- protected stuff ----//

    protected void closeInputStream() {
        if (inputStream != null) {
            try {
                inputStream.close();

            } catch (Exception e) {
                ThumbiLogger.error("Failed to close socket input stream!");
            } finally {
                inputStream = null;
            }
        }
    }


    protected void closeOutputStream() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                ThumbiLogger.error("Failed to close socket output stream!");
            } finally {
                outputStream = null;
            }
        }
    }
}