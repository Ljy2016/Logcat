package com.example.azadljy.logcatutil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

import com.example.azadljy.logcatutil.adapter.LogAdapter;
import com.example.azadljy.logcatutil.databinding.ActivityLogBinding;
import com.example.azadljy.logcatutil.model.LogModel;
import com.example.azadljy.logcatutil.model.LogcatViewModel;


/**
 * 作者：Ljy on 2017/2/24.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogActivity extends AppCompatActivity implements LogcatViewModel.LogManager {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private Spinner spinner;
    LogcatViewModel logcatViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ActivityLogBinding logBinding = DataBindingUtil.setContentView(this, R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.rl_showlog);
        spinner = (Spinner) findViewById(R.id.sp_logType);
        logcatViewModel = new LogcatViewModel(recyclerView, this, spinner);
        logcatViewModel.setManager(this);
        adapter = logcatViewModel.getAdapter();
        logBinding.setLogcatViewModel(logcatViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutAnimation(null);
        recyclerView.setItemAnimator(null);
    }

    @Override
    protected void onDestroy() {
        LogcatViewModel.isRunning = false;
        super.onDestroy();
    }

    public static String currentColor = "#000000";

    @Override
    public void manage(String log, LogModel model) {
        if (log.contains("V/")) {
            currentColor = "#000000";
        }
        if (log.contains("D/")) {
            currentColor = "#3cba54";
        }
        if (log.contains("I/")) {
            currentColor = "#4885ed";
        }
        if (log.contains("W/")) {
            currentColor = "#f4c20d";
        }
        if (log.contains("E/")) {
            currentColor = "#db3236";
        }
        if (log.contains("天冷涂的蜡")) {
            model.setSpecialInfo(true);
            currentColor = "#00FFFF";
        }
        if (("MyCommand").equals(LogcatViewModel.currentLogCommand)) {
            String time = log.substring(6, 14);
            model.setLogTime(time);
            model.setLogContent(log.substring(log.indexOf("):") + 2));
        }
        model.setLogColor(currentColor);

    }
}