package com.cibot.gui;

import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author Uefix
 */
public class CIBotStatusPanel extends JPanel {


    private JSVGCanvas canvas;


    void initialize() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        initCanvas();
    }


    public void setCanvasSVGUrl(URL svgUrl) {
        canvas.setURI(svgUrl.toString());
    }


    private void initCanvas() {
        canvas = new JSVGCanvas();
        canvas.setDisableInteractions(true);
        canvas.setEnableImageZoomInteractor(false);
        canvas.setEnablePanInteractor(false);
        canvas.setEnableResetTransformInteractor(false);
        canvas.setEnableRotateInteractor(false);
        canvas.setEnableZoomInteractor(false);

        add(canvas, GUIUtil.constraintsBuilder().build());
    }
}