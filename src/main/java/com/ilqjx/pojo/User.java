package com.ilqjx.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String salt;

    @Transient
    private String anonymousName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAnonymousName() {
        if (null != anonymousName) {
            return anonymousName;
        }
        if (null == name) {
            return null;
        }
        String anonymousName;
        int len = name.length();
        if (len <= 1) {
            anonymousName = "*";
        } else if (len == 2) {
            anonymousName = name.substring(0, 1) + "*";
        } else {
            char[] charArray = name.toCharArray();
            for (int i = 1; i < len - 1; i++) {
                charArray[i] = '*';
            }
            anonymousName = new String(charArray);
        }
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", anonymousName='" + anonymousName + '\'' +
                '}';
    }

}
