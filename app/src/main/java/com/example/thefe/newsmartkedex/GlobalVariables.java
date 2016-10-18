package com.example.thefe.newsmartkedex;

import android.app.Application;

/**
 * Created by TheFe on 18/10/2016.
 */

public class GlobalVariables extends Application {
    private String language = "ITA";
    private String owner = "";
    private String smartkedex = "";

    //getters
    public String getLanguage() {
        return language;
    }
    public String getOwner() { return owner; }
    public String getSmartkedex() {return smartkedex; }

    //setters
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setOwner(String owner) { this.owner = owner; }
    public void setSmartkedex(String smartkedex) { this.smartkedex = smartkedex; }
}
