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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Arrays;

public class SearchListActivity{
    private Parsers parser;
    private Context context;
    private String msearch = "";

    static int start = 1;

    String[] first = new String[Util.SEARCH_COUNT2];//초기값
    String[] link = new String[Util.SEARCH_COUNT2];
    String[] image = new String[Util.SEARCH_COUNT2];
    String[] lprice = new String[Util.SEARCH_COUNT2];
    String[] category2 = new String[Util.SEARCH_COUNT2];
    ImgAsysc imgAsysc = new ImgAsysc();

    public void SearchList(final Context context) {
        this.context = context;
        EditText search = ((Activity)context).findViewById(R.id.search_tag);
        msearch = String.valueOf(search.getText());
        parser = new Parsers();
        new NaverAsync().execute();

    }

    class NaverAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) { return parser.Parser(start, msearch); }

        @Override
        protected void onPostExecute(String items) {
            String[] category3 = new String[Util.SEARCH_COUNT2];//확인용

            try {
                JSONObject jsonObject = new JSONObject(items);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for (int i = 0; i <= Util.SEARCH_COUNT2 - 1; i++) {
                    link[i] = (String) jsonArray.getJSONObject(i).get("link").toString();
                    image[i] = (String) jsonArray.getJSONObject(i).get("image").toString();
                    lprice[i] = (String) jsonArray.getJSONObject(i).get("lprice").toString();
                    category2[i] = (String) jsonArray.getJSONObject(i).get("category2").toString();
                    category3[i] = (String) jsonArray.getJSONObject(i).get("category3").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int j = 0; j <= 3; j++) {
                first[j] = lprice[j];
            }
            Log.i("my", "\nfirst :" + Arrays.toString(first));
            Log.i("my", "\ncategory3 :" + Arrays.toString(category3));

            new dialog(first);

        }//onpost
    }
    class dialog{
        public dialog(final String[] first) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("항목중에 하나를 선택하세요").setItems(first, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    //image[index]
                    String str = image[index];
                    Log.i("My","img : " + str);
                    imgAsysc.img(str);
                    imgAsysc.execute();
                    MainActivity.insertData(image[index], lprice[index], link[index], category2[index]);
                }
            });
            alert.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SearchListActivity.start += 4;
                }});
            alert.create();
            alert.show();
        }
    }
    //이미지를 로드하는 Async클래스
    class ImgAsysc extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView img;

        void img(String url){
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //읽어들인 bitmap을 ImageView에 세팅
            Log.i("MY", msearch);
            switch (msearch){
                case "패션모자": case "스포츠헤어밴드": case "스냅백":case "비니":
                    img = ((Activity)context).findViewById(R.id.hat);
                    img.setImageBitmap(bitmap);
                    Log.i("MY","bitmap : "+bitmap);
                    break;
                case "얇은 반팔티": case "얇은 긴팔티": case "얇은 맨투맨" : case "두꺼운 반팔티": case "두꺼운 긴팔티": case "두꺼운 맨투맨" :
                    img = ((Activity)context).findViewById(R.id.top);
                    img.setImageBitmap(bitmap);
                    Log.i("MY","bitmap : "+bitmap);
                    break;
                case "반바지": case "면바지": case "청바지":
                    img = ((Activity)context).findViewById(R.id.bottom);
                    img.setImageBitmap(bitmap);
                    Log.i("MY","bitmap : "+bitmap);
                    break;
                case "나이키신발" : case "아디다스신발": case "아식스신발": case "컨버스신발": case "닥터마틴신발":
                    img = ((Activity)context).findViewById(R.id.shoes);
                    img.setImageBitmap(bitmap);
                    Log.i("MY","bitmap : "+bitmap);
                    break;
            }
        }
    }
}