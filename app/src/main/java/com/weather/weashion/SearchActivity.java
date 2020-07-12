package com.weather.weashion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    ListView myListView;
    static EditText search;
    Button search_btn;
    SearchParser searchParser;
    ArrayList<SearchVO> list;
    ViewResultAdapter adapter;
    boolean mLockListView = true;

    int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);

        search = findViewById(R.id.search);
        myListView = findViewById(R.id.myListView);
        search_btn = findViewById(R.id.search_btn);

        searchParser = new SearchParser();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 한 글자 이상 입력했을 때...

                if( search.getText().toString().trim().length() > 0 ) {
                    start = 1;//초기화

                    list = new ArrayList<SearchVO>();
                    adapter = null;

                    new NaverAsync().execute();

                    InputMethodManager imm =
                            (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                }
            }
        });
    }


    class NaverAsync extends AsyncTask<String, Void, ArrayList<SearchVO>> {

        @Override
        protected ArrayList<SearchVO> doInBackground(String... strings) {
            list = searchParser.Parser(start, list);
            /*Log.i("MY",parser.Parser(start, list));*/
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchVO> items){
            Log.i("MY","여기였나"+items.get(0).getTitle());

            if( adapter == null ){
                adapter = new ViewResultAdapter( SearchActivity.this, R.layout.search_result_item, items, myListView );

                /*myListView.setOnScrollListener(scrollListener);*/
                myListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }

        }

    }

}