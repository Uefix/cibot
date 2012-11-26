package com.cibot.gui;

import com.cibot.util.CIBotUtil;
import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * .
 */
public class JSVGCanvasTester extends JFrame {

    public static void main(String[] args) {
        JSVGCanvasTester tester = new JSVGCanvasTester();
        try {
            tester.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSVGCanvas canvas;

    private void initialize() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        setSize(600, 600);
        centerFrame();


        initCanvas();

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(canvas,
                new GridBagConstraints(
                        1, 1, 1, 1, 1.0d, 1.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        setVisible(true);
    }

    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((int) screenSize.getWidth() - getWidth()) >> 1;
        int y = ((int) screenSize.getHeight() - getHeight()) >> 1;
        setLocation(x, y);
    }


    private void initCanvas() {
        URL url = getClass().getClassLoader().getResource("images/thumbup.svg");

        canvas = new JSVGCanvas();
        canvas.setURI(url.toString());

    }
}
