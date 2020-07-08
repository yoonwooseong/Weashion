package com.weather.weashion;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<CartVO> {

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

        img.setImageResource(Integer.parseInt(data.get(position).getImg()));
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

        return convertView;
    }
}
