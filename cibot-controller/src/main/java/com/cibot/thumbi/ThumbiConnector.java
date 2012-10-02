package com.cibot.thumbi;

import com.cibot.config.CIBotConfiguration;
import com.cibot.model.CIBotModel;
import com.cibot.util.CIBotUtil;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InterruptedIOException;

/**
 * User: Uefix
 * Date: 01.10.12
 * Time: 06:59
 */
public class ThumbiConnector implements NXTCommLogListener  {


    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbiConnector.class);


    public static enum ConnectionType {
        USB,
        BLUETOOTH
    }


    private CIBotConfiguration configuration;

    private CIBotModel model;

    private ConnectionListener connectionListener;

    private volatile long lastCommunicationMillis;

    private volatile boolean reconnecting = false;


    private volatile ConnectionThread connectionThread;

    private WatchDogThread watchDogThread;



    public void start() {
        startConnectionThread();
        startWatchDogThread();

        LOGGER.info("ThumbiConnector started.");
    }


    public ConnectionType getConnectionType() {
        String uri = configuration.getThumbi().getConnectionUri();
        if (uri.startsWith("usb://")) {
            return ConnectionType.USB;
        } else if (uri.startsWith("btspp://")) {
            return ConnectionType.BLUETOOTH;
        }
        return null;
    }



    //----  C o n n e c t i o n T h r e a d  ----//


    private void startConnectionThread() {
        connectionThread = new ConnectionThread();
        connectionThread.start();
    }


    private void stopConnectionThread() {
        try {
            if (connectionThread != null) {
                connectionThread.interrupt();
                connectionThread = null;
            }
        } catch (Exception e) {
            LOGGER.error("Stop connection thread failed: ", e);
        }
    }


    private class ConnectionThread extends Thread {

        private DataInputStream inputStream;

        private DataOutputStream outputStream;

        NXTConnector nxtConnector = new NXTConnector();

        public void run(){
            try {
                nxtConnector.addLogListener(ThumbiConnector.this);

                String nxtUri = configuration.getThumbi().getConnectionUri();

                LOGGER.info("Trying to connect to {} ...", nxtUri);

                // Connect to any NXT over Bluetooth
                boolean connected = nxtConnector.connectTo(nxtUri);
                if (!connected) {
                    LOGGER.error("Failed to connect to NXT: {}", nxtUri);
                    return;
                }

                LOGGER.info("Connected to {}", nxtUri);

                inputStream = nxtConnector.getDataIn();
                outputStream = nxtConnector.getDataOut();

                String clientMessage = inputStream.readLine();

                lastCommunicationMillis = System.currentTimeMillis();

                ConnectionType connectionType = getConnectionType();
                connectionListener.connected(connectionType);

                if ("get_status".equals(clientMessage)) {
                    synchronized (model) {
                        outputStream.writeBytes(model.getCurrentStatus().toString() + "\n");
                        outputStream.flush();
                    }
                }
                CIBotUtil.sleep(2000L);

            } catch (InterruptedIOException iioe) {
                LOGGER.debug("Connection thread interrupted.");
            } catch (Exception e) {
                LOGGER.error("Exception in thumbi thread: {}", e.getMessage(), e);
            } finally {
                reconnecting = false;

                connectionListener.disconnected(null);

                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
                try {
                    if (nxtConnector != null) {
                        nxtConnector.close();
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to close nxtConnector: ", e);
                }
            }
        }
    }


    //----  W a t c h D o g T h r e a d  ----//


    private void startWatchDogThread() {
        watchDogThread = new WatchDogThread();
        watchDogThread.start();
    }


    private class WatchDogThread extends Thread {

        public void run() {
            lastCommunicationMillis = System.currentTimeMillis();
            while (true) {
                long duration = System.currentTimeMillis() - lastCommunicationMillis;
                boolean timedOut = duration > configuration.getThumbi().getTimeout();

                if (timedOut) {
                    if (!reconnecting) {
                        reconnecting = true;

                        LOGGER.info("Connection timed out. Trying reconnect...");

                        stopConnectionThread();
                        startConnectionThread();
                    }
                }
                CIBotUtil.sleep(1000L);
            }
        }
    }




    public void setModel(CIBotModel model) {
        this.model = model;
    }


    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }



    public void setConfiguration(CIBotConfiguration configuration) {
        this.configuration = configuration;
    }


    //---- NXTCommLogListener ----//

    public void logEvent(String message) {
        LOGGER.debug("NXTCommLogListener: {}", message);
    }

    public void logEvent(Throwable throwable) {
        LOGGER.debug("NXTCommLogListener: exception={}", throwable.getMessage(), throwable);
    }
}
