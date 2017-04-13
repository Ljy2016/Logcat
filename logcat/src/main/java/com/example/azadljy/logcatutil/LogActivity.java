package com.example.azadljy.logcatutil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.azadljy.logcatutil.adapter.LogAdapter;
import com.example.azadljy.logcatutil.databinding.ActivityLogBinding;
import com.example.azadljy.logcatutil.model.ClickEvent;


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
        recyclerView.setLayoutAnimation(null);
        recyclerView.setItemAnimator(null);
    }

    @Override
    protected void onDestroy() {
        ClickEvent.isRunning = false;
        super.onDestroy();
    }

}