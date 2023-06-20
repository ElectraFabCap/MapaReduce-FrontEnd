package com.example.distributedsystems2023;

import android.app.Application;

public class GPXApplication extends Application {
    private String masterIP = "";
    private String username = "";

    public String getMasterIP() {
        return masterIP;
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = masterIP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
