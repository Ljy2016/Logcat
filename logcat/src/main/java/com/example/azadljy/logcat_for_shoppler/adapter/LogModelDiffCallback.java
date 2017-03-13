package com.example.azadljy.logcat_for_shoppler.adapter;

import android.support.v7.util.DiffUtil;

import com.example.azadljy.logcat_for_shoppler.model.LogModel;

import java.util.List;

/**
 * 作者：Ljy on 2017/3/13.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogModelDiffCallback extends DiffUtil.Callback {
    private final List<LogModel> oldList;
    private final List<LogModel> newList;

    public LogModelDiffCallback(List<LogModel> oldList, List<LogModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
