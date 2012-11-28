package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.CIBotConfigurationLoader;
import com.cibot.feedreader.BuildStatusChecker;
import com.cibot.feedreader.JenkinsBuildStatusChecker;
import com.cibot.gui.CIBotFrame;
import com.cibot.gui.CIBotJobOverviewPanel;
import com.cibot.gui.CIBotStatusPanel;
import com.cibot.gui.GUIResources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: Uefix
 * Date: 07.10.12
 * Time: 13:15
 */
@Configuration
public class CIBotApplicationConfiguration {


    @Bean(initMethod = "initialize")
    public GUIResources guiResources() {
        return new GUIResources();
    }

    @Bean(initMethod = "initialize")
    public CIBotFrame guiFrame() {
        return new CIBotFrame();
    }

    @Bean(initMethod = "initialize")
    public CIBotStatusPanel guiStatusPanel() {
        return new CIBotStatusPanel();
    }

    @Bean(initMethod = "initialize")
    public CIBotJobOverviewPanel guiJobOverviewPanel() {
        return new CIBotJobOverviewPanel();
    }


    @Bean
    public BuildStatusChecker buildStatusChecker() {
        return new JenkinsBuildStatusChecker();
    }


    @Bean
    public CIBotConfigurationLoader configurationLoader() {
        return new CIBotConfigurationLoader();
    }


    @Bean
    public CIBotConfiguration configuration() {
        return configurationLoader().loadConfiguration();
    }
}