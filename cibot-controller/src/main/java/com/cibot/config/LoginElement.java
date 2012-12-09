package com.cibot.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
* @author Uefix
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginElement {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String user;

    @XmlAttribute
    private String password;


    public LoginElement() {
    }

    public LoginElement(String id, String user, String password) {
        this.id = id;
        this.user = user;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginElement login = (LoginElement) o;

        if (id != null ? !id.equals(login.id) : login.id != null) return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;
        if (user != null ? !user.equals(login.user) : login.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
