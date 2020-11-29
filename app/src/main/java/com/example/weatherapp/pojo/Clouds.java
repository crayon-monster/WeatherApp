package com.example.weatherapp.pojo;

import com.squareup.moshi.Json;

public class Clouds{

	@Json(name = "all")
	private int all;

    public Clouds(int all) {
        this.all = all;
    }

    public int getAll(){
		return all;
	}
}