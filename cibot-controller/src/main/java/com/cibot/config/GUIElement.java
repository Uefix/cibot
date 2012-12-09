package com.cibot.config;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author Uefix
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GUIElement {

    @XmlElement
    private LabelPropertiesElement labelProperties = new LabelPropertiesElement();


    public LabelPropertiesElement getLabelProperties() {
        return labelProperties;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GUIElement that = (GUIElement) o;

        if (labelProperties != null ? !labelProperties.equals(that.labelProperties) : that.labelProperties != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return labelProperties != null ? labelProperties.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GUIElement{" +
                "labelProperties=" + labelProperties +
                '}';
    }
}
