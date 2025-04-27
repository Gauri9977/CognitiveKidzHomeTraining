package com.example.cognitivekidshometraining;
public class HelperClass {

    String username, email;

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HelperClass(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public HelperClass() {
    }
}