package com.example.azadljy.logcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "logtest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void printLog(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                        Log.v(TAG, "天王盖地虎！");
                        Log.d(TAG, "宝塔镇河妖！");
                        Log.i(TAG, "脸红什么？");
                        Log.w(TAG, "精神焕发！");
                        Log.e(TAG, "怎么又黄了？天冷涂的蜡！");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
