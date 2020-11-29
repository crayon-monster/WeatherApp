package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class Main {

    @Json(name = "temp")
    private double temp;

    @Json(name = "temp_min")
    private double tempMin;

    @Json(name = "humidity")
    private int humidity;

    @Json(name = "pressure")
    private int pressure;

    @Json(name = "feels_like")
    private double feelsLike;

    @Json(name = "temp_max")
    private double tempMax;

    public Main(double temp, double tempMin, int humidity, int pressure, double feelsLike, double tempMax) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.humidity = humidity;
        this.pressure = pressure;
        this.feelsLike = feelsLike;
        this.tempMax = tempMax;
    }

    public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getTempMax() {
        return tempMax;
    }
}