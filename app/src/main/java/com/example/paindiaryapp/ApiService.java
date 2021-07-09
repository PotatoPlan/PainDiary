package com.example.paindiaryapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("weather")
    Call<DataWeather> getDataWeather(@Query("q") String name,
                                     @Query("units") String units,
                                     @Query("appid") String apiKey);

}
