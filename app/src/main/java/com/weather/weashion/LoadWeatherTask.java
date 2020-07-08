package com.weather.weashion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadWeatherTask extends AsyncTask<String, Void, String> {

    private Context context;
    String str, receiveMsg;
    String v1, v2;

    public LoadWeatherTask(Context context, String v1, String v2) {
        this.context = context;
        this.v1 = v1;
        this.v2 = v2;
    }


    @Override
    protected String doInBackground(String... Strings) {
        URL url;
        String urlStr = "https://api.openweathermap.org/data/2.5/onecall?units=metric&lat="+v1+"&lon="+v2+"&appid="+Util.APPID;
        try {
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("MY", "가져오는 날씨 데이터들: "+receiveMsg);

                reader.close();
                tmp.close();

            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }

    @Override
    protected void onPostExecute(String weather) {
        ((MainActivity)context).currentWeatherParser(weather);
    }

}
