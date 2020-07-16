package com.weather.weashion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;


public class ModelAllActivity {
    private Context context;
    private Parsers parser;

    private String texthat;
    private String texttop1;
    private String texttop2;
    private String textbottom;
    private String textshoes;
    private int hatr;
    private int topr1;
    private int topr2;
    private int bottomr;
    private int shoesr;

    int start = 1;

    String[] image = new String[Util.SEARCH_COUNT2];

    public ModelAllActivity(Context context) {
        this.context = context;

        hatr = new Random().nextInt(4);
        topr1 = new Random().nextInt(3);
        topr2 = new Random().nextInt(2);
        bottomr = new Random().nextInt(3);
        shoesr = new Random().nextInt(5);

        texthat = Util.hat[hatr];
        texttop1 = Util.top[topr1];
        texttop2 = Util.top2[topr2];
        textbottom = Util.bottom[bottomr];
        textshoes = Util.shoes[shoesr];

        //누를때마다 크게 증가시켜 검색범위 증가.
        start += 10;

        //셋팅
        parser = new Parsers();
        new hatAsync().execute();

        parser = new Parsers();
        new topAsync().execute();

        parser = new Parsers();
        new bottomAsync().execute();

        parser = new Parsers();
        new shoesAsync().execute();
    }

    class hatAsync extends AsyncTask<String, Void, String> {
        int random;
        @Override
        protected String doInBackground(String... strings) { return parser.Parser(start, texthat); }
        @Override
        protected void onPostExecute(String items) {
            try {
                JSONObject jsonObject = new JSONObject(items);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i <= Util.SEARCH_COUNT2 - 1; i++) {
                    image[i] = (String) jsonArray.getJSONObject(i).get("image").toString();
                    random = new Random().nextInt(4);
                }
            }
            catch (JSONException e) { e.printStackTrace(); }
            ImgAsyschat imgAsyschat = new ImgAsyschat();
            imgAsyschat.img(image[random]);
            imgAsyschat.execute();
        }//onpost
    }//hatAsync

    class topAsync extends AsyncTask<String, Void, String> {
        int random;
        @Override
        protected String doInBackground(String... strings) { return parser.Parser(start, texttop2+" "+texttop1); }
        @Override
        protected void onPostExecute(String items) {
            try {
                JSONObject jsonObject = new JSONObject(items);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i <= Util.SEARCH_COUNT2 - 1; i++) {
                    image[i] = (String) jsonArray.getJSONObject(i).get("image").toString();
                    random = new Random().nextInt(4);
                }
            }
            catch (JSONException e) { e.printStackTrace(); }
            ImgAsysctop imgAsysctop = new ImgAsysctop();
            imgAsysctop.img(image[random]);
            imgAsysctop.execute();
        }//onpost
    }//topAsync

    class bottomAsync extends AsyncTask<String, Void, String> {
        int random;
        @Override
        protected String doInBackground(String... strings) { return parser.Parser(start, textbottom); }
        @Override
        protected void onPostExecute(String items) {
            try {
                JSONObject jsonObject = new JSONObject(items);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i <= Util.SEARCH_COUNT2 - 1; i++) {
                    image[i] = (String) jsonArray.getJSONObject(i).get("image").toString();
                    random = new Random().nextInt(4);
                }
            }
            catch (JSONException e) { e.printStackTrace(); }
            ImgAsyscbottom imgAsyscbottom = new ImgAsyscbottom();
            imgAsyscbottom.img(image[random]);
            imgAsyscbottom.execute();
        }//onpost
    }//bottomAsynce

    class shoesAsync extends AsyncTask<String, Void, String> {
        int random;
        @Override
        protected String doInBackground(String... strings) { return parser.Parser(start, textshoes); }
        @Override
        protected void onPostExecute(String items) {
            try {
                JSONObject jsonObject = new JSONObject(items);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i <= Util.SEARCH_COUNT2 - 1; i++) {
                    image[i] = (String) jsonArray.getJSONObject(i).get("image").toString();
                    random = new Random().nextInt(4);
                }
            }
            catch (JSONException e) { e.printStackTrace(); }
            ImgAsyscshoes imgAsyscshoes = new ImgAsyscshoes();
            imgAsyscshoes.img(image[random]);
            imgAsyscshoes.execute();
        }//onpost
    }//shoesAsync

    //이미지를 로드하는 Async클래스
    class ImgAsyschat extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView hat = ((Activity) context).findViewById(R.id.hat);
       void img(String url) { this.url = url; }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            }
            catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) { hat.setImageBitmap(bitmap); }
    }//imgAsyncHat

    class ImgAsysctop extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView top = ((Activity) context).findViewById(R.id.top);
        void img(String url) { this.url = url; }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            }
            catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) { top.setImageBitmap(bitmap); }
    }//imgAsyncTop

    class ImgAsyscbottom extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView bottom = ((Activity) context).findViewById(R.id.bottom);
        void img(String url) { this.url = url; }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            }
            catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) { bottom.setImageBitmap(bitmap); }
    }//imgAsyncBottom

    class ImgAsyscshoes extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView shoes = ((Activity) context).findViewById(R.id.shoes);
        void img(String url) { this.url = url; }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            }
            catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) { shoes.setImageBitmap(bitmap); }
    }//imgAsyncShoes
}