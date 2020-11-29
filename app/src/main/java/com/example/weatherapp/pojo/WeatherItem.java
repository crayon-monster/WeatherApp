package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class WeatherItem {

    @Json(name = "icon")
    private String icon;

    @Json(name = "description")
    private String description;

    @Json(name = "main")
    private String main;

    @Json(name = "id")
    private int id;

    public WeatherItem(String icon, String description, String main, int id) {
        this.icon = icon;
        this.description = description;
        this.main = main;
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }

    public int getId() {
        return id;
    }
}