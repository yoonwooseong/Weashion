package com.weather.weashion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<CartVO>{

    Context context;
    ArrayList<CartVO> data;
    int resource;
    CartVO cart;
    String weatherMain;

    public MyAdapter(Context context, int resource, ArrayList<CartVO> data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;

        SharedPreferences share = context.getSharedPreferences("SHARE", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        this.weatherMain = share.getString("weather", "");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater linf = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        convertView = linf.inflate(resource, null);
        cart = data.get(position);

        ImageView img = convertView.findViewById(R.id.img);
        TextView price = convertView.findViewById(R.id.price);
        TextView category = convertView.findViewById(R.id.category);

        //Integer.parseInt("http://www.jbros.co.kr/shopimages/jbros/3250020016782.jpg?1553672966")

        //img.setImageResource(Integer.parseInt(data.get(position).getImg()));
        price.setText("" + data.get(position).getPrice());
        category.setText(data.get(position).getCategory());

        switch (weatherMain){
            case "Clouds":
                price.setTextColor(Color.BLACK);
                category.setTextColor(Color.BLACK);
                break;
            case "Clear":
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
            case "Rain": case "Drizzle":
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
            case "Thunderstorm":
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
            case "Snow":
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
            default:
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
        }

        new ImgAsync(img, cart).execute();

        return convertView;
    }

    public static String getLink(String link){
        return link;
    }

}

//이미지를 로드하는 Async클래스
class ImgAsync extends AsyncTask<Void, Void, Bitmap> {

    Bitmap bm;
    ImageView mImg;
    CartVO vo;

    public ImgAsync(ImageView mImg, CartVO vo) {
        this.mImg = mImg;
        this.vo = vo;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        try {
            URL img_url = new URL(vo.getImg());

            BufferedInputStream bis = new BufferedInputStream( img_url.openStream() );

            //얻어온 스트림으로부터 Bitmap생성
            bm = BitmapFactory.decodeStream( bis );
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
        mImg.setImageBitmap( bitmap );
    }
}
