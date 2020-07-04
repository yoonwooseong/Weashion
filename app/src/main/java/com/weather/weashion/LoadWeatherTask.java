package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadWeatherTask extends AsyncTask<Void, Void, String> {

    private Context context;
    String str, receiveMsg;

    public LoadWeatherTask(Context context){
        this.context = context;
    }


    @Override
    protected String doInBackground(Void... voids) {
        URL url;
        String urlStr = "https://api.openweathermap.org/data/2.5/onecall?units=metric&lat="+Util.LAT+"&lon="+Util.LON+"&appid="+Util.APPID;
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
    protected void onPostExecute(String s) {
        ((MainActivity)context).currentWeatherParser(s);
    }


}
