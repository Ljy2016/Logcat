package com.example.azadljy.logcat_for_shoppler.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.azadljy.logcat_for_shoppler.databinding.LogitemBinding;
import com.example.azadljy.logcat_for_shoppler.model.LogModel;


/**
 * 作者：Ljy on 2017/3/1.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogViewHolder extends RecyclerView.ViewHolder {

    private final LogitemBinding binding;

    public LogViewHolder(LogitemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * 绑定数据  并立即调用executePendingBindings（）方法，使RecyclerView
     * 可以在绑定viewholder后立即测量该view，防止数据错误而导致的binding等待，使得测量结果不准确。
     *
     * @param model  数据源
     */
    public void bind(LogModel model) {
        binding.setLog(model);
        binding.executePendingBindings();
    }

}
