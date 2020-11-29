package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class Coord {

    @Json(name = "lon")
    private double lon;

    @Json(name = "lat")
    private double lat;

    public Coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}