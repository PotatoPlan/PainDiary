package com.example.paindiaryapp;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    String main;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
