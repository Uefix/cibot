package com.cibot.gui;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Uefix
 */
public final class GUIUtil {

    private GUIUtil() {}


    public static void centerWindow(Window window) {
        Preconditions.checkArgument(window != null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((int) screenSize.getWidth() - window.getWidth()) >> 1;
        int y = ((int) screenSize.getHeight() - window.getHeight()) >> 1;
        window.setLocation(x, y);
    }


    public static void addExitWindowListener(Window window) {
        Preconditions.checkArgument(window != null);
        window.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent windowEvent) {
                        System.exit(1);
                    }
                });
    }


    public static void forceSize(Component component, Dimension size) {
        Preconditions.checkArgument(component != null);
        Preconditions.checkArgument(size != null);

        component.setMinimumSize(size);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setSize(size);
    }


    public static Dimension reDimension(Dimension dim, int addX, int addY) {
        return new Dimension((int) dim.getWidth() + addX, (int) dim.getHeight() + addY);
    }


    public static GridBagConstraintsBuilder constraintsBuilder() {
        return new GridBagConstraintsBuilder();
    }


    public static class GridBagConstraintsBuilder {

        private GridBagConstraints contraints = new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);


        public GridBagConstraintsBuilder gridx(int gridx) {
            contraints.gridx = gridx;
            return this;
        }

        public GridBagConstraintsBuilder gridy(int gridy) {
            contraints.gridy = gridy;
            return this;
        }

        public GridBagConstraintsBuilder weightx(double weightx) {
            contraints.weightx = weightx;
            return this;
        }

        public GridBagConstraintsBuilder weighty(double weighty) {
            contraints.weighty = weighty;
            return this;
        }

        public GridBagConstraintsBuilder fill(int fill) {
            contraints.fill = fill;
            return this;
        }

        public GridBagConstraintsBuilder fillHorizontal() {
            return fill(GridBagConstraints.HORIZONTAL);
        }

        public GridBagConstraintsBuilder anchor(int anchor) {
            contraints.anchor = anchor;
            return this;
        }

        public GridBagConstraintsBuilder anchorNorth() {
            return anchor(GridBagConstraints.NORTH);
        }


        public GridBagConstraintsBuilder insets(int top, int left, int bottom, int right) {
            contraints.insets = new Insets(top, left, bottom, right);
            return this;
        }


        public GridBagConstraints build() {
            return (GridBagConstraints) contraints.clone();
        }
    }
}
