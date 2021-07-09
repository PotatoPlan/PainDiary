package com.example.paindiaryapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataWeather {

//  Data from Main Object
    @SerializedName("main")
    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }


//  Data from Weather Object (It is an JSONArray, so you'll need to parse it
    @SerializedName("weather")
    private List<Weather> weathers;

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }
}
