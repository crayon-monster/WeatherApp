package com.example.weatherapp.network;

import com.example.weatherapp.pojo.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<CurrentWeather> getCurrentWeather(
            @Query("q") String q,
            @Query("appid") String appid);

}
