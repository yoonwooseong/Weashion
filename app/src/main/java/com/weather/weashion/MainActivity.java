package com.weather.weashion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String[] arrayWeather = new String[6];

    TextView txt_city, txt_condition, txt_temp, txt_p_cloud, txt_p_humidity, txt_p_windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_city = findViewById(R.id.txt_city);
        txt_condition = findViewById(R.id.txt_condition);
        txt_temp = findViewById(R.id.txt_temp);
        txt_p_cloud = findViewById(R.id.txt_p_cloud);
        txt_p_humidity = findViewById(R.id.txt_p_humidity);
        txt_p_windSpeed = findViewById(R.id.txt_p_windSpeed);

        new LoadWeatherTask(this).execute();

    }//onCreate()


    public String[] currentWeatherParser(String resultJson){

        String timezone = null;
        String weatherDes= null;
        String temp= null;
        String humidity= null;
        String clouds= null;
        String windSpeed= null;

        try {
            JSONObject jObject = new JSONObject(resultJson);
            JSONObject currentObject = jObject.getJSONObject("current");

            timezone = jObject.getString("timezone");

            temp = String.valueOf(currentObject.getInt("temp"));
            humidity = String.valueOf(currentObject.getInt("humidity"));
            clouds = String.valueOf(currentObject.getInt("clouds"));
            windSpeed = String.valueOf(currentObject.getDouble("wind_speed"));

            Log.i("MY", "여기"+currentObject);

            JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(0);
            weatherDes = weatherCondition.getString("description");
            /*Log.i("MY", "진짜"+weatherCondition.getString("description"));*/

            arrayWeather[0] = timezone;
            /*arrayWeather[1] = "null";
            arrayWeather[2] = temp;
            arrayWeather[3] = humidity;
            arrayWeather[4] = clouds;
            arrayWeather[5] = windSpeed;*/

            txt_city.setText(arrayWeather[0]);
            txt_condition.setText(weatherDes);
            txt_temp.setText(temp+"℃");
            txt_p_humidity.setText("습도 : "+humidity+"%");
            txt_p_cloud.setText("흐림 : "+clouds+"%");
            txt_p_windSpeed.setText("풍속 : "+windSpeed+"m/s");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayWeather;
    }

}