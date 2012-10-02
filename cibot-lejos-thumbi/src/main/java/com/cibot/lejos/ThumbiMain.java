package com.cibot.lejos;


import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;

import java.io.IOException;

/**
 * @author Uefix
 */
public final class ThumbiMain {


	public static void main(String[] args) {
        ThumbiMain thumbi = new ThumbiMain();
        thumbi.start();
	}


    private ThumbiConnection connection = null;

    private ThumbiRotator rotator = new ThumbiRotator(Motor.A, 900, 4500);


    private boolean failed = false;

    private int counter = 0;

    private Thread connectionThread;



    public void start() {
        ThumbiLogger.log("ciBOT Thumbi 1.0");

        initRightButton();
        initLeftButton();
        initEnterButton();
        initConnection();

        Button.ESCAPE.waitForPressAndRelease();
    }



    //---- Internal stuff ----//

    private void initConnection() {
        connection = new USBThumbiConnection();
    }


    private void startConnectionLoop() {
        connectionThread =
                new Thread() {

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                processConnectionCycle();
                            } catch (Exception e) {
                            }
                            ThumbiUtil.sleep(1000L);
                        }
                    }
                };
        connectionThread.start();
    }



    private void processConnectionCycle() throws IOException {
        ThumbiLogger.log("ping #" + counter++);
        boolean connected = connection.connect(10000);
        if (connected) {
            ThumbiLogger.log("connected.");

            ThumbiLogger.log("sending: " + ThumbiMessages.REQUEST_GET_STATUS);
            connection.sendMessage(ThumbiMessages.REQUEST_GET_STATUS);

            ThumbiLogger.log("receive...");
            String serverResponse = connection.receiveMessage();
            ThumbiLogger.log("response: " + serverResponse);

            processServerResponse(serverResponse);

            connection.disconnect();
        }
    }


    private void processServerResponse(String serverResponse) {
        if (ThumbiMessages.isBuildOk(serverResponse)) {
            if (failed) {
                failed = false;
                rotator.rotateLeft();
            }
        } else if (ThumbiMessages.isBuildFailedOrUnstable(serverResponse)) {
            if (!failed) {
                failed = true;
                rotator.rotateRight();
            }
        } else {
            if (serverResponse != null) {
                ThumbiLogger.error("Unknown: " + serverResponse);
            }
        }

    }


    private void initEnterButton() {
        Button.ENTER.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed(Button button) {}

            @Override
            public void buttonReleased(Button button) {
                try {
                    startConnectionLoop();
                } catch (Exception e) {
                    ThumbiLogger.error("FAIL: " + e.getMessage());
                }
            }
        });
    }


    private void initLeftButton() {
        Button.LEFT.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed(Button button) {}

            @Override
            public void buttonReleased(Button button) {
                rotator.rotateLeft();
            }
        });
    }


    private void initRightButton() {
        Button.RIGHT.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed(Button button) {}

            @Override
            public void buttonReleased(Button button) {
                rotator.rotateRight();
            }
        });
    }
}