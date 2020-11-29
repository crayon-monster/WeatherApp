package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.network.RetrofitClientInstance;
import com.example.weatherapp.network.WeatherService;
import com.example.weatherapp.pojo.CurrentWeather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.weatherapp.network.Constants.API_KEY;

public class MainActivity extends AppCompatActivity {

    TextView date, time, location, temperature, weekDay;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        date = findViewById(R.id.date_textView);
        time = findViewById(R.id.time_textView);
        location = findViewById(R.id.location_textView);
        temperature = findViewById(R.id.tempCelsius_textView);
        weekDay = findViewById(R.id.weekDay_textView);
        weatherIcon = findViewById(R.id.weather_icon_imageView);

        // Weather Setup
        getCurrentWeather();

        // Time Setup
        setRepeatingTimeTask();

        //TODO Change BG and Icon

        //TODO get city location
    }

    private void getCurrentWeather() {
        WeatherService weatherService = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<CurrentWeather> call = weatherService.getCurrentWeather("Erzurum", API_KEY);

        call.enqueue(new Callback<CurrentWeather>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                Log.d("myTag", "onResponse: " + response);

                if(response.isSuccessful() && response.body() != null) {
                    CurrentWeather currentWeather = response.body();
                    temperature.setText((int) (currentWeather.getMain().getTemp() - 273.15) + "°С");
                    location.setText(currentWeather.getName());

                    // Setting icon
                    weatherIcon.setImageResource(
                            CurrentWeatherIconSelector
                                    .getWeatherIconResId(currentWeather.getWeatherItems().get(0).getId()));

                    // Setting background

                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Log.d("myTag", "onResponse: " + t);
            }
        });
    }

    private void setRepeatingTimeTask() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getCurrentDate());
            }
        };

        timer.schedule(task, 0L, 500);
    }

    public void getCurrentDate() {
        SimpleDateFormat mmmm_dd = new SimpleDateFormat("MMMM dd");
        SimpleDateFormat hh_mm = new SimpleDateFormat("K:mma");
        SimpleDateFormat eeee = new SimpleDateFormat("EEEE");

        Date currentDate = Calendar.getInstance().getTime();

        String monthDay = mmmm_dd.format(currentDate);
        String hourMinute = hh_mm.format(currentDate);
        String dayWeek = eeee.format(currentDate);

        date.setText(monthDay);
        time.setText(hourMinute);
        weekDay.setText(dayWeek);

        Log.d("myTag", "Time set");
    }
}