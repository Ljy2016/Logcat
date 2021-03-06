package com.example.azadljy.logcatutil.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.azadljy.logcatutil.databinding.LogitemBinding;
import com.example.azadljy.logcatutil.model.LogModel;

import android.support.v7.util.DiffUtil;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/3/1.
 * 邮箱：enjoy_azad@sina.com
 */

public abstract class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
    private List<LogModel> logModels;
    List<LogitemBinding> logitemBindings;

    public LogAdapter(List<LogModel> logModels) {
        logitemBindings = new ArrayList<>();
        this.logModels = logModels;

    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LogitemBinding logitemBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        logitemBindings.add(logitemBinding);
        return new LogViewHolder(logitemBinding);

    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        LogModel logModel = logModels.get(position);
        holder.bind(logModel);
    }

    @Override
    public int getItemViewType(int position) {
        return setItemViewType(position);
    }


    //获得item的布局
    public abstract int setItemViewType(int position);

    @Override
    public int getItemCount() {
        return logModels.size();
    }

}
