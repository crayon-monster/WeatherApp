package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class Wind {

    @Json(name = "deg")
    private int deg;

    @Json(name = "speed")
    private double speed;

    public Wind(int deg, double speed) {
        this.deg = deg;
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public double getSpeed() {
        return speed;
    }
}