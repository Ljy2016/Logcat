package com.example.azadljy.logcat_for_shoppler;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.azadljy.logcat_for_shoppler.adapter.LogAdapter;
import com.example.azadljy.logcat_for_shoppler.databinding.ActivityLogBinding;
import com.example.azadljy.logcat_for_shoppler.model.ClickEvent;


/**
 * 作者：Ljy on 2017/2/24.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private LottieAnimationView animationView;
    boolean isDisplayLogTime;
    ClickEvent clickEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ActivityLogBinding logBinding = DataBindingUtil.setContentView(this, R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.rl_showlog);
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setProgress(0.5f);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.playAnimation();
            }
        });
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