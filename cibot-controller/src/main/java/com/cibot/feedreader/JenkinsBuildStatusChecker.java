package com.cibot.feedreader;

import com.cibot.config.CIBotConfiguration;
import com.cibot.cimodel.BuildStatus;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation that checks the build status of Jenkins CI builds.
 *
 * @author j-n00b
 */
public class JenkinsBuildStatusChecker implements BuildStatusChecker {


    @Autowired
    private CIBotConfiguration configuration;


    @Override
    public BuildStatus getCurrentBuildStatus() throws RuntimeException {
        URL feedUrl = null;
        try {
            for (Iterator<URL> it = configuration.getFeedReader().getFeedUrls().iterator(); it.hasNext(); ) {
                feedUrl = it.next();
                BuildStatus statusForUrl = getBuildStatus(feedUrl);
                if (statusForUrl != BuildStatus.BUILD_OK) {
                    return BuildStatus.BUILD_UNSTABLE;
                }
            }
            return BuildStatus.BUILD_OK;
        } catch (Exception e) {
            throw new RuntimeException("Error while getting the current build status for " + feedUrl, e);
        }
    }


    //----  I n t e r n a l  ----//


    private BuildStatus getBuildStatus(URL url) throws IOException, FeedException {
        // by default we assume the build is broken!
        BuildStatus status = BuildStatus.BUILD_UNSTABLE;

        Reader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
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
            }
            return status;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }


    /**
     * @param statusString
     * @return
     */
    private BuildStatus mapBuildStatusString(String statusString) {
        if (StringUtils.isNotEmpty(statusString)) {
            BuildStatus result = configuration.getFeedReader().mapBuildStatus(statusString);
            if (result != null) {
                return result;
            }
        }
        return BuildStatus.BUILD_FAILED;
    }
}
