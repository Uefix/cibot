package com.cibot;

import com.cibot.config.CIBotConfiguration;
import com.cibot.config.CIBotConfigurationLoader;
import com.cibot.feedreader.BuildStatusChecker;
import com.cibot.feedreader.JenkinsBuildStatusChecker;
import com.cibot.gui.CIBotFrame;
import com.cibot.thumbi.ThumbiConnector;
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
    public CIBotFrame cibotFrame() {
        return new CIBotFrame();
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