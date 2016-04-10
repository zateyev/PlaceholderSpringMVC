package com.epam.webtest.domain;

public class User {
    private String email;
    private String password;
    private short isEnabled;

    public User() {
    }

    public User(String email, String password, short isEnabled) {
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short isEnabled() {
        return isEnabled;
    }

    public void setEnabled(short enabled) {
        isEnabled = enabled;
    }
}
