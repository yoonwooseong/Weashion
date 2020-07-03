package com.weather.weashion;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadWeatherTask extends AsyncTask<Void, Void, String> {

    String str, receiveMsg;
    @Override
    protected String doInBackground(Void... voids) {
        URL url;
        String urlStr = "https://api.openweathermap.org/data/2.5/onecall?lat="+Util.LAT+"&lon="+Util.LON+"&appid="+Util.APPID;
        try {
            url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            Log.i("MY", urlStr);
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("MY", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

    }
}
