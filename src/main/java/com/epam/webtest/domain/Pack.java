package com.epam.webtest.domain;

import java.util.List;

public class Pack extends BaseEntity {
    private String name;
    private List<Document> documents;
    String location;
    User user;

    public Pack() {
    }

    public Pack(String name, List<Document> documents) {
        this.name = name;
        this.documents = documents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
