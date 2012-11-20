package com.cibot.feedreader;

import com.cibot.cimodel.BuildStatus;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Uefix
 */
public class JenkinsBuildStatusCheckerTest {


    private static final Logger LOG = LoggerFactory.getLogger(JenkinsBuildStatusCheckerTest.class);


    private JenkinsBuildStatusChecker checker;

    private String url;
    private String user;
    private String password;

    @Before
    public void setup() {
        checker  = Mockito.spy(new JenkinsBuildStatusChecker());
        mockMapBuildStatusString(BuildStatus.BUILD_OK);

        url = System.getProperty("url");
        user = System.getProperty("user");
        password = System.getProperty("password");
    }



    @Test
    public void getBuildStatus_withCredentials_returnsMockedResult() throws Exception {
        if (!isLoginConfigAvailable()) {
            LOG.debug("Skipping test because login configuration is not available");
            return;
        }

        BuildStatus result = checker.getBuildStatus(new URL(url), user, password);
        assertEquals(BuildStatus.BUILD_OK, result);
    }




    private void mockMapBuildStatusString(BuildStatus result) {
        when(checker.mapBuildStatusString(anyString())).thenReturn(result);
    }


    private boolean isLoginConfigAvailable() {
        if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
            return true;
        }
        return false;
    }
}
