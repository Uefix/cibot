package com.cibot.config;

import com.cibot.cimodel.BuildStatus;
import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
* @author Uefix
*/
public class StatusMapping {


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
