package com.cibot.config;

import com.cibot.model.BuildStatus;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Uefix
 * Date: 25.09.12
 * Time: 20:49
 */
public class CIBotConfigurationLoaderTest {


    public static final String TEST_CONFIG_FILE = "test-config.xml";


    private CIBotConfigurationLoader loader = new CIBotConfigurationLoader();


    @Test
    public void loadConfiguration_testConfig_returnsExpectedResult() {
        CIBotConfiguration result = loader.loadConfiguration(TEST_CONFIG_FILE);

        CIBotConfiguration expected = new CIBotConfiguration();
        expected.getThumbi().setConnectionUri("usb://NXT_A");
        expected.getThumbi().setTimeout(10000L);

        CIBotConfiguration.FeedReader expectedFeedReader = expected.getFeedReader();
        expectedFeedReader.getFeedUrls().add("http://JENKINS_HOST:8080/jenkins/job/NAME_OF_THE_JOB1/rssAll");
        expectedFeedReader.getFeedUrls().add("http://JENKINS_HOST:8080/jenkins/job/NAME_OF_THE_JOB2/rssAll");

        expectedFeedReader.getStatusMappings().putAll(
                BuildStatus.BUILD_OK, Lists.newArrayList("stabil", "back to normal"));

        expectedFeedReader.getStatusMappings().putAll(
                BuildStatus.BUILD_UNSTABLE, Lists.newArrayList("tests failures"));

        assertEquals(expected, result);
    }
}