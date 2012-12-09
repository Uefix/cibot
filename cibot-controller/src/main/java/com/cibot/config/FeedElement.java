package com.cibot.config;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Uefix
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedElement {

    private static Pattern JOB_BY_FEED_URL_PATTERN = Pattern.compile("^.*/(.*)/rssAll$");


    @XmlAttribute
    private String login;

    @XmlAttribute
    private String jobName;

    @XmlValue
    private URL url;

    public FeedElement() {
    }

    public FeedElement(URL url, String login) {
        this.url = url;
        this.login = login;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        if (jobName == null) {
            return parseJobName();
        }
        return jobName;
    }

    private String parseJobName() {
        if (url != null) {
            try {
                Matcher matcher = JOB_BY_FEED_URL_PATTERN.matcher(url.toExternalForm());
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean hasLogin() {
        return StringUtils.isNotBlank(login);
    }


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedElement feed = (FeedElement) o;

        if (jobName != null ? !jobName.equals(feed.jobName) : feed.jobName != null) return false;
        if (login != null ? !login.equals(feed.login) : feed.login != null) return false;
        if (url != null ? !url.equals(feed.url) : feed.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (jobName != null ? jobName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "login='" + login + '\'' +
                ", jobName='" + jobName + '\'' +
                ", url=" + url +
                '}';
    }
}
