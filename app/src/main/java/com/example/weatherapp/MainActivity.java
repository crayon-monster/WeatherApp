package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.weatherapp.network.RetrofitClientInstance;
import com.example.weatherapp.network.WeatherService;
import com.example.weatherapp.pojo.CurrentWeather;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.weatherapp.Constants.API_KEY;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //TODO Refresh Animation
    private GPSTrack gpsTrack;
    private TextView date, time, location, temperature, weekDay;
    private ImageView weatherIcon;
    private ImageButton refreshButton;
    private MKLoader mkLoader;

    // Location variables
    Double pLong;
    Double pLat;

    //Preferences
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        // Initialization
        date = findViewById(R.id.date_textView);
        time = findViewById(R.id.time_textView);
        location = findViewById(R.id.location_textView);
        temperature = findViewById(R.id.tempCelsius_textView);
        weekDay = findViewById(R.id.weekDay_textView);
        weatherIcon = findViewById(R.id.weather_icon_imageView);
        refreshButton = findViewById(R.id.refresh_button);
        mkLoader = findViewById(R.id.mkLoader);

        // Set listener
        setClickListener();

        setRepeatingTasks();

        // Preferences
        sharedPreferences = getSharedPreferences("weatherData", Context.MODE_PRIVATE);

        location.setText(sharedPreferences.getString("Location", "Not Found"));
        temperature.setText(sharedPreferences.getInt("Temps", 404) + "°С");
        View layout = findViewById(R.id.main_background);
        int imageId = CurrentWeatherIconSelector.getWeatherIconResId(sharedPreferences.getInt("ConditionID", R.drawable.ic_no_idea));
        int backgroundId = BackgroundSelector.getBgInt(sharedPreferences.getInt("ConditionID", R.drawable.no_weather));

        layout.setBackgroundResource(backgroundId);
        weatherIcon.setImageResource(imageId);
    }

    public void getLocation() {
        gpsTrack = new GPSTrack(MainActivity.this);
        if (gpsTrack.canGetLocation()) {
            pLat = gpsTrack.getLatitude();
            pLong = gpsTrack.getLongitude();
            Log.d("myLocation", "\nlon: " + pLong + "\nlat: " + pLat);
            if (pLong != 0.0 && pLat != 0.0) getCurrentWeather();
        }
        else {
            gpsTrack.showSettingsAlert();
            stopAnimation();
        }
    }

    private void setClickListener() {
        refreshButton.setOnClickListener(v -> {
            refreshButton.setVisibility(View.INVISIBLE);
            mkLoader.setVisibility(View.VISIBLE);
            Log.d("MYBUTTON", "Button appeared");

            getLocation();
        });
    }

    private void stopAnimation() {
        mkLoader.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        Log.d("MYBUTTON", "Button disappeared");
    }

    private void getCurrentWeather() {
        WeatherService weatherService = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<CurrentWeather> call = weatherService.getCurrentWeather(pLat, pLong, API_KEY);

        call.enqueue(new Callback<CurrentWeather>() {

            int currentTemp;
            int conditionID;
            int imageId;
            int backgroundId;
            String locationCity;
            ConstraintLayout layout;

            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {

                Log.d("myTag", "onResponse: " + response);

                if (response.isSuccessful() && response.body() != null) {
                    CurrentWeather currentWeather = response.body();

                    currentTemp = (int) (currentWeather.getMain().getTemp() - 273.15);
                    conditionID = currentWeather.getWeatherItems().get(0).getId();
                    locationCity = currentWeather.getName();
                    imageId = CurrentWeatherIconSelector.getWeatherIconResId(conditionID);
                    backgroundId = BackgroundSelector.getBgInt(conditionID);


                    temperature.setText(MessageFormat.format("{0}°С", currentTemp));
                    location.setText(locationCity);
                    weatherIcon.setImageResource(imageId);

                    // Setting background
                    layout = findViewById(R.id.main_background);
                    layout.setBackgroundResource(backgroundId);


                    saveToPreferences(currentTemp, conditionID, locationCity);
                    Toast.makeText(MainActivity.this, "Weather updated!", Toast.LENGTH_SHORT).show();

                    stopAnimation();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {
                Log.d("myTag", "onResponse: " + t);

                Toast.makeText(getApplicationContext(), "Please connect to the Internet", Toast.LENGTH_SHORT).show();

                stopAnimation();
            }
        });
    }

    private void saveToPreferences(int temp, int id, String location) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("Temps", temp);
        editor.putInt("ConditionID", id);
        editor.putString("Location", location);

        editor.apply();
    }

    private void setRepeatingTasks() {
        Timer timer = new Timer();
        TimerTask dateTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getCurrentDate());
            }
        };

        timer.schedule(dateTask, 0L, 500);

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

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        pLat = location.getLatitude();
        pLong = location.getLongitude();
        Log.d("alo", pLat + " " + pLong);
    }
}