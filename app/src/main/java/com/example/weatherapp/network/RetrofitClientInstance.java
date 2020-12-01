package com.example.weatherapp.network;

import com.example.weatherapp.Constants;
import com.squareup.moshi.Moshi;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(
                            new Moshi.Builder().build()
                            )
                    )
                    .baseUrl(Constants.BASE_URL)
                    .build();
        }
        return retrofit;

    }

}
