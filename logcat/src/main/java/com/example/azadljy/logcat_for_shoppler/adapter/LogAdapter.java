package com.example.azadljy.logcat_for_shoppler.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.azadljy.logcat_for_shoppler.databinding.LogitemBinding;
import com.example.azadljy.logcat_for_shoppler.model.LogModel;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/3/1.
 * 邮箱：enjoy_azad@sina.com
 */

public abstract class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

    List<LogitemBinding> logitemBindings;

    public List<LogModel> getLogModels() {
        return logModels;
    }

    private List<LogModel> logModels;

    public LogAdapter(List<LogModel> newLogModels) {
        logitemBindings = new ArrayList<>();
        logModels = new ArrayList<>();
        logModels.addAll(newLogModels);
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
        LogModel logModel = getModel(position);
        holder.bind(logModel);
    }

    @Override
    public int getItemViewType(int position) {
        return setItemViewType(position);
    }

    //从外部获得数据
    public LogModel getModel(int position) {
        return logModels.get(position);
    }

    //获得item的布局
    public abstract int setItemViewType(int position);

    @Override
    public int getItemCount() {
        return logModels.size();
    }

    public void update(List<LogModel> newLogModels) {
        final LogModelDiffCallback diffCallback = new LogModelDiffCallback(this.logModels, newLogModels);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.logModels.clear();
        this.logModels.addAll(newLogModels);
        diffResult.dispatchUpdatesTo(this);
    }

    public void update1(DiffUtil.DiffResult diffResult) {

        diffResult.dispatchUpdatesTo(this);
    }

    //设置是否显示日志的时间信息
    public void setDispalyLogTime(boolean dispalyLogTime) {

        for (LogitemBinding binding : logitemBindings) {
            if (binding != null) {
                binding.setIsDisplayTime(dispalyLogTime);
            }
        }

    }
}
