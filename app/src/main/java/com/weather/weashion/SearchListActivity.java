package com.weather.weashion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;


public class SearchListActivity extends ArrayAdapter<ModelVO> implements AdapterView.OnItemClickListener {

    private Context context;
    ArrayList<ModelVO> list;
    int resource;
    ModelVO rvo;
    ImgAsys imgAsys;
    String type;

    public SearchListActivity(Context context, int resource, ArrayList list, ListView myView, String type) {
        super(context, resource,list);
        this.context = context;
        this.resource = resource;
        this.list = list;
        this.type = type;

        myView.setOnItemClickListener(this);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate(resource, null);

        ImageView img = convertView.findViewById(R.id.alertDialogItemImageView);

        rvo = list.get(position);

        Glide.with(convertView).load(rvo.getImage()).into(img);

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
        imgAsys = new ImgAsys();
        imgAsys.img(list.get(index).getImage());
        imgAsys.execute();
        MainActivity.insertData(list.get(index).getImage(),list.get(index).getLink(),list.get(index).getLprice(),list.get(index).getCategory2());
    }
    //이미지를 로드하는 Async클래스
    class ImgAsys extends AsyncTask<Void, Void, Bitmap> {
        Bitmap bm;
        String url;
        ImageView hat = ((Activity) context).findViewById(R.id.hat);
        ImageView top = ((Activity) context).findViewById(R.id.top);
        ImageView bottom = ((Activity) context).findViewById(R.id.bottom);
        ImageView shoes = ((Activity) context).findViewById(R.id.shoes);
        void img(String url) { this.url = url; }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL img_url;
            try {
                img_url = new URL(url);
                BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            }
            catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            switch (type) {
                case "hat":
                    hat.setImageBitmap(bitmap);
                    break;
                case "top":
                    top.setImageBitmap(bitmap);
                    break;
                case "bottom":
                    bottom.setImageBitmap(bitmap);
                    break;
                case "shoes":
                    shoes.setImageBitmap(bitmap);
                    break;
            }
        }
    }//imgAsyncHat

}