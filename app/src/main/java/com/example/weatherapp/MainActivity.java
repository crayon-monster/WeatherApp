package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

    TextView date, time, location, temperature, weekDay;
    ImageView weatherIcon;
    ImageButton refreshButton;

    // Location variables
    Double pLong = 0.0;
    Double pLat = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.Theme_WeatherApp);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        // Set listener
        setClickListener();

        // Get location
        getLocation();

        Log.d("myLocation", "\nlon: " + pLong + "\nlat: " + pLat);

        // Set repeating tasks
        setRepeatingTasks();
    }
    private void setClickListener() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                getCurrentWeather();
                Toast.makeText(getApplicationContext(), "Weather updated", Toast.LENGTH_SHORT).show();
                Log.d("myLocation", "\nlon: " + pLong + "\nlat: " + pLat);
            }
        });
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(true);
        crta.setBearingRequired(true);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crta, true);
        Log.d("","provider : "+provider);
        // String provider = LocationManager.GPS_PROVIDER;
        MyLocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(provider);

        pLat = location.getLatitude();
        pLong = location.getLongitude();

    }

    private void getCurrentWeather() {
        WeatherService weatherService = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<CurrentWeather> call = weatherService.getCurrentWeather(pLat, pLong, API_KEY);

        call.enqueue(new Callback<CurrentWeather>() {

            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {
                Log.d("myTag", "onResponse: " + response);

                if (response.isSuccessful() && response.body() != null) {
                    CurrentWeather currentWeather = response.body();
                    temperature.setText(MessageFormat.format("{0}°С", (int) (currentWeather.getMain().getTemp() - 273.15)));
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
            public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {
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
                getCurrentWeather();
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        pLat = location.getLatitude();
        pLong = location.getLongitude();
        Log.d("alo", pLat + " " + pLong);
    }
}