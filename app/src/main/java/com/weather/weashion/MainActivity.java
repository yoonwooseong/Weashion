package com.weather.weashion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends Activity {

    TextView txt_city, txt_condition, txt_temp, txt_p_cloud, txt_p_humidity, txt_p_windSpeed;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;

    String timezone = null;
    String weatherDes= null;
    String temp= null;
    String humidity= null;
    String clouds= null;
    String windSpeed= null;
    String weatherMain = null;//배경선택

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
        btn_current = findViewById(R.id.btn_current);
        btn_details = findViewById(R.id.btn_details);
        box_current = findViewById(R.id.box_current);
        box_details = findViewById(R.id.box_details);
        largestBox = findViewById(R.id.largestBox);

        new LoadWeatherTask(this).execute();

        btn_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_current.setBackgroundResource(R.drawable.background_radius3);
                btn_details.setBackgroundResource(R.drawable.background_radius2);
                box_current.setVisibility(View.VISIBLE);
                box_details.setVisibility(View.GONE);
            }
        });

        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_current.setBackgroundResource(R.drawable.background_radius2);
                btn_details.setBackgroundResource(R.drawable.background_radius3);
                box_current.setVisibility(View.GONE);
                box_details.setVisibility(View.VISIBLE);
            }
        });

    }//onCreate()

    //JSON 정보를 PASING 하는 메서드
    public void currentWeatherParser(String resultJson){

        try {
            JSONObject jObject = new JSONObject(resultJson);
            JSONObject currentObject = jObject.getJSONObject("current");
            timezone = jObject.getString("timezone");
            temp = String.valueOf(currentObject.getInt("temp"));
            humidity = String.valueOf(currentObject.getInt("humidity"));
            clouds = String.valueOf(currentObject.getInt("clouds"));
            windSpeed = String.valueOf(currentObject.getDouble("wind_speed"));

            JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(0);
            weatherDes = weatherCondition.getString("description");
            weatherMain = weatherCondition.getString("main");
            /*Log.i("MY", "여기"+currentObject);*/

            choiceBackground(weatherMain);

            txt_city.setText(timezone);
            txt_condition.setText(weatherDes);
            txt_temp.setText(temp+"℃");
            txt_p_humidity.setText("습도 : "+humidity+"%");
            txt_p_cloud.setText("흐림 : "+clouds+"%");
            txt_p_windSpeed.setText("풍속 : "+windSpeed+"m/s");

            /*정보 저장(날씨, 온도) - 추후 네이버 api 조건에 활용*/
            SharedPreferences pref = getSharedPreferences("SHARE", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("weather", weatherMain);
            edit.putString("temp", temp);
            edit.commit();

            //저장된 값 불러올때 : pref.getString("weather", "")

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//currentWeatherParser()

    /*날씨에 따른 배경화면 선택 메서드*/
    public void choiceBackground(String weather){
        switch (weatherMain) {
            case "Clouds":
                largestBox.setBackgroundResource(R.drawable.cloud);
                break;
            case "Clear":
                largestBox.setBackgroundResource(R.drawable.clear);
                break;
            case "Rain": case "Drizzle":
                largestBox.setBackgroundResource(R.drawable.rain);
                break;
            case "Thunderstorm":
                largestBox.setBackgroundResource(R.drawable.thunderstorm);
                break;
            case "Snow":
                largestBox.setBackgroundResource(R.drawable.snow);
                break;
            default:
                largestBox.setBackgroundResource(R.drawable.mist);
                break;
        }
    }//choiceBackground()

}