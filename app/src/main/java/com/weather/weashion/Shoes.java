package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import java.util.Random;

public class Shoes {
    private Context context;

    static EditText search;

    SearchListActivity searchListActivity = new SearchListActivity();
    public Shoes(Context context) {
        this.context = context;
        search = ((Activity)context).findViewById(R.id.search_tag);
        int random = new Random().nextInt(5);
        String randoms = Util.shoes[random];
        search.setText(randoms);
        searchListActivity.SearchList(context);
    }
}
