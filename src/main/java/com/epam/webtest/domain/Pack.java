package com.epam.webtest.domain;

import java.util.List;

public class Pack extends BaseEntity {
    String location;
    String username;
    private String name;
    private List<Document> documents;

    public Pack(String name, String location, String username) {
        this.name = name;
        this.location = location;
        this.username = username;
    }

    public Pack() {
    }

    public Pack(String name, List<Document> documents) {
        this.name = name;
        this.documents = documents;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
