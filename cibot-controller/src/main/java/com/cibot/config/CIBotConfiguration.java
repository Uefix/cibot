package com.cibot.config;

import com.cibot.cimodel.BuildStatus;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Uefix
 */
@XmlRootElement(name = "cibotConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class CIBotConfiguration {

    @XmlElement
    private Thumbi thumbi = new Thumbi();

    @XmlElement
    private FeedReader feedReader = new FeedReader();


    public Thumbi getThumbi() {
        return thumbi;
    }


    public FeedReader getFeedReader() {
        return feedReader;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Thumbi {

        @XmlElement
        private String connectionUri;

        @XmlElement
        private long timeout;


        public String getConnectionUri() {
            return connectionUri;
        }

        public void setConnectionUri(String connectionUri) {
            this.connectionUri = connectionUri;
        }


        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Thumbi that = (Thumbi) o;

            if (timeout != that.timeout) return false;
            if (connectionUri != null ? !connectionUri.equals(that.connectionUri) : that.connectionUri != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = connectionUri != null ? connectionUri.hashCode() : 0;
            result = 31 * result + (int) (timeout ^ (timeout >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "Thumbi{" +
                    "connectionUri='" + connectionUri + '\'' +
                    ", timeout=" + timeout +
                    '}';
        }
    }



    @XmlRootElement
    public static class FeedReader {


        private List<String> feedUrls = Lists.newArrayList();

        /**
         * http://www.mail-archive.com/cxf-user@incubator.apache.org/msg04723.html
         * http://www.java-forum.org/xml-co/116454-jaxb-hashmap.html
         */
        private ArrayListMultimap<BuildStatus,String> statusMappings = ArrayListMultimap.create();

        @XmlElementWrapper(name = "feeds")
        @XmlElement(name = "feed")
        public List<String> getFeedUrls() {
            return feedUrls;
        }

        public void setFeedUrls(List<String> feedUrls) {
            this.feedUrls = feedUrls;
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

            FeedReader that = (FeedReader) o;

            if (feedUrls != null ? !feedUrls.equals(that.feedUrls) : that.feedUrls != null) return false;
            if (statusMappings != null ? !statusMappings.equals(that.statusMappings) : that.statusMappings != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = feedUrls != null ? feedUrls.hashCode() : 0;
            result = 31 * result + (statusMappings != null ? statusMappings.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "FeedReader{" +
                    "feedUrls=" + feedUrls +
                    ", statusMappings=" + statusMappings +
                    '}';
        }
    }



    public static class StatusMapping {


        private BuildStatus buildStatus;

        private ArrayList<String> texts = Lists.newArrayList();


        public static StatusMapping forMapEntry(Map.Entry<BuildStatus, Collection<String>> entry) {
            StatusMapping result = new StatusMapping();
            result.setBuildStatus(entry.getKey());
            result.getTexts().addAll(entry.getValue());
            return result;
        }


        @XmlAttribute(name = "status")
        public BuildStatus getBuildStatus() {
            return buildStatus;
        }

        public void setBuildStatus(BuildStatus buildStatus) {
            this.buildStatus = buildStatus;
        }

        @XmlElement(name = "text")
        public ArrayList<String> getTexts() {
            return texts;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StatusMapping that = (StatusMapping) o;

            if (buildStatus != null ? !buildStatus.equals(that.buildStatus) : that.buildStatus != null) return false;
            if (texts != null ? !texts.equals(that.texts) : that.texts != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = buildStatus != null ? buildStatus.hashCode() : 0;
            result = 31 * result + (texts != null ? texts.hashCode() : 0);
            return result;
        }
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CIBotConfiguration that = (CIBotConfiguration) o;

        if (feedReader != null ? !feedReader.equals(that.feedReader) : that.feedReader != null) return false;
        if (thumbi != null ? !thumbi.equals(that.thumbi) : that.thumbi != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = thumbi != null ? thumbi.hashCode() : 0;
        result = 31 * result + (feedReader != null ? feedReader.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CIBotConfiguration{" +
                "thumbi=" + thumbi +
                ", feedReader=" + feedReader +
                '}';
    }
}