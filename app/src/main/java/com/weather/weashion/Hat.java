package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import java.util.Random;


public class Hat {
    static EditText search;
    private Context context;
    SearchListActivity searchListActivity = new SearchListActivity();


    public Hat(Context context) {
        this.context = context;
        search = ((Activity) context).findViewById(R.id.search_tag);
        int random = new Random().nextInt(4);
        String randoms = Util.hat[random];
        search.setText(randoms);
        searchListActivity.SearchList(context);
    }
}