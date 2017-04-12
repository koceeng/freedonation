package com.koceeng.freedonation.base;

import com.google.firebase.database.Exclude;

public class FirebaseObject {

    private String key;

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
