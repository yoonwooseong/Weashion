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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity {

    TextView txt_city, txt_condition, txt_temp, txt_p_cloud, txt_p_humidity, txt_p_windSpeed;
    TextView txt_uvi, txt_wind_deg, txt_rise, txt_set, txt_morn, txt_day, txt_eve, txt_night;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;

    String temp= null;
    String weatherMain = null;//배경선택
    /*String timezone = null;
    String weatherDes= null;
    String humidity= null;
    String clouds= null;
    String windSpeed= null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*날씨 정보*/
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

        /*날씨 상세 정보 ID*/
        txt_uvi = findViewById(R.id.txt_uvi);
        txt_wind_deg = findViewById(R.id.txt_wind_deg);
        txt_rise = findViewById(R.id.txt_rise);
        txt_set = findViewById(R.id.txt_set);
        txt_morn = findViewById(R.id.txt_morn);
        txt_day = findViewById(R.id.txt_day);
        txt_eve = findViewById(R.id.txt_eve);
        txt_night = findViewById(R.id.txt_night);

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

        /*날씨 기본 정보*/
        String timezone, weatherDes, humidity, clouds, windSpeed;
        /*날씨 상세 정보*/
        String sunrize, sunset, uvi, wind_deg, morn, day, eve, night;

        try {
            JSONObject jObject = new JSONObject(resultJson);
            JSONObject currentObject = jObject.getJSONObject("current");
            timezone = jObject.getString("timezone");

            sunrize = String.valueOf(currentObject.getInt("sunrise"));
            sunset = String.valueOf(currentObject.getInt("sunset"));
            temp = String.valueOf(currentObject.getInt("temp"));
            humidity = String.valueOf(currentObject.getInt("humidity"));
            uvi = String.valueOf(currentObject.getInt("uvi"));
            clouds = String.valueOf(currentObject.getInt("clouds"));
            windSpeed = String.valueOf(currentObject.getDouble("wind_speed"));
            wind_deg = String.valueOf(currentObject.getDouble("wind_deg"));

            JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(0);
            weatherMain = weatherCondition.getString("main");
            weatherDes = weatherCondition.getString("description");

            JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(0);/*get(0)은 오늘날짜 +1할수록 다음날*/
            JSONObject dailyTempObject = dailyObject.getJSONObject("temp");
            morn = String.valueOf(dailyTempObject.getInt("morn"));
            day = String.valueOf(dailyTempObject.getInt("day"));
            eve = String.valueOf(dailyTempObject.getInt("eve"));
            night = String.valueOf(dailyTempObject.getInt("night"));
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

            /*상세 정보 출력*/
            txt_rise.setText("일출\n"+getTimestampToDate(sunrize));
            txt_set.setText("일몰\n"+getTimestampToDate(sunset));
            txt_uvi.setText("자외선지수\n"+uvi);
            txt_wind_deg.setText("풍향\n"+wind_deg+"°");
            txt_morn.setText("아침\n"+morn+"℃");
            txt_day.setText("낮\n"+day+"℃");
            txt_eve.setText("저녁\n"+eve+"℃");
            txt_night.setText("밤\n"+night+"℃");

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

    //유닉스 시간을 우리가 보는 시간으로 변경하는 메서드
    private String getTimestampToDate(String timestampStr){
        long timestamp = Long.parseLong(timestampStr);
        Date date = new Date(timestamp*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}