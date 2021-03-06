package com.cibot.gui;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.LabelPropertiesElement;
import com.cibot.util.CIBotUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author Uefix
 */
public final class GUIResources {


    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    public static final Dimension CONNECTION_ICON_SIZE = new Dimension(50, 67);


    public static final Color BG_COLOR = Color.WHITE;


    private ImageIcon blueToothIcon;

    private ImageIcon usbIcon;

    private ImageIcon logoIcon;


    private URL thumbDownUrl;

    private URL thumbUpUrl;

    private URL jenkinsUnavailableUrl;


    private Font jobNameFont;

    private Font jobNameFontItalic;

    private Dimension jobLabelSize;


    @Autowired
    CIBotConfiguration configuration;


    public void initialize() {
        initSVGCanvasUrls();
        loadIcons();

        LabelPropertiesElement labelProperties = configuration.getGui().getLabelProperties();
        int fontSize = labelProperties.getFontSize();
        jobNameFont = new Font("Arial", Font.BOLD, fontSize);
        jobNameFontItalic = new Font("Arial", Font.BOLD | Font.ITALIC, fontSize);
        jobLabelSize = new Dimension(labelProperties.getWidth(), labelProperties.getHeight());

    }


    //----  G e t t e r   &   S e t t e r  ----//

    public ImageIcon getBlueToothIcon() {
        return blueToothIcon;
    }

    public ImageIcon getUsbIcon() {
        return usbIcon;
    }

    public ImageIcon getLogoIcon() {
        return logoIcon;
    }

    public URL getThumbDownUrl() {
        return thumbDownUrl;
    }

    public URL getThumbUpUrl() {
        return thumbUpUrl;
    }

    public URL getJenkinsUnavailableUrl() {
        return jenkinsUnavailableUrl;
    }


    public Dimension getJobLabelSize() {
        return jobLabelSize;
    }

    public Font getJobNameFont() {
        return jobNameFont;
    }

    public Font getJobNameFontItalic() {
        return jobNameFontItalic;
    }


    //----  I n t e r n a l  ----//

    private void initSVGCanvasUrls() {
        thumbUpUrl = CIBotUtil.getResource("images/thumbup.svg");
        thumbDownUrl = CIBotUtil.getResource("images/thumbdown.svg");
        jenkinsUnavailableUrl = CIBotUtil.getResource("images/jenkins_unavailable.svg");
    }


    private void loadIcons() {
        URL blueToothUrl = CIBotUtil.getResource("images/bluetooth.png");
        blueToothIcon = new ImageIcon(blueToothUrl);

        URL usbUrl = CIBotUtil.getResource("images/usb.png");
        usbIcon = new ImageIcon(usbUrl);

        URL logoIconUrl = CIBotUtil.getResource("images/logo.png");
        logoIcon = new ImageIcon(logoIconUrl);
    }
}
