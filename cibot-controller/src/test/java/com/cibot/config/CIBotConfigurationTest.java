package com.cibot.config;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author Uefix.
 */
public class CIBotConfigurationTest {

    private CIBotConfiguration.Feed feed;

    private CIBotConfiguration config;

    @Before
    public void setup() throws Exception {
        feed = new CIBotConfiguration.Feed(new URL("http://JENKINS_HOST:8080/jenkins/testjob/NAME_OF_THE_JOB1/rssAll"), null);
        config = new CIBotConfiguration();
        config.getFeedReader().getFeeds().add(feed);
    }


    @Test
    public void feedGetJobName_noJobNameConfigured_returnsJobnameDefinedByUrl() {
        assertEquals("NAME_OF_THE_JOB1", feed.getJobName());
    }

    @Test
    public void feedGetJobName_jobNameConfigured_returnsExpectedJobName() {
        feed.setJobName("theJob");

        assertEquals("theJob", feed.getJobName());
    }
}
