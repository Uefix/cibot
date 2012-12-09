package com.cibot.config;

import com.cibot.cimodel.BuildStatus;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @author Uefix
*/
@XmlRootElement
public class FeedReaderElement {

    private Map<String,LoginElement> logins = Maps.newHashMap();

    private List<FeedElement> feeds = Lists.newArrayList();

    /**
     * http://www.mail-archive.com/cxf-user@incubator.apache.org/msg04723.html
     * http://www.java-forum.org/xml-co/116454-jaxb-hashmap.html
     */
    private ArrayListMultimap<BuildStatus,String> statusMappings = ArrayListMultimap.create();


    public Map<String,LoginElement> getLogins() {
        return logins;
    }

    @XmlElementWrapper(name = "logins")
    @XmlElement(name = "login")
    public List<LoginElement> getLoginsList() {
        return Lists.newArrayList(logins.values());
    }

    public void setLoginsList(List<LoginElement> loginsList) {
        logins.clear();
        for (LoginElement login : loginsList) {
            logins.put(login.getId(), login);
        }
    }


    @XmlElementWrapper(name = "feeds")
    @XmlElement(name = "feed")
    public List<FeedElement> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedElement> feeds) {
        this.feeds = feeds;
    }

    public ArrayListMultimap<BuildStatus, String> getStatusMappings() {
        return statusMappings;
    }


    public BuildStatus mapBuildStatus(String buildStatusString) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(buildStatusString),
                "Given build status string must not be empty");

        buildStatusString = buildStatusString.toLowerCase();

        for (BuildStatus status : statusMappings.keySet()) {
            if (statusMappings.asMap().get(status).contains(buildStatusString)) {
                return status;
            }
        }
        return null;
    }


    @XmlElement(name = "statusMapping")
    private StatusMapping[] getStatusMappingArray() {
        List<StatusMapping> mappingsList = Lists.newArrayList();
        for (Map.Entry<BuildStatus, Collection<String>> entry : statusMappings.asMap().entrySet()) {
            mappingsList.add(StatusMapping.forMapEntry(entry));
        }
        return mappingsList.toArray(new StatusMapping[mappingsList.size()]);
    }

    private void setStatusMappingArray(StatusMapping[] statusMappings) {
        for (StatusMapping statusMapping : statusMappings) {
            this.statusMappings.putAll(
                    statusMapping.getBuildStatus(),
                    statusMapping.getTexts());
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedReaderElement that = (FeedReaderElement) o;

        if (feeds != null ? !feeds.equals(that.feeds) : that.feeds != null) return false;
        if (logins != null ? !logins.equals(that.logins) : that.logins != null) return false;
        if (statusMappings != null ? !statusMappings.equals(that.statusMappings) : that.statusMappings != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = logins != null ? logins.hashCode() : 0;
        result = 31 * result + (feeds != null ? feeds.hashCode() : 0);
        result = 31 * result + (statusMappings != null ? statusMappings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FeedReader{" +
                "logins=" + logins +
                ", feeds=" + feeds +
                ", statusMappings=" + statusMappings +
                '}';
    }
}
