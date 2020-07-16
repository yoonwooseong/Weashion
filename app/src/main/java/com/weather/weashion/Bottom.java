package com.weather.weashion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;


public class Bottom {

    Context context;
    static int start1 = 1;
    static int start2 = 2;
    static int start3 = 3;
    static int start4 = 4;
    ModelParser parsers;
    ListView listview_alterdialog_list;
    ArrayList list;
    SearchListActivity searchListActivity;
    int random;
    String randoms;
    String type = "bottom";
    Dialog dialog;

    public Bottom(Context context) {
        this.context = context;
        parsers = new ModelParser();
        list = new ArrayList();
        listview_alterdialog_list = ((Activity)context).findViewById(R.id.listview_alterdialog_list);
        random = new Random().nextInt(3);
        randoms = Util.bottom[random];
        new NaverAsync().execute();

    }

class NaverAsync extends AsyncTask<String, Void, ArrayList<ModelVO>>{

    @Override
    protected ArrayList<ModelVO> doInBackground(String... strings) {
        list.add(parsers.Parser(start1,randoms));
        list.add(parsers.Parser(start2,randoms));
        list.add(parsers.Parser(start3,randoms));
        list.add(parsers.Parser(start4,randoms));
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<ModelVO> modelVOS) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.model_dialog_list);
        listview_alterdialog_list = dialog.findViewById(R.id.listview_alterdialog_list);
        searchListActivity = new SearchListActivity(context, R.layout.model_dialog_select, modelVOS, listview_alterdialog_list, type);
        listview_alterdialog_list.setAdapter(searchListActivity);

        Button dialog_next = dialog.findViewById(R.id.dialog_next);
        dialog_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new Bottom(context);
            }
        });
        dialog.show();
        start1 +=4;
        start2 +=4;
        start3 +=4;
        start4 +=4;
        }
    }
}