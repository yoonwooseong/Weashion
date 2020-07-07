package com.weather.weashion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherActivity extends Activity {

    TextView txt_city, txt_condition, txt_temp, txt_p_cloud, txt_p_humidity;
    TextView txt_uvi, txt_morn, txt_day, txt_eve, txt_night;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;

    String timezone = null;
    String temp= null;
    String weatherMain = null;//배경선택
    int selectDate = Util.TODAY;

    /*도시, 날씨 번역*/
    String cityKR, weatherKR;

    /*장소 Dialog*/
    Dialog placeDialog;
    /*날짜 Dialog*/
    AlertDialog.Builder dateDialog;
    final CharSequence[] inDayItems = {"오늘","내일","모래"};
    String loadedJson;

    /*GPS main*/
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(WeatherActivity.this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);

        /*확인 코드*/
        Toast.makeText(WeatherActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();

        /*여기부터 원래 코드*/
        new LoadWeatherTask(this, lat, lon).execute();

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
        btn_details.setOnClickListener( click );
        txt_city.setOnClickListener( click );
        txt_condition.setOnClickListener( click );

    }//onCreate()

    public void changeText(int i){
        switch (i){
            case Util.TODAY:
                btn_current.setText("현재 날씨");
                break;
            case Util.TOMORROW:
                btn_current.setText("내일 평균 날씨");
                break;
            case Util.AFTERTOMORROW:
                btn_current.setText("모래 평균 날씨");
                break;
        }
    }

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

            if( selectDate == Util.TODAY ){
                JSONObject currentObject = jObject.getJSONObject("current");
                temp = String.valueOf(currentObject.getInt("temp"));

                JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(selectDate);
                weatherMain = weatherCondition.getString("main");
            } else {
                JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(selectDate);
                JSONObject tempAvg = dailyObject.getJSONObject("temp");
                temp = String.valueOf(tempAvg.getInt("day"));

                JSONObject weatherCondition = (JSONObject) dailyObject.getJSONArray("weather").get(Util.TODAY);
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

            /*현재 날씨 정보 출력*/
            txt_city.setText(cityNameKR(splitTimezone[1]));
            txt_condition.setText(weatherKR);
            txt_temp.setText(temp+"℃");
            txt_p_humidity.setText("습도 : "+humidity+"%");
            txt_p_cloud.setText("흐림 : "+clouds+"%");

            /*상세 정보 출력*/
            txt_uvi.setText("자외선\n\t\t"+uvi);
            txt_morn.setText("아침\n"+morn+"℃");
            txt_day.setText("낮\n"+day+"℃");
            txt_eve.setText("저녁\n"+eve+"℃");
            txt_night.setText("밤\n"+night+"℃");

            /*정보 저장(날씨, 온도) - 추후 네이버 api 조건에 활용*/
            SharedPreferences pref = getSharedPreferences("SHARE", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("weather", weatherMain);
            edit.putString("temp", temp);
            edit.commit();

            /*다시 Pasing해주기 위해 Json저장*/
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
                    if(box_current.getVisibility() == View.VISIBLE){
                        dateDialog = new AlertDialog.Builder(WeatherActivity.this);
                        dateDialog.setTitle("날짜 선택").setItems(inDayItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectDate = i;
                                changeText(i);
                                currentWeatherParser(loadedJson);
                            }
                        });
                        dateDialog.setCancelable(false);
                        dateDialog.create();
                        dateDialog.show();
                    } else {
                        btn_current.setBackgroundResource(R.drawable.background_radius3);
                        btn_details.setBackgroundResource(R.drawable.background_radius2);
                        box_current.setVisibility(View.VISIBLE);
                        box_details.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btn_details:
                    btn_current.setBackgroundResource(R.drawable.background_radius2);
                    btn_details.setBackgroundResource(R.drawable.background_radius3);
                    box_current.setVisibility(View.GONE);
                    box_details.setVisibility(View.VISIBLE);
                    break;

                case R.id.txt_city:
                    placeDialog = new Dialog(WeatherActivity.this);
                    placeDialog.setContentView(R.layout.select_place_dialog);
                    placeDialog.show();
                    break;
            }
        }
    };

    /*여기부터 추가*/
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(WeatherActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(WeatherActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(WeatherActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(WeatherActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(WeatherActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}