package com.example.cognitivekidshometraining.Model;

public class Child {
    public String uid;
    public String name;
    public String disorder;

    public Child() {} // Needed for Firebase

    public Child(String uid, String name, String disorder) {
        this.uid = uid;
        this.name = name;
        this.disorder = disorder;
    }
}
