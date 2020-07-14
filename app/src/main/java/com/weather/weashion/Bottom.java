package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import java.util.Random;

public class Bottom {
    private Context context;
    static EditText search;

    SearchListActivity searchListActivity = new SearchListActivity();
    public Bottom(Context context) {
        this.context = context;
        search = ((Activity)context).findViewById(R.id.search_tag);
        int random = new Random().nextInt(3);
        String randoms = Util.bottom[random];
        search.setText(randoms);
        searchListActivity.SearchList(context);
    }
}
