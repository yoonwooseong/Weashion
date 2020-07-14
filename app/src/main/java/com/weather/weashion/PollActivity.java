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
    final CharSequence[] items2 = {"웜톤","쿨톤"};
    final CharSequence[] items3 = {"캐주얼","스트릿","미니멀","아메카지"," "};

    AlertDialog.Builder dialog;
    SharedPreferences shared ;
    static boolean mycheck = false;
    Context context;
    private boolean mycheck = false;
    private Context context;
    MainActivity.RecommendSetNaverAsync recommendSetNaverAsync;

    public PollActivity(Context context, MainActivity.RecommendSetNaverAsync recommendSetNaverAsync) {
        super(context);
        this.context = context;
        this.recommendSetNaverAsync = recommendSetNaverAsync;
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
                    dialog.setTitle("당신의 피부톤은?").setItems(items2, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int index) {
                            Toast.makeText(context.getApplicationContext(), items2[index], Toast.LENGTH_SHORT).show();
                            String str2 = (String) items2[index];
                            shared.edit().putString("skin", str2);
                            dialog.setTitle("자주 입는 스타일은?").setItems(items3, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    Toast.makeText(context.getApplicationContext(), items3[index], Toast.LENGTH_SHORT).show();
                                    String str3 = (String) items3[index];
                                    shared.edit().putString("style", str3);
                                    dialog.setTitle("다시 보시겠습니까?").setItems(null, new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) { }
                                    });
                                    dialog.setPositiveButton("다시보지않기", click);
                                    dialog.setNegativeButton("다시보기", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) { }
                                    dialog.setTitle("다시 보겠습니까?").setItems(null, new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            /*new MainActivity.RecommendSetNaverAsync().execute();*/

                                        }
                                    });
                                    dialog.setPositiveButton("다시보지않기", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context.getApplicationContext(), "hide", Toast.LENGTH_SHORT).show();
                                            mycheck = shared.edit().putBoolean("save", true).commit();
                                            /*recommendSetNaverAsync.execute();*/

                                        }
                                    });
                                    dialog.setNegativeButton("다시보기", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context.getApplicationContext(), "show", Toast.LENGTH_SHORT).show();
                                            /*recommendSetNaverAsync.execute();*/

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

}


