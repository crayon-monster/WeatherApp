package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Initialization
        date = findViewById(R.id.date_textView);
        time = findViewById(R.id.time_textView);
        location = findViewById(R.id.location_textView);
        temperature = findViewById(R.id.tempCelsius_textView);
        weekDay = findViewById(R.id.weekDay_textView);
        weatherIcon = findViewById(R.id.weather_icon_imageView);

        // Set repeating tasks
        setRepeatingTasks();

        //TODO Change BG and Icon

        //TODO get city location
    }

    private void getCurrentWeather() {
        WeatherService weatherService = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<CurrentWeather> call = weatherService.getCurrentWeather("Moscow", API_KEY);

        call.enqueue(new Callback<CurrentWeather>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                Log.d("myTag", "onResponse: " + response);

                if (response.isSuccessful() && response.body() != null) {
                    CurrentWeather currentWeather = response.body();
                    temperature.setText((int) (currentWeather.getMain().getTemp() - 273.15) + "°С");
                    location.setText(currentWeather.getName());

                    // Current weather ID
                    int weatherId = currentWeather.getWeatherItems().get(0).getId();
                    // Setting icon
                    weatherIcon.setImageResource(
                            CurrentWeatherIconSelector
                                    .getWeatherIconResId(weatherId));

                    // Setting background
                    ConstraintLayout layout = findViewById(R.id.main_background);
                    layout.setBackgroundResource(BackgroundSelector.getBgInt(weatherId));
                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Log.d("myTag", "onResponse: " + t);
            }
        });
    }

    private void setRepeatingTasks() {
        Timer timer = new Timer();
        TimerTask dateTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getCurrentDate());
            }
        };
        TimerTask weatherTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getCurrentWeather());
            }
        };

        timer.schedule(dateTask, 0L, 500);
        timer.schedule(weatherTask, 0L, 1000 * 60 * 60 * 6);


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