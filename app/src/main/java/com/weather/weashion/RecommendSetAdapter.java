package com.weather.weashion;

import android.content.Context;
import android.util.Log;
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

        TextView title = convertView.findViewById(R.id.result_title);
        TextView brand = convertView.findViewById(R.id.result_brand);
        TextView lprice = convertView.findViewById(R.id.result_lprice);
        ImageView img = convertView.findViewById(R.id.result_image);

        rvo = list.get(position);

        /*Log.i("err",rvo.getTitle());
        Log.i("err",rvo.getBrand());
        Log.i("err",rvo.getLprice());
        Log.i("err",rvo.getImage());*/

        title.setText( rvo.getTitle() );
        brand.setText( rvo.getBrand() );
        lprice.setText( rvo.getLprice() );

        if(rvo.getImage().equals(" ")){
            Log.i("where","여기왜안나와");

            //아이콘 제작자 <a href="https://www.flaticon.com/kr/authors/freepik" title="Freepik">Freepik</a>
            //from <a href="https://www.flaticon.com/kr/" title="Flaticon">www.flaticon.com</a>

            switch (rvo.getType()){
                case Util.CATEGORY_HAT:
                    Glide.with(convertView).load(R.drawable.iconhat).into(img);
                    break;
                case Util.CATEGORY_TOP:
                    Glide.with(convertView).load(R.drawable.icontshirt).into(img);
                    break;
                case Util.CATEGORY_BOTTOM:
                    Glide.with(convertView).load(R.drawable.iconpants).into(img);
                    break;
                case Util.CATEGORY_SHOES:
                    Glide.with(convertView).load(R.drawable.iconshoes).into(img);
                    break;
                case Util.CATEGORY_UMB:
                    Glide.with(convertView).load(R.drawable.iconumb).into(img);
                    break;
                default:
                    Glide.with(convertView).load(rvo.getImage()).into(img);
                    break;
            }
        } else {
            Glide.with(convertView).load(rvo.getImage()).into(img);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        rvo = list.get(position);
        String img = rvo.getImage();
        String price = rvo.getLprice();
        String link = rvo.getLink();
        String category;
        switch (rvo.getType()){
            case Util.CATEGORY_HAT:
                category = "모자";
                break;

            case Util.CATEGORY_TOP:
                category = "상의";
                break;

            case Util.CATEGORY_BOTTOM:
                category = "하의";
                break;

            case Util.CATEGORY_SHOES:
                category = "신발";
                break;

            case Util.CATEGORY_UMB:
                category = "우산";
                break;

            case Util.CATEGORY_ACC:
                category = "악세사리";
                break;
            default:
                category = "없음";
                break;
        }
        if (rvo.getTitle().equals("준비중")){

        } else {
            MainActivity.insertData(img,link, price, category);
        }
    }
}
