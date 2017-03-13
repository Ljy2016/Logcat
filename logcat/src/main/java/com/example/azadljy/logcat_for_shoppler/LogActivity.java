package com.example.azadljy.logcat_for_shoppler;

import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azadljy.logcat_for_shoppler.adapter.LogAdapter;
import com.example.azadljy.logcat_for_shoppler.databinding.ActivityLogBinding;
import com.example.azadljy.logcat_for_shoppler.model.ClickEvent;
import com.example.azadljy.logcat_for_shoppler.model.LogModel;
import com.github.clans.fab.FloatingActionMenu;
import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/2/24.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    boolean isDisplayLogTime;
    ClickEvent clickEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ActivityLogBinding logBinding = DataBindingUtil.setContentView(this, R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.rl_showlog);
        clickEvent = new ClickEvent(recyclerView, this);
        adapter = clickEvent.getAdapter();
        logBinding.setClickEvent(clickEvent);
        adapter.setDispalyLogTime(isDisplayLogTime);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        ClickEvent.isRunning = false;
        super.onDestroy();
    }


//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "返回键暂时会有问题，home键退出", Toast.LENGTH_SHORT).show();
//    }
}