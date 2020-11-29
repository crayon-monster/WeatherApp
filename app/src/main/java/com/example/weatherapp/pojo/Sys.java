package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class Sys {

    @Json(name = "country")
    private String country;

    @Json(name = "sunrise")
    private int sunrise;

    @Json(name = "sunset")
    private int sunset;

    @Json(name = "id")
    private int id;

    @Json(name = "type")
    private int type;

    public Sys(String country, int sunrise, int sunset, int id, int type) {
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.id = id;
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}