package com.cibot.lejos;

import lejos.nxt.NXTRegulatedMotor;

/**
 * @author Uefix
 */
public final class ThumbiRotator {


    private final NXTRegulatedMotor motor;

    private final int speed;

    private final int angel;


    public ThumbiRotator(NXTRegulatedMotor motor, int speed, int angel) {
        this.motor = motor;
        this.speed = speed;
        this.angel = angel;
    }


    public void rotateLeft() {
        ThumbiLogger.log("rotating L...");
        rotate(false);
        ThumbiLogger.log("done.");
    }


    public void rotateRight() {
        ThumbiLogger.log("rotating R...");
        rotate(true);
        ThumbiLogger.log("done.");
    }



    //---- Internal stuff ----//

    private void rotate(boolean positiveAngel) {
        motor.setSpeed(speed);
        int angelCalculated = angel;
        if (!positiveAngel) {
            angelCalculated = -1 * angel;
        }
        motor.rotate(angelCalculated);
    }
}