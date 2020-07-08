package com.weather.weashion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class PollActivity extends Dialog {
    final CharSequence[] items = {"남자","여자"};
    final CharSequence[] items2 = {"봄","여름","가을","겨울"};
    final CharSequence[] items3 = {"캐주얼","힙합","인기상품","생각없음"};
    AlertDialog.Builder dialog;
    SharedPreferences shared ;
    boolean mycheck = false;
    Context context;

    public PollActivity(Context context) {
        super(context);
        this.context = context;
        shared = (SharedPreferences)context.getSharedPreferences("SHARE", Context.MODE_PRIVATE);
    }

    public void setDialog() {
        dialog = new AlertDialog.Builder(context);

        if (shared.getBoolean("save", mycheck) != true) {
            dialog.setTitle("당신의 성별은?").setItems(items, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    Toast.makeText(context.getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
                    String str = (String) items[index];
                    Log.i("MY", "" + str + "를 선택하셧습니다. 이후 내용은 생략");
                    shared.edit().putString("gender", str);
                    dialog.setTitle("선호하는 계절은?").setItems(items2, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int index) {
                            Toast.makeText(context.getApplicationContext(), items2[index], Toast.LENGTH_SHORT).show();
                            String str2 = (String) items2[index];
                            shared.edit().putString("season", str2);
                            dialog.setTitle("자주 입는 스타일은?").setItems(items3, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    Toast.makeText(context.getApplicationContext(), items3[index], Toast.LENGTH_SHORT).show();
                                    String str3 = (String) items3[index];
                                    shared.edit().putString("style", str3);
                                }
                            });
                            dialog.setPositiveButton("다시보지않기", click);
                            dialog.setCancelable(false);
                            dialog.create();
                            dialog.show();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create();
                    dialog.show();
                }
            });
            dialog.setCancelable(false);
            dialog.create();
            dialog.show();
        }
    }

    //마지막 다시보기 체크.
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(context.getApplicationContext(),"hide",Toast.LENGTH_SHORT).show();
            mycheck = shared.edit().putBoolean("save",true).commit();
        }
    };
}

