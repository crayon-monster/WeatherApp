package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

import java.util.List;

public class CurrentWeather {

    @Json(name = "visibility")
    private int visibility;

    @Json(name = "timezone")
    private int timezone;

    @Json(name = "main")
    private Main main;

    @Json(name = "clouds")
    private Clouds clouds;

    @Json(name = "sys")
    private Sys sys;

    @Json(name = "dt")
    private int dt;

    @Json(name = "coord")
    private Coord coord;

    @Json(name = "weather")
    private List<WeatherItem> weatherItems;

    @Json(name = "name")
    private String name;

    @Json(name = "cod")
    private int cod;

    @Json(name = "id")
    private int id;

    @Json(name = "base")
    private String base;

    @Json(name = "wind")
    private Wind wind;

    public CurrentWeather(int visibility, int timezone, Main main, Clouds clouds, Sys sys, int dt, Coord coord, List<WeatherItem> weatherItems, String name, int cod, int id, String base, Wind wind) {
        this.visibility = visibility;
        this.timezone = timezone;
        this.main = main;
        this.clouds = clouds;
        this.sys = sys;
        this.dt = dt;
        this.coord = coord;
        this.weatherItems = weatherItems;
        this.name = name;
        this.cod = cod;
        this.id = id;
        this.base = base;
        this.wind = wind;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getTimezone() {
        return timezone;
    }

    public Main getMain() {
        return main;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Sys getSys() {
        return sys;
    }

    public int getDt() {
        return dt;
    }

    public Coord getCoord() {
        return coord;
    }

    public List<WeatherItem> getWeatherItems() {
        return weatherItems;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    public int getId() {
        return id;
    }

    public String getBase() {
        return base;
    }

    public Wind getWind() {
        return wind;
    }
}