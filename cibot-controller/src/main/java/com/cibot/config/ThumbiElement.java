package com.cibot.config;

import javax.xml.bind.annotation.*;

/**
* @author Uefix
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ThumbiElement {

    @XmlElement
    private String connectionUri;

    @XmlElement
    private long timeout;

    @XmlAttribute
    private boolean enabled = false;

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


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThumbiElement thumbi = (ThumbiElement) o;

        if (enabled != thumbi.enabled) return false;
        if (timeout != thumbi.timeout) return false;
        if (connectionUri != null ? !connectionUri.equals(thumbi.connectionUri) : thumbi.connectionUri != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = connectionUri != null ? connectionUri.hashCode() : 0;
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Thumbi{" +
                "connectionUri='" + connectionUri + '\'' +
                ", timeout=" + timeout +
                ", enabled=" + enabled +
                '}';
    }
}
