package com.weather.weashion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CateAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    ArrayList<String> arr;

    public CateAdapter(Context context, int resource, ArrayList<String> arr) {
        super(context, resource, arr);

        this.context = context;
        this.arr = arr;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService(
                context.LAYOUT_INFLATER_SERVICE);
        convertView = linf.inflate(resource, null);

        String str = arr.get( position );

        ImageView img = convertView.findViewById(R.id.cate_img);
        TextView list_brand = convertView.findViewById(R.id.list_form_title);
        TextView list_title = convertView.findViewById(R.id.list_form_title2);
        TextView list_price = convertView.findViewById(R.id.list_form_title3);
        list_brand.setText(str);
        list_title.setText(str);
        list_price.setText(str);

        return convertView;
    }
}
