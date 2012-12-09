package com.cibot.config;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author Uefix
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LabelPropertiesElement {


    @XmlAttribute
    private int width = 250;

    @XmlAttribute
    private int height = 30;

    @XmlAttribute
    private int fontSize = 14;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelPropertiesElement that = (LabelPropertiesElement) o;

        if (fontSize != that.fontSize) return false;
        if (height != that.height) return false;
        if (width != that.width) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + fontSize;
        return result;
    }


    @Override
    public String toString() {
        return "LabelPropertiesElement{" +
                "width=" + width +
                ", height=" + height +
                ", fontSize=" + fontSize +
                '}';
    }
}
