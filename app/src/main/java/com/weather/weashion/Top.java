package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import java.util.Random;

public class Top {
    private Context context;

    static EditText search;

    SearchListActivity searchListActivity = new SearchListActivity();
    public Top(Context context) {
        this.context = context;
        search = ((Activity)context).findViewById(R.id.search_tag);
        int random = new Random().nextInt(3);
        int random2 = new Random().nextInt(2);
        String randoms = Util.top[random];
        String randoms2 = Util.top2[random2];
        search.setText(randoms2+" "+randoms);
        searchListActivity.SearchList(context);
    }
}
