package com.weather.weashion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ViewResultAdapter extends ArrayAdapter<SearchVO> implements AdapterView.OnItemClickListener {//BookVO를 출력해야하므로 제너릭타입 Bookvo

    Context context;
    ArrayList<SearchVO> list;
    int resource;
    SearchVO svo;

    public ViewResultAdapter(Context context, int resource, ArrayList<SearchVO> list, ListView myListView) {
        super(context, resource, list); //갯뷰 호출 못한다, 갯수몰라서, list 안넣으면

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
        svo = list.get(position);

        TextView title = convertView.findViewById(R.id.result_title);
        TextView brand = convertView.findViewById(R.id.result_brand);
        TextView lprice = convertView.findViewById(R.id.result_lprice);
        ImageView img = convertView.findViewById(R.id.result_image);

        title.setText( svo.getTitle() );
        brand.setText( svo.getBrand() );
        lprice.setText( svo.getLprice() );
        Glide.with(convertView).load(svo.getImage()).into(img);

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//i : 몇번째 클릭한지

        //추출한 id를 통해 상세정보 페이지로 연결할 url준비
        String detailLink = svo.getLink();
        Intent intent = new Intent(Intent.ACTION_VIEW);//괄호 안 없으면 구글이나 이상한대감
        intent.setData( Uri.parse(detailLink) );
        context.startActivity(intent);//상세보기 페이지로 화면 전환*/

    }
}

