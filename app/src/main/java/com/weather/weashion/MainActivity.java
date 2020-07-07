package com.weather.weashion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    TextView txt_city, txt_condition, txt_temp, txt_p_cloud, txt_p_humidity;
    TextView txt_uvi, txt_morn, txt_day, txt_eve, txt_night;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;

    String timezone = null;
    String temp= null;
    String weatherMain = null;//배경선택

    int touchDelay = 2;
    int selectDate = 0;

    /*도시, 날씨 번역*/
    String cityKR, weatherKR;

    AlertDialog.Builder dialog;
    final CharSequence[] inDayItems = {"오늘","내일","모래","글피"};
    String loadedJson;

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
        btn_current = findViewById(R.id.btn_current);
        btn_details = findViewById(R.id.btn_details);
        box_current = findViewById(R.id.box_current);
        box_details = findViewById(R.id.box_details);
        largestBox = findViewById(R.id.largestBox);

        /*날씨 상세 정보 ID*/
        txt_uvi = findViewById(R.id.txt_uvi);
        txt_morn = findViewById(R.id.txt_morn);
        txt_day = findViewById(R.id.txt_day);
        txt_eve = findViewById(R.id.txt_eve);
        txt_night = findViewById(R.id.txt_night);

        /*클릭 이벤트 추가*/
        btn_current.setOnClickListener( click );
        btn_current.setOnLongClickListener( longClick );
        btn_details.setOnClickListener( click );
        txt_city.setOnClickListener( click );
        txt_condition.setOnClickListener( click );

        new LoadWeatherTask(this).execute();


    }//onCreate()

    //JSON 정보를 PASING 하는 메서드
    public void currentWeatherParser(String resultJson){

        /*날씨 기본 정보*/
        String weatherDes, humidity, clouds;
        /*날씨 상세 정보*/
        String uvi, morn, day, eve, night;

        try {
            JSONObject jObject = new JSONObject(resultJson);
            timezone = jObject.getString("timezone");
            String[] splitTimezone = timezone.split("/");

            if( selectDate == 0 ){
                JSONObject currentObject = jObject.getJSONObject("current");
                temp = String.valueOf(currentObject.getInt("temp"));

                JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(selectDate);
                weatherMain = weatherCondition.getString("main");
            } else {
                JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(selectDate);
                JSONObject tempAvg = dailyObject.getJSONObject("temp");
                temp = String.valueOf(tempAvg.getInt("day"));

                JSONObject weatherCondition = (JSONObject) dailyObject.getJSONArray("weather").get(0);
                weatherMain = weatherCondition.getString("main");
            }

            JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(selectDate);/*get(0)은 오늘날짜 +1할수록 다음날*/
            JSONObject dailyTempObject = dailyObject.getJSONObject("temp");
            morn = String.valueOf(dailyTempObject.getInt("morn"));
            day = String.valueOf(dailyTempObject.getInt("day"));
            eve = String.valueOf(dailyTempObject.getInt("eve"));
            night = String.valueOf(dailyTempObject.getInt("night"));
            humidity = String.valueOf(dailyObject.getInt("humidity"));
            clouds = String.valueOf(dailyObject.getInt("clouds"));
            uvi = String.valueOf(dailyObject.getInt("uvi"));


            choiceBackground(weatherMain);

            txt_city.setText(cityNameKR(splitTimezone[1]));
            txt_condition.setText(weatherKR);
            txt_temp.setText(temp+"℃");
            txt_p_humidity.setText("습도 : "+humidity+"%");
            txt_p_cloud.setText("흐림 : "+clouds+"%");

            /*정보 저장(날씨, 온도) - 추후 네이버 api 조건에 활용*/
            SharedPreferences pref = getSharedPreferences("SHARE", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("weather", weatherMain);
            edit.putString("temp", temp);
            edit.commit();
            //저장된 값 불러올때 : pref.getString("weather", "")

            /*상세 정보 출력*/
            txt_uvi.setText("자외선\n\t\t"+uvi);
            txt_morn.setText("아침\n"+morn+"℃");
            txt_day.setText("낮\n"+day+"℃");
            txt_eve.setText("저녁\n"+eve+"℃");
            txt_night.setText("밤\n"+night+"℃");

            loadedJson = resultJson;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//currentWeatherParser()

    /*날씨에 따른 배경화면 선택 메서드*/
    public void choiceBackground(String weather){
        switch (weatherMain) {
            case "Clouds":
                largestBox.setBackgroundResource(R.drawable.cloud);
                weatherKR = "구름";
                break;
            case "Clear":
                largestBox.setBackgroundResource(R.drawable.clear);
                weatherKR = "맑음";
                break;
            case "Rain": case "Drizzle":
                largestBox.setBackgroundResource(R.drawable.rain);
                weatherKR = "비";
                break;
            case "Thunderstorm":
                largestBox.setBackgroundResource(R.drawable.thunderstorm);
                weatherKR = "뇌우";
                break;
            case "Snow":
                largestBox.setBackgroundResource(R.drawable.snow);
                weatherKR = "눈";
                break;
            default:
                largestBox.setBackgroundResource(R.drawable.mist);
                weatherKR = "안개";
                break;
        }
    }//choiceBackground()

    /*날씨에 따른 배경화면 선택 메서드*/
    public String cityNameKR(String city){
        switch (city) {
            case "Seoul":
                return "서울";

            case "Busan":
                return "부산";

            default:
                return city;
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

    /*날씨, 도시 이벤트 추가*/
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_current:
                    btn_current.setBackgroundResource(R.drawable.background_radius3);
                    btn_details.setBackgroundResource(R.drawable.background_radius2);
                    box_current.setVisibility(View.VISIBLE);
                    box_details.setVisibility(View.GONE);
                    Log.i("MY","안되나?");
                    break;

                case R.id.btn_details:
                    btn_current.setBackgroundResource(R.drawable.background_radius2);
                    btn_details.setBackgroundResource(R.drawable.background_radius3);
                    box_current.setVisibility(View.GONE);
                    box_details.setVisibility(View.VISIBLE);
                    break;

                case R.id.txt_city:
                    final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                0 );
                    }
                    else{
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        String provider = location.getProvider();
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        double altitude = location.getAltitude();

                        Log.i("MY","위치정보 : " + provider + "\n" +
                                "위도 : " + longitude + "\n" +
                                "경도 : " + latitude + "\n" +
                                "고도  : " + altitude);

                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                    }
                    final LocationListener gpsLocationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {

                            String provider = location.getProvider();
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();
                            double altitude = location.getAltitude();

                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onProviderDisabled(String provider) {
                        }
                    };
                    break;


                case R.id.txt_condition:
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("날짜 선택").setItems(inDayItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectDate = i;
                            currentWeatherParser(loadedJson);
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create();
                    dialog.show();
                    break;
            }
        }
    };

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Log.i("MY","되나?");
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("날짜 선택").setItems(inDayItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectDate = i;
                    currentWeatherParser(loadedJson);
                }
            });
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();

            return true;
        }
    };

}