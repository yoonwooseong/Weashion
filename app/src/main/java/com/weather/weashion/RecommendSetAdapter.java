package com.weather.weashion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecommendSetAdapter extends ArrayAdapter<SearchVO> implements AdapterView.OnItemClickListener {

    Context context;
    ArrayList<SearchVO> list;
    int resource;
    SearchVO rvo;

    public RecommendSetAdapter(Context context, int resource, ArrayList<SearchVO> list, ListView myListView) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;

        //리스트뷰 클릭이벤트 감지자 등록
        myListView.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate(resource, null);//resource 라는 북xml을 컨버트뷰로 만들게요
        rvo = list.get(position);

        TextView title = convertView.findViewById(R.id.result_title);
        TextView brand = convertView.findViewById(R.id.result_brand);
        TextView lprice = convertView.findViewById(R.id.result_lprice);
        ImageView img = convertView.findViewById(R.id.result_image);

        if (rvo.getTitle() == null){
            rvo.setTitle("");
        }
        if (rvo.getBrand() == null){
            rvo.setBrand("");
        }
        if (rvo.getLprice() == null){
            rvo.setLprice("");
        }
        if (rvo.getImage() == null){
            rvo.setImage("@mipmap/ic_launcher_round");
        }
        title.setText( rvo.getTitle() );
        brand.setText( rvo.getBrand() );
        lprice.setText( rvo.getLprice() );
        Glide.with(convertView).load(rvo.getImage()).into(img);

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
