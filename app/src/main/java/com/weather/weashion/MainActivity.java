package com.weather.weashion;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {

    //---------------메인화면
    PollActivity pollActivity;
    View secondmain;
    DrawerLayout drawerLayout;
    RelativeLayout view_mode_model;
    LinearLayout view_mode_list;
    ImageView hat, top, bottom, shoes, umb, img_open_drawer_l, img_open_drawer;//Model모드 이미지뷰 카테
    Button btn_mode_list, btn_mode_model, btn_mode_list_l, btn_mode_model_l, btn_open_drawer, btn_open_drawer_l;
    Button addCateBtn;

    SwipeMenuListView listView;
    ArrayList<String> arr;
    CateAdapter cateAdapter = null;

    /*메인화면 하단*/
    static EditText search_tag;
    Button btn_recommend, btn_query, btn_setting;
    int start = 1;

    //---------------장바구니
    ListView cartList;
    ArrayList<CartVO> list;
    TextView price, category;
    View view;

    //--------------날씨
    TextView txt_condition, txt_temp, txt_p_cloud, txt_p_humidity;
    TextView txt_uvi, txt_morn, txt_day, txt_eve, txt_night;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;
    String imgWeatherCode;
    String timezone = null;
    String temp= null;
    String weatherMain = null;//배경선택
    int selectDate = 0;
    String weatherKR;

    /*날짜 Dialog*/
    AlertDialog.Builder dateDialog;
    final CharSequence[] inDayItems = {"오늘","내일","모레"};
    String loadedJson;

    /*GPS main*/
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //내부저장소 접근 권한에 대한 수락 여부 확인
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            setPermission();
        }

        arr = new ArrayList<>();
        arr.add("나이키 모자 헤리티지86 스우시캡 볼캡");
        arr.add("폴로랄프로렌 반팔티셔츠");
        arr.add("The 착한 밴딩 면바지");
        arr.add("마르지엘라 독일군 페인팅 스니커즈 S57WS0240 [249537]");
        arr.add("커버낫 코듀라 어센틱 로고 럭색 블랙");
        arr.add("체인팔찌 알타 캡처 미니 참 링크팔찌");
        arr.add("다이에나롤랑 소가죽 자동버클 남자 벨트");
        Log.i("MYMY",arr.get(1));

        //--------설문조사
        pollActivity = new PollActivity(MainActivity.this);
        pollActivity.setDialog();

        //---------------메인화면
        drawerLayout = findViewById(R.id.drawerLayout);
        secondmain = findViewById(R.id.largestBox);
        btn_open_drawer = findViewById(R.id.btn_open_drawer);//서랍 열기(날씨그림) 버튼
        btn_open_drawer_l = findViewById(R.id.btn_open_drawer_l);
        hat = findViewById(R.id.hat);//모자그림
        top = findViewById(R.id.top);//상의그림
        bottom = findViewById(R.id.bottom);//바지그림
        shoes = findViewById(R.id.shoes);//신발그림
        umb = findViewById(R.id.umb);//우산그림
        img_open_drawer_l = findViewById(R.id.img_open_drawer_l);
        img_open_drawer = findViewById(R.id.img_open_drawer);

        btn_mode_list = findViewById(R.id.btn_mode_list);
        btn_mode_model = findViewById(R.id.btn_mode_model);
        btn_mode_list_l = findViewById(R.id.btn_mode_list_l);
        btn_mode_model_l = findViewById(R.id.btn_mode_model_l);
        view_mode_list = findViewById(R.id.view_mode_list);
        view_mode_model = findViewById(R.id.view_mode_model);

        search_tag = findViewById(R.id.search_tag);
        btn_recommend = findViewById(R.id.btn_recommend);
        btn_query = findViewById(R.id.btn_query);
        btn_setting = findViewById(R.id.btn_setting);
        btn_mode_list.setPaintFlags(btn_mode_list.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_mode_list_l.setPaintFlags(btn_mode_list_l.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        hat.setOnClickListener( searchCategroyClick );

        /*상단 버튼 클릭*/
        btn_mode_list.setOnClickListener( modeClick );
        btn_mode_list_l.setOnClickListener( modeClick );
        btn_mode_model.setOnClickListener( modeClick );
        btn_mode_model_l.setOnClickListener( modeClick );
        btn_open_drawer.setOnClickListener(drawerOpenClick);
        btn_open_drawer_l.setOnClickListener(drawerOpenClick);

        /*하단 버튼 클릭*/
        btn_recommend.setOnClickListener( bottomBtnClick );
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "배고파", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_setting.setOnClickListener( bottomBtnClick );

        addCateBtn = findViewById(R.id.addCateBtn);

        addCateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arr.add("더해버리기");
                cateAdapter.notifyDataSetChanged();
                listView.setAdapter(cateAdapter);
            }
        });

        listView = findViewById(R.id.listView);

        cateAdapter = new CateAdapter(MainActivity.this, R.layout.list_form, arr);
        listView.setAdapter(cateAdapter);
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Toast.makeText(MainActivity.this," "+position+"여기에 모자 클릭시 이동하는 곳과 같은 화면",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete
                        arr.remove(position);
                        cateAdapter.notifyDataSetChanged();
                        listView.setAdapter(cateAdapter);
                        Toast.makeText(MainActivity.this," "+position+"하이",Toast.LENGTH_SHORT).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        // Left
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        //---------------장바구니
        cartList = findViewById(R.id.cartList);

        sampleData(); //누르면 해당 상품의 링크로 이동함.

        MyAdapter myAdapter = new MyAdapter(this, R.layout.activity_resource, list);
        cartList.setAdapter(myAdapter);

        LayoutInflater linf = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        view = linf.inflate(R.layout.activity_resource, null);

        price = view.findViewById(R.id.price);
        category = view.findViewById(R.id.category);

        //항목 클릭 시 상품 정보 페이지로 이동
        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sampleData()));
                startActivity(intent);
            }
        });

        //---------------날씨 정보
        /*날씨 정보*/
        txt_condition = findViewById(R.id.txt_condition);
        txt_temp = findViewById(R.id.txt_temp);
        txt_p_cloud = findViewById(R.id.txt_p_cloud);
        txt_p_humidity = findViewById(R.id.txt_p_humidity);
        btn_current = findViewById(R.id.btn_current);
        btn_details = findViewById(R.id.btn_details);
        box_current = findViewById(R.id.box_current);
        box_details = findViewById(R.id.box_details);
        largestBox = findViewById(R.id.largestBox);

        /*날씨 상세 정보 ID*/
        txt_uvi = findViewById(R.id.txt_uvi);
        txt_morn = findViewById(R.id.txt_morn);
        txt_day = findViewById(R.id.txt_day);
        txt_eve = findViewById(R.id.txt_eve);
        txt_night = findViewById(R.id.txt_night);

        /*클릭 이벤트 추가*/
        btn_current.setOnClickListener( weatherClick );
        btn_details.setOnClickListener( weatherClick );
        txt_condition.setOnClickListener( weatherClick );

        /*GPS 추가*/
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(MainActivity.this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);

        new LoadWeatherTask(this, lat, lon).execute();

    }//onCreate


    //앱 권한 설정 감지자
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //모든 권한의 수락이 완료된 경우 호출되는 메서드
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //한 가지라도 허용되지 않은 권한이 있는 경우 호출되는 메서드
            finish();
        }
    };

    public void fileSave(String text){

        File f = new File(Environment.getExternalStorageDirectory()+"/weashion/");
        if(!f.exists()){
            f.mkdirs();
        }

        File f2 = new File(f,  "cart.txt");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(f2, true);
            fos.write((text).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //fileSave()

    public void setPermission(){
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("내부 저장소 접근 권한을 허용해야 합니다.\n설정->권한 에서 해당 권한을 활성화 해주십시오.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) //설정하고자 하는 권한들 다중추가 가능
                .check();
    }

    /*list 모드일때*/
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(200);
            // set item title
            openItem.setTitle("open");
            // set item title fontsize
            openItem.setTitleSize(10);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(200);
            // set a icon
            deleteItem.setIcon(R.drawable.cantrash);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    /*클릭 메서드*/
    View.OnClickListener modeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch ( view.getId() ){
                case R.id.btn_mode_list: case R.id.btn_mode_list_l:
                    btn_mode_list.setPaintFlags(btn_mode_list.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_mode_list_l.setPaintFlags(btn_mode_list_l.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_mode_model.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    btn_mode_model_l.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    view_mode_list.setVisibility(View.VISIBLE);
                    view_mode_model.setVisibility(View.GONE);
                    break;

                case R.id.btn_mode_model: case R.id.btn_mode_model_l:
                    btn_mode_list.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    btn_mode_list_l.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    btn_mode_model.setPaintFlags(btn_mode_model.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_mode_model_l.setPaintFlags(btn_mode_model_l.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    view_mode_list.setVisibility(View.GONE);
                    view_mode_model.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    View.OnClickListener drawerOpenClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawerLayout.openDrawer(secondmain);
        }
    };
    View.OnClickListener bottomBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener searchCategroyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ( view.getId() ){
                case R.id.hat :
                    //이때 숨은 검색창에 입력해서 출력하도록
                    break;
            }
        }
    };

    //---------------cart
    public String sampleData(){//샘플 데이터 - 받아온 데이터 여기에 담으면 됨

        int count = 0;
        int m_price = 30000;
        String price = String.format("%,d", m_price);
        int img = R.drawable.sample;//임시
        String link = "https://www.naver.com";
        String category = "모자";
        list = new ArrayList<CartVO>();

        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));
        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));
        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));
        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));
        list.add(new CartVO(Integer.toString(img), price + "원", link, "카테고리 : " + category));

        //sampleData() 에 추가
        String text = price + ", " + img + ", " + link + ", " + category + "\n";
        fileSave(text);

        SharedPreferences pref = getSharedPreferences("CART", MODE_PRIVATE);

        Log.i("MY", pref.getString("price0", ""));//가져온 장바구니의 1번째 아이템 가격

        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("img" + count, img);
        edit.putString("price" + count, price);
        edit.putString("link" + count, link);
        edit.putString("category" + count, category);
        edit.commit();

        return link;
    } //sampleData()

    //---------------날씨
    public void changeText(int i){
        switch (i){
            case Util.TODAY:
                btn_current.setText("현재 날씨");
                break;
            case Util.TOMORROW:
                btn_current.setText("내일 평균 날씨");
                break;
            case Util.AFTERTOMORROW:
                btn_current.setText("모래 평균 날씨");
                break;
        }
    }

    //JSON 정보를 PASING 하는 메서드
    public void currentWeatherParser(String resultJson){

        /*날씨 기본 정보*/
        String humidity, clouds;
        /*날씨 상세 정보*/
        String uvi, morn, day, eve, night;

        try {
            JSONObject jObject = new JSONObject(resultJson);
            timezone = jObject.getString("timezone");
            String[] splitTimezone = timezone.split("/");

            if( selectDate == Util.TODAY  ){
                JSONObject currentObject = jObject.getJSONObject("current");
                temp = String.valueOf(currentObject.getInt("temp"));

                JSONObject weatherCondition = (JSONObject) currentObject.getJSONArray("weather").get(selectDate);
                weatherMain = weatherCondition.getString("main");
            } else {
                JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(selectDate);
                JSONObject tempAvg = dailyObject.getJSONObject("temp");
                temp = String.valueOf(tempAvg.getInt("day"));

                JSONObject weatherCondition = (JSONObject) dailyObject.getJSONArray("weather").get(Util.TODAY );
                weatherMain = weatherCondition.getString("main");
            }

            JSONObject dailyObject = (JSONObject)jObject.getJSONArray("daily").get(selectDate);/*get(0)은 오늘날짜 +1할수록 다음날*/
            JSONObject dailyTempObject = dailyObject.getJSONObject("temp");
            morn = String.valueOf(dailyTempObject.getInt("morn"));
            day = String.valueOf(dailyTempObject.getInt("day"));
            eve = String.valueOf(dailyTempObject.getInt("eve"));
            night = String.valueOf(dailyTempObject.getInt("night"));
            humidity = String.valueOf(dailyObject.getInt("humidity"));
            clouds = String.valueOf(dailyObject.getInt("clouds"));
            uvi = String.valueOf(dailyObject.getInt("uvi"));

            choiceBackground(weatherMain);
            String weatherUrl = "http://openweathermap.org/img/wn/"+imgWeatherCode+"@2x.png";
            Glide.with(this).load(weatherUrl).into(img_open_drawer_l);
            Glide.with(this).load(weatherUrl).into(img_open_drawer);

            /*현재 날씨 정보 출력*/
            /*txt_city.setText(cityNameKR(splitTimezone[1]));*/
            txt_condition.setText(weatherKR);
            txt_temp.setText(temp+"도");
            txt_p_humidity.setText("습도 : "+humidity+"%");
            txt_p_cloud.setText("흐림 : "+clouds+"%");

            /*상세 정보 출력*/
            txt_uvi.setText("자외선\n\t\t"+uvi);
            txt_morn.setText("아침\n"+morn+"℃");
            txt_day.setText("낮\n"+day+"℃");
            txt_eve.setText("저녁\n"+eve+"℃");
            txt_night.setText("밤\n"+night+"℃");

            /*정보 저장(날씨, 온도) - 추후 네이버 api 조건에 활용*/
            SharedPreferences pref = getSharedPreferences("SHARE", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("weather", weatherMain);
            edit.putString("temp", temp);
            edit.commit();

            /*다시 Pasing해주기 위해 Json저장*/
            loadedJson = resultJson;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//currentWeatherParser()

    /*날씨에 따른 배경화면 선택 메서드*/
    public void choiceBackground(String weather){
        switch (weatherMain) {
            case "Clouds":
                imgWeatherCode = "04d";
                price.setTextColor(Color.BLACK);
                category.setTextColor(Color.BLACK);
                largestBox.setBackgroundResource(R.drawable.cloud);
                weatherKR = "구름";
                break;
            case "Clear":
                imgWeatherCode = "01d";
                largestBox.setBackgroundResource(R.drawable.clear);
                weatherKR = "맑음";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                break;
            case "Rain": case "Drizzle":
                imgWeatherCode = "09d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.rain);
                weatherKR = "비";
                break;
            case "Thunderstorm":
                imgWeatherCode = "11d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.thunderstorm);
                weatherKR = "뇌우";
                break;
            case "Snow":
                imgWeatherCode = "13d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.snow);
                weatherKR = "눈";
                break;
            default:
                imgWeatherCode = "50d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.mist);
                weatherKR = "안개";
                break;
        }
    }//choiceBackground()

    /*날씨, 도시 이벤트 추가*/
    View.OnClickListener weatherClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_current:
                    if(box_current.getVisibility() == View.VISIBLE){
                        dateDialog = new AlertDialog.Builder(MainActivity.this);
                        dateDialog.setTitle("날짜 선택").setItems(inDayItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectDate = i;
                                changeText(i);
                                currentWeatherParser(loadedJson);
                            }
                        });
                        dateDialog.setCancelable(false);
                        dateDialog.create();
                        dateDialog.show();
                    } else {
                        btn_current.setBackgroundResource(R.drawable.background_radius3);
                        btn_details.setBackgroundResource(R.drawable.background_radius2);
                        box_current.setVisibility(View.VISIBLE);
                        box_details.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btn_details:
                    btn_current.setBackgroundResource(R.drawable.background_radius2);
                    btn_details.setBackgroundResource(R.drawable.background_radius3);
                    box_current.setVisibility(View.GONE);
                    box_details.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    //경로 수집
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    //휴대폰 가로 세로 인식
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            startActivity(i);
            finish();
        }
    }

    /*여기부터 GPS추가*/
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}