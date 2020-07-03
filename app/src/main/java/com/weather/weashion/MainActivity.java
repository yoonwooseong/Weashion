package com.weather.weashion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadWeatherTask().execute();

    }//onCreate()



}