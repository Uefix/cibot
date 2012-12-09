package com.cibot.config;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Uefix
 */
@XmlRootElement(name = "cibotConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class CIBotConfiguration {


    @XmlElement
    private ThumbiElement thumbi = new ThumbiElement();

    @XmlElement
    private FeedReaderElement feedReader = new FeedReaderElement();

    @XmlElement
    private GUIElement gui = new GUIElement();


    public LoginElement getLogin(FeedElement feed) {
        Preconditions.checkArgument(feed != null);
        Preconditions.checkArgument(feed.hasLogin());

        return feedReader.getLogins().get(feed.getLogin());
    }


    public ThumbiElement getThumbi() {
        return thumbi;
    }


    public FeedReaderElement getFeedReader() {
        return feedReader;
    }

    public GUIElement getGui() {
        return gui;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CIBotConfiguration that = (CIBotConfiguration) o;

        if (feedReader != null ? !feedReader.equals(that.feedReader) : that.feedReader != null) return false;
        if (gui != null ? !gui.equals(that.gui) : that.gui != null) return false;
        if (thumbi != null ? !thumbi.equals(that.thumbi) : that.thumbi != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = thumbi != null ? thumbi.hashCode() : 0;
        result = 31 * result + (feedReader != null ? feedReader.hashCode() : 0);
        result = 31 * result + (gui != null ? gui.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CIBotConfiguration{" +
                "thumbi=" + thumbi +
                ", feedReader=" + feedReader +
                ", gui=" + gui +
                '}';
    }
}
