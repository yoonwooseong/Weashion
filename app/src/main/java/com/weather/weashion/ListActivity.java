package com.weather.weashion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

//세로본능 - 미완성
public class ListActivity extends Activity {
    ListView cartList;
    ArrayList<CartVO> list;

    //pref 중복 방지
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sampleData();

        cartList = findViewById(R.id.cartList);
        MyAdapter myAdapter = new MyAdapter(this, R.layout.activity_resource, list);

        cartList.setAdapter(myAdapter);

        //해당 상품 링크로 이동. = 예) naver
        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sampleData()));
                startActivity(intent);
            }
        });
    } //onCreate

    public String sampleData(){

        int m_price = 30000;
        String price = String.format("%,d", m_price);
        int img = R.drawable.sample;
        String link = "https://www.naver.com";
        String category = "모자";
        list = new ArrayList<CartVO>();

        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));

        count++;

        SharedPreferences pref = getSharedPreferences("CART", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("img" + count, img);
        edit.putString("price" + count, price);
        edit.putString("link" + count, link);
        edit.putString("category" + count, category);
        edit.commit();

        return link;
    } //sampleData()

    //핸드폰 가로 세로 인식
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent i = new Intent(ListActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}