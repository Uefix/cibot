package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.CIBotConfigurationLoader;
import com.cibot.gui.CIBotFrame;
import com.cibot.thumbi.ThumbiConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.net.MalformedURLException;

/**
 * @author j-n00b
 * @author Uefix
 */
public final class CIBotMain {


    private static final Logger LOG = LoggerFactory.getLogger(CIBotMain.class);


    private CIBotMain() {}


    public static void main(String[] args) throws MalformedURLException {
        processConfigurationFileArgument(args);

        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("cibot-application-context.xml");
        ctx.registerShutdownHook();

        CIBotController controller = ctx.getBean(CIBotController.class);
        controller.start();

        CIBotFrame cibotFrame = ctx.getBean(CIBotFrame.class);

        CIBotConfiguration configuration = ctx.getBean(CIBotConfiguration.class);
        if (configuration.getThumbi().isEnabled()) {
            ThumbiConnector thumbiConnector = ctx.getBean(ThumbiConnector.class);
            thumbiConnector.setConnectionListener(cibotFrame);
            thumbiConnector.start();
        }
    }


    private static void processConfigurationFileArgument(String[] args) {
        if (args.length != 1) {
            LOG.error("Path to configuration file not specified.");
            System.exit(-1);
        }

        File configFile = new File(args[0]);
        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
            LOG.error("Configuration file {} is not existent/readable.", configFile.getAbsolutePath());
            System.exit(-2);
        }

        System.setProperty(CIBotConfigurationLoader.CONFIG_FILENAME_SYSPROPERTY, args[0]);
    }
}