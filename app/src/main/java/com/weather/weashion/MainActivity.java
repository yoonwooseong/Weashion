package com.weather.weashion;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends Activity {

    static MyAdapter myAdapter = null;
    static String cartText = "";
    String[] arrQuery = new String[]{"","","","","",""};
    int startCount = 0;

    //---------------메인화면
    PollActivity pollActivity;
    View secondmain;
    DrawerLayout drawerLayout;
    RelativeLayout view_mode_model;
    LinearLayout view_mode_list;
    ImageView img_open_drawer_l, img_open_drawer;//Model모드 이미지뷰 카테
    ImageView model_item_0, model_item_1, model_item_2, model_item_3, model_item_4;
    Button btn_mode_list, btn_mode_model, btn_mode_list_l, btn_mode_model_l, btn_open_drawer, btn_open_drawer_l;
    Button addCateBtn;
    Dialog dialog;

    SwipeMenuListView listView;
    RecommendSetParser recommendSetParser;
    RecommendSetAdapter recommendSetAdapter;
    ArrayList<SearchVO> recommendSetArr;
    ArrayList<SearchVO> bufferedRecommendSetArr;
    CateAdapter cateAdapter = null;
    ViewResultAdapter viewResultAdapter = null;

    static ArrayList<String> goModelImage;

    /*메인화면 하단, 추가 다이얼로그 버튼*/
    RadioGroup radioGroup;
    int radioResult;//카테고리 추가시 어떤 카테고린지 판별하는 변수
    Button addDialogBtn;
    RadioButton rb_btn_0, rb_btn_1, rb_btn_2, rb_btn_3, rb_btn_4, rb_btn_5;

    static EditText search_tag;
    Button btn_recommend, btn_query, btn_setting;
    int start = 1;

    //---------------장바구니
    static MyAdapter myAdapter = null;
    static String cartText = "";
    static String linkText = "";
    static ArrayList<CartVO> list = new ArrayList<>();
    ListView cartList;
    static ArrayList<CartVO> list = new ArrayList<>();
    TextView price, category;
    View view;
    String loadData = "";

    //--------------날씨
    TextView txt_condition, txt_temp, txt_p_cloud, txt_p_humidity;
    TextView txt_uvi, txt_morn, txt_day, txt_eve, txt_night;
    Button btn_current, btn_details;
    LinearLayout box_current, box_details, largestBox;
    String imgWeatherCode;
    String timezone = null;
    String temp= null;
    String weatherMain = null;//배경 선택
    int selectDate = 0;//날짜 선택
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

    /*부가기능 : 흔들기*/
    int speed;
    int x, y;
    final int SHAKE_HOLD1 = 40;
    final int SHAKE_HOLD2 = -40;
    SensorManager sensorM;
    Sensor acc_sensor;
    SensorEventListener sensorL;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //내부저장소 접근 권한에 대한 수락 여부 확인
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            setPermission();
        }

        bufferedRecommendSetArr = new ArrayList<>();

        //랜덤 추천 불러오기
        recommendSetParser = new RecommendSetParser();
        recommendSetArr = new ArrayList<>();

        bufferedRecommendSetArr = new ArrayList<>();


        RecommendSetNaverAsync recommendSetNaverAsync;
        recommendSetNaverAsync = new RecommendSetNaverAsync();
        new RecommendSetNaverAsync().execute();

        /*InputMethodManager imm =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(.getWindowToken(), 0);*/

        //--------설문조사
        pollActivity = new PollActivity(MainActivity.this, recommendSetNaverAsync);
        pollActivity.setDialog();

        //---------------메인화면
        drawerLayout = findViewById(R.id.drawerLayout);
        secondmain = findViewById(R.id.largestBox);
        btn_open_drawer = findViewById(R.id.btn_open_drawer);//서랍 열기(날씨그림) 버튼
        btn_open_drawer_l = findViewById(R.id.btn_open_drawer_l);
        model_item_0 = findViewById(R.id.hat);//모자그림
        model_item_1 = findViewById(R.id.top);//상의그림
        model_item_2 = findViewById(R.id.bottom);//바지그림
        model_item_3 = findViewById(R.id.shoes);//신발그림
        model_item_4 = findViewById(R.id.umb);//우산그림
        img_open_drawer_l = findViewById(R.id.img_open_drawer_l);
        img_open_drawer = findViewById(R.id.img_open_drawer);

        btn_mode_list = findViewById(R.id.btn_mode_list);
        btn_mode_model = findViewById(R.id.btn_mode_model);
        btn_mode_list_l = findViewById(R.id.btn_mode_list_l);
        btn_mode_model_l = findViewById(R.id.btn_mode_model_l);
        view_mode_list = findViewById(R.id.view_mode_list);
        view_mode_model = findViewById(R.id.view_mode_model);

        //다이얼로그

        search_tag = findViewById(R.id.search_tag);
        btn_recommend = findViewById(R.id.btn_recommend);
        btn_query = findViewById(R.id.btn_query);
        btn_setting = findViewById(R.id.btn_setting);
        btn_mode_list.setPaintFlags(btn_mode_list.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_mode_list_l.setPaintFlags(btn_mode_list_l.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        model_item_0.setOnClickListener( searchCategroyClick );

        /*상단 버튼 클릭*/
        btn_mode_list.setOnClickListener( modeClick );
        btn_mode_list_l.setOnClickListener( modeClick );
        btn_mode_model.setOnClickListener( modeClick );
        btn_mode_model_l.setOnClickListener( modeClick );
        btn_open_drawer.setOnClickListener(drawerOpenClick);
        btn_open_drawer_l.setOnClickListener(drawerOpenClick);

        //가속도 센서 부착.
        sensorM = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //흔들었을 때.

        sensorL = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                x = (int)sensorEvent.values[0];
                y = (int)sensorEvent.values[1];
                speed = x+y;

                //흔들림 감지.
                if (speed > SHAKE_HOLD1 || speed < SHAKE_HOLD2){
                    if(cnt/2==0) {
                        Log.i("my","sensor On!! 현재 카운트 : " + cnt);
                        recommendSetParser = new RecommendSetParser();
                        recommendSetArr = new ArrayList<>();
                        new RecommendSetNaverAsync().execute();
                        new ModelAllActivity(MainActivity.this);
                    }
                    cnt++;
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) { }
        };

        /*하단 버튼 클릭*/
        btn_recommend.setOnClickListener( bottomBtnClick );
        btn_setting.setOnClickListener( bottomBtnClick );
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",2);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        addCateBtn = findViewById(R.id.addCateBtn);

        addCateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_add_category);

                rb_btn_0 = dialog.findViewById(R.id.rb_btn_0);
                rb_btn_1 = dialog.findViewById(R.id.rb_btn_1);
                rb_btn_2 = dialog.findViewById(R.id.rb_btn_2);
                rb_btn_3 = dialog.findViewById(R.id.rb_btn_3);
                rb_btn_4 = dialog.findViewById(R.id.rb_btn_4);
                rb_btn_5 = dialog.findViewById(R.id.rb_btn_5);
                addDialogBtn = dialog.findViewById(R.id.addDialogBtn);

                rb_btn_0.setOnClickListener( rbClick );
                rb_btn_1.setOnClickListener( rbClick );
                rb_btn_2.setOnClickListener( rbClick );
                rb_btn_3.setOnClickListener( rbClick );
                rb_btn_4.setOnClickListener( rbClick );
                rb_btn_5.setOnClickListener( rbClick );
                addDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                radioGroup = dialog.findViewById(R.id.radioGroup);
                radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

                dialog.show();

               // recommendSetAdapter.notifyDataSetChanged();
               // listView.setAdapter(recommendSetAdapter);
            }
        });

        listView = findViewById(R.id.listView);

        recommendSetAdapter = new RecommendSetAdapter(MainActivity.this, R.layout.search_result_item, recommendSetArr, listView);
        listView.setAdapter(recommendSetAdapter);
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Intent i = new Intent(MainActivity.this, SearchActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 1);
                        bundle.putString("query",arrQuery[position]);

                        i.putExtras(bundle);
                        startActivity(i);
                        //Toast.makeText(MainActivity.this,"검색",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete

                        recommendSetArr.remove(position);
                        recommendSetAdapter.notifyDataSetChanged();
                        listView.setAdapter(recommendSetAdapter);
                        bufferedRecommendSetArr.remove(position);
                        bufferedRecommendSetArr.remove(position);

                        recommendSetAdapter.notifyDataSetChanged();
                        listView.setAdapter(recommendSetAdapter);
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

        //장바구니 불러오기

        /*try {
            FileReader fr = new FileReader(Environment.getExternalStorageDirectory()+"/weashion/cart.txt");
            BufferedReader br = new BufferedReader(fr);
            String data = "";

            while((data = br.readLine()) != null){
                loadData += data;
            }

            Log.i("TESTLOAD", loadData);

            if(loadData == ""){
                return;
            }else{
                String[] loadCart = loadData.split(", ");
                Log.i("TESTLOAD", "0" + loadCart[0]);
                Log.i("TESTLOAD", "8" + loadCart[8]);
                Log.i("TESTLOAD", "10" + loadCart[10]);
                Log.i("TESTLOAD", "15" + loadCart[15]);
                Log.i("TESTLOAD", "length : " + loadC+art.length);
                for(int i = 0; i < loadCart.length;){
                    insertData(loadCart[i], loadCart[i+1], loadCart[i+2], loadCart[i+3]);
                    i += 4;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        cartList = findViewById(R.id.cartList);

        myAdapter = new MyAdapter(this, R.layout.activity_resource, list);
        cartList.setAdapter(myAdapter);

        LayoutInflater linf = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        view = linf.inflate(R.layout.activity_resource, null);

        price = view.findViewById(R.id.price);
        category = view.findViewById(R.id.category);

        //항목 클릭 시 상품 정보 페이지로 이동
        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkText));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("여기에 링크 값을 끌어다 넣어야 함"));
                startActivity(intent);
            }
        });

        cartList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.remove(i);
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });

        //---------------날씨 정보-------------------------------------
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

        //사람 쪽 그림 클릭 이벤트
        model_item_0.setOnClickListener(categorys);
        model_item_1.setOnClickListener(categorys);
        model_item_2.setOnClickListener(categorys);
        model_item_3.setOnClickListener(categorys);

    }//onCreate------------------------------------------------------------------


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

    //프로그램이 종료될 때 호출할 수 있도록 수정(우선 주석 처리)
    /*public void fileSave(String text){

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
    } //fileSave()*/

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
            openItem.setTitle("more");

            openItem.setTitle("more");

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
                    /*Glide.with(MainActivity.this).load(goModelImage.get(0)).into(model_item_0);
                    Glide.with(MainActivity.this).load(goModelImage.get(1)).into(model_item_1);
                    Glide.with(MainActivity.this).load(goModelImage.get(2)).into(model_item_2);
                    Glide.with(MainActivity.this).load(goModelImage.get(3)).into(model_item_3);
                    Glide.with(MainActivity.this).load(goModelImage.get(4)).into(model_item_4);*/

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
            switch (v.getId()){
                case R.id.btn_recommend:
                    //여긴 다시 랜덤
                    Toast.makeText(MainActivity.this, "추천하기", Toast.LENGTH_SHORT).show();
                    recommendSetParser = new RecommendSetParser();
                    recommendSetArr = new ArrayList<>();
                    new RecommendSetNaverAsync().execute();

                    new ModelAllActivity(MainActivity.this);

                    break;
                case R.id.btn_setting:
                    SharedPreferences sharedPreferences = getSharedPreferences("SHARE",MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("save",false).commit();
                    new PollActivity(MainActivity.this).setDialog();
                    //여긴 처음 물어본 설정 변경
                    break;
            }
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

    View.OnClickListener rbClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (view.getId()) {
                case R.id.rb_btn_0:
                    radioResult = Util.CATEGORY_HAT;
                    break;
                case R.id.rb_btn_1:
                    radioResult = Util.CATEGORY_TOP;
                    break;
                case R.id.rb_btn_2:
                    radioResult = Util.CATEGORY_BOTTOM;
                    break;
                case R.id.rb_btn_3:
                    radioResult = Util.CATEGORY_SHOES;
                    break;
                case R.id.rb_btn_4:
                    radioResult = Util.CATEGORY_UMB;
                    break;
                case R.id.rb_btn_5:
                    radioResult = Util.CATEGORY_ACC;
                    break;

            }
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_btn_0:
                    radioResult = Util.CATEGORY_HAT;
                    Toast.makeText(MainActivity.this, "0",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_btn_1:
                    radioResult = Util.CATEGORY_TOP;
                    Toast.makeText(MainActivity.this, "1",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_btn_2:
                    radioResult = Util.CATEGORY_BOTTOM;
                    Toast.makeText(MainActivity.this, "2",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_btn_3:
                    radioResult = Util.CATEGORY_SHOES;
                    Toast.makeText(MainActivity.this, "3",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_btn_4:
                    radioResult = Util.CATEGORY_UMB;
                    Toast.makeText(MainActivity.this, "4",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rb_btn_5:
                    radioResult = Util.CATEGORY_ACC;
                    Toast.makeText(MainActivity.this, "5",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static ArrayList<CartVO> insertData(String myImg, String myPrice, String myLink, String myCategory){//받아온 데이터 여기에 담으면 됨

        String price = myPrice;

        //3자리 단위로 , 붙여주기 (현재 적용 안됨)
        /*String price = String.format("%,d", m_price);*/

        String img = myImg;String link = myLink;
        String category = myCategory;

        //링크 호출
        linkText = myLink;
    //---------------cart
    public static ArrayList<CartVO> insertData(String myPrice, String myImg, String myLink, String myCategory){//받아온 데이터 여기에 담으면 됨

        String price = myPrice;
        //String price = String.format("%,d", m_price);
        String img = myImg;
        String link = myLink;
        String category = myCategory;

        Log.i("MY2", "price : " + myPrice);
        Log.i("MY2", "img : " + myImg);
        Log.i("MY2", "link : " + myLink);
        Log.i("MY2", "category : " + myCategory);

        list.add(new CartVO(img, price + "원", link, category));
        cartText += img + ", " + price + ", " + link + ", " + category + ", ";
        myAdapter.notifyDataSetChanged();
        cartText += img + ", " + price + ", " + link + ", " + category + "\n";
        myAdapter.notifyDataSetChanged();

        return list;
    } //insertData()

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
                model_item_4.setVisibility(View.GONE);
                break;
            case "Clear":
                imgWeatherCode = "01d";
                largestBox.setBackgroundResource(R.drawable.clear);
                weatherKR = "맑음";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                model_item_4.setVisibility(View.GONE);
                break;
            case "Rain": case "Drizzle":
                imgWeatherCode = "09d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.rain);
                weatherKR = "비";
                model_item_4.setVisibility(View.VISIBLE);
                break;
            case "Thunderstorm":
                imgWeatherCode = "11d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.thunderstorm);
                weatherKR = "뇌우";
                model_item_4.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"위험한 날입니다. 빠른 귀가를 하십시오.",Toast.LENGTH_SHORT).show();
                break;
            case "Snow":
                imgWeatherCode = "13d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.snow);
                weatherKR = "눈";
                model_item_4.setVisibility(View.VISIBLE);
                break;
            default:
                imgWeatherCode = "50d";
                price.setTextColor(Color.WHITE);
                category.setTextColor(Color.WHITE);
                largestBox.setBackgroundResource(R.drawable.mist);
                weatherKR = "안개";
                model_item_4.setVisibility(View.GONE);
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

    public String settingQuery(int categoryNum) {
        String weather, temp, gender, skin, style;
        String category;
        String queryStr;
        int ranNum;
        Random rd = new Random();

        SharedPreferences shared = getSharedPreferences("SHARE", MODE_PRIVATE);
        weather = shared.getString("weather","");
        temp = shared.getString("temp","");
        gender = shared.getString("gender","");
        style = shared.getString("style","");

        String[] arrHat, arrTop, arrBottom, arrShoes, arrUmb, arrAcc;

        arrHat = new String[]{"cap", "버킷햇", "뉴스보이 캡", "베레모", "비니"};
        arrTop = new String[]{"반팔티", "롱슬리브", "후드티", "셔츠", "맨투맨"};
        arrBottom = new String[]{"숏 팬츠", "와이드 팬츠", "슬랙스", "청바지", "배기팬츠"};
        arrShoes = new String[]{"쪼리", "단화", "shoes", "부츠", "러닝화"};
        /*arrAcc = new String[]{"bracelet", "necklace", "piercing", "backpack", "belt"};*/

        if (Integer.parseInt(temp) > 15){
            ranNum = rd.nextInt(3 - 0 + 1) + 0;
        } else {
            ranNum = rd.nextInt(4 - 1 + 1) + 1;

        }

        switch (categoryNum){
            case Util.CATEGORY_HAT:
                category = arrHat[ranNum];

                arrQuery[Util.CATEGORY_HAT] = category;
                break;
            case Util.CATEGORY_TOP:
                category = arrTop[ranNum];
                arrQuery[Util.CATEGORY_TOP] = category;
                break;
            case Util.CATEGORY_BOTTOM:
                category = arrBottom[ranNum];
                arrQuery[Util.CATEGORY_BOTTOM] = category;
                break;
            case Util.CATEGORY_SHOES:
                category = arrShoes[ranNum];
                arrQuery[Util.CATEGORY_SHOES] = category;
                break;
            case Util.CATEGORY_UMB:
                category = "umbrella";
                arrQuery[Util.CATEGORY_UMB] = category;
                break;
            /*case Util.CATEGORY_ACC:
                category = arrAcc[ranNum];
                arrQuery[Util.CATEGORY_ACC] = category;
                break;*/

            default:
                category= "";
                break;
        }

        switch (weather){
            case "Clouds":

                break;
            case "Clear":

                break;
            case "Rain": case "Drizzle":

                break;
            case "Thunderstorm":

                break;
            case "Snow":

                break;
            default://안개

                break;
        }

        if (category.equals("umbrella")){
            queryStr = category;
        } else if ((categoryNum == Util.CATEGORY_TOP || categoryNum == Util.CATEGORY_BOTTOM) && style.equals("스트릿 ")){
            queryStr = style + category;
        } else {
            queryStr = category;
        }

        return queryStr;
    }

    class RecommendSetNaverAsync extends AsyncTask<String , Void, ArrayList<SearchVO>> {

        @Override
        protected ArrayList<SearchVO> doInBackground(String... strings) {

            if( startCount == 0 ){
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_HAT), Util.CATEGORY_HAT));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_TOP), Util.CATEGORY_TOP));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_BOTTOM), Util.CATEGORY_BOTTOM));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_SHOES), Util.CATEGORY_SHOES));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_UMB), Util.CATEGORY_UMB));
                startCount++;
                bufferedRecommendSetArr.addAll(recommendSetArr);
            } else {
                for (int i = 0; i < bufferedRecommendSetArr.size(); i++){
                    int removeNum = bufferedRecommendSetArr.get(i).getType()-i;
                    if (i+removeNum == bufferedRecommendSetArr.get(i).getType()){
                        recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(i+removeNum), i+removeNum));
                    }
                }
                bufferedRecommendSetArr.clear();
                bufferedRecommendSetArr.addAll(recommendSetArr);
            }

            if( startCount == 0 ){
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_HAT), Util.CATEGORY_HAT));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_TOP), Util.CATEGORY_TOP));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_BOTTOM), Util.CATEGORY_BOTTOM));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_SHOES), Util.CATEGORY_SHOES));
                recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(Util.CATEGORY_UMB), Util.CATEGORY_UMB));
                startCount++;
                bufferedRecommendSetArr.addAll(recommendSetArr);
            } else {
                for (int i = 0; i < bufferedRecommendSetArr.size(); i++){
                    int removeNum = bufferedRecommendSetArr.get(i).getType()-i;
                    if (i+removeNum == bufferedRecommendSetArr.get(i).getType()){
                        recommendSetArr.add(recommendSetParser.RecommendSetParser(settingQuery(i+removeNum), i+removeNum));
                    }
                }
                bufferedRecommendSetArr.clear();
                bufferedRecommendSetArr.addAll(recommendSetArr);
            }


            /*recommendSetArr.add(recommendSetParser.RecommendSetParser("street shoes", Util.CATEGORY_SHOES));*/
            return recommendSetArr;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchVO> searchVOS) {
            recommendSetAdapter = new RecommendSetAdapter(MainActivity.this, R.layout.search_result_item, searchVOS, listView);
            listView.setAdapter(recommendSetAdapter);


            recommendSetAdapter = new RecommendSetAdapter(MainActivity.this, R.layout.search_result_item, searchVOS, listView);
            listView.setAdapter(recommendSetAdapter);
            /*recommendSetAdapter.notifyDataSetChanged();*/
        }
    }

    //item 선택
    View.OnClickListener categorys = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.hat:
                    new Hat(MainActivity.this);
                    break;
                case R.id.top:
                    new Top(MainActivity.this);
                    break;
                case R.id.bottom:
                    new Bottom(MainActivity.this);
                    break;
                case R.id.shoes:
                    new Shoes(MainActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        sensorM.registerListener(sensorL, acc_sensor, sensorM.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onStop() {
        sensorM.unregisterListener(sensorL);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        fileSave(cartText);
        super.onDestroy();
    }

    void fileSave(String text){
    public void fileSave(String text){

        File f = new File(Environment.getExternalStorageDirectory()+"/weashion/");
        if(!f.exists()){
            f.mkdirs();
        }
        File f2 = new File(f,"cart.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f2, true);
            fos.write((text).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();

        File f2 = new File(f,  "cart.txt");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(f2, true);
            fos.write((text).getBytes());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
