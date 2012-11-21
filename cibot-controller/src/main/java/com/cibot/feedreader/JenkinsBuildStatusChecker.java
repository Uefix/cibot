package com.cibot.feedreader;

import com.cibot.cimodel.BuildStatus;
import com.cibot.config.CIBotConfiguration;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation that checks the build status of Jenkins CI builds.
 *
 * @author Uefix
 * @author j-n00b
 */
public class JenkinsBuildStatusChecker implements BuildStatusChecker {


    private static final Logger LOG = LoggerFactory.getLogger(JenkinsBuildStatusChecker.class);


    @Autowired
    private CIBotConfiguration configuration;


    @Override
    public BuildStatus getCurrentBuildStatus() throws RuntimeException {
        CIBotConfiguration.Feed feed = null;
        try {
            BuildStatus result = BuildStatus.BUILD_OK;
            Iterator<CIBotConfiguration.Feed> it = configuration.getFeedReader().getFeeds().iterator();
            while (it.hasNext()) {
                feed = it.next();

                String user = null;
                String password = null;

                if (feed.hasLogin()) {
                    CIBotConfiguration.Login loginConfig = configuration.getLogin(feed);
                    user = loginConfig.getUser();
                    password = loginConfig.getPassword();
                }
                BuildStatus statusForUrl = getBuildStatus(feed.getUrl(), user, password);
                if (result != BuildStatus.UNKNOWN) {
                    if (statusForUrl == BuildStatus.UNKNOWN) {
                        result = BuildStatus.UNKNOWN;
                    } else if (statusForUrl != BuildStatus.BUILD_OK) {
                        result = BuildStatus.BUILD_FAILED;
                    }
                }
            }
            return result;
        } catch (Exception e) {
            LOG.error("Error while getting the current build status for " + feed, e);
            return BuildStatus.UNKNOWN;
        }
    }


    //----  I n t e r n a l  ----//


    BuildStatus getBuildStatus(URL url, String user, String password) throws IOException, FeedException {
        // by default we assume the build is broken!
        BuildStatus status = BuildStatus.BUILD_UNSTABLE;

        Reader reader = null;
        try {
            URLConnection con = url.openConnection();
            if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
                con.setRequestProperty(
                        "Authorization",
                        "Basic " + new BASE64Encoder().encode((user + ":" + password).getBytes()));
            }
            con.connect();
            reader = new InputStreamReader(con.getInputStream());

            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(reader);

            @SuppressWarnings("unchecked")
            final List<SyndEntry> feedEntries = feed.getEntries();
            if (feedEntries.size() > 0) {
                final String title = feedEntries.iterator().next().getTitle();

                // FIXME das muss schöner gehen!
                final int beginIndex = title.indexOf('(') + 1;
                final int endIndex = title.lastIndexOf(')');
                String statusString = title.substring(beginIndex, endIndex);

                status = mapBuildStatusString(statusString);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Status for {}: {}", url.toExternalForm(), status.toString());
                }
            }
            return status;
        } catch (Exception e) {
            LOG.debug("{} while reading feed {}", e.getClass().getSimpleName(), url.toExternalForm());
            status = BuildStatus.UNKNOWN;
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return status;
    }


    /**
     * @param statusString
     * @return
     */
    BuildStatus mapBuildStatusString(String statusString) {
        if (StringUtils.isNotEmpty(statusString)) {
            BuildStatus result = configuration.getFeedReader().mapBuildStatus(statusString);
            if (result != null) {
                return result;
            }
        }
        return BuildStatus.UNKNOWN;
    }
}
