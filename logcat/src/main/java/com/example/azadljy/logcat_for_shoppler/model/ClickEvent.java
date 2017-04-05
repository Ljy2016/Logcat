package com.example.azadljy.logcat_for_shoppler.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.View;

import com.example.azadljy.logcat_for_shoppler.LogException;
import com.example.azadljy.logcat_for_shoppler.R;
import com.example.azadljy.logcat_for_shoppler.adapter.LogAdapter;
import com.example.azadljy.logcat_for_shoppler.adapter.LogModelDiffCallback;

import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 控件的点击事件，因为事件较少，所以统一放置一起，
 * 如果有大量事件，还是应该按模块或页面分别管理
 * ViewModel的雏形
 * 作者：Ljy on 2017/3/3.
 * 邮箱：enjoy_azad@sina.com
 */
public class ClickEvent {
    public final String dTime = "显示时间";
    public final String hTime = "隐藏时间";
    public static Boolean isRunning = false;
    private RecyclerView recyclerView;
    private AsyncTask task;
    private boolean isDisplayTime;
    private Activity context;
    private String logCommand = "logcat -v raw";

    public String getLogCommand() {
        return logCommand;
    }

    public void setLogCommand(String logCommand) {
        this.logCommand = logCommand;
    }

    public void setDisplayTime(boolean displayTime) {
        isDisplayTime = displayTime;
    }

    public boolean isDisplayTime() {
        return isDisplayTime;
    }

    List<LogModel> logModels;
    LogAdapter adapter;
    private Handler handler;

    public ClickEvent(RecyclerView recyclerView, Activity context) {
        logModels = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.context = context;
    }

    /**
     * AsyncTask不能重复执行，即使调用cancle也可能不会立即停止
     * 导致退出后，再次执行会无效，同时也会导致内存溢出
     *
     * @param view
     */
    public void startLogByAsyncTask(View view) {
        if (!isRunning && adapter != null) {

            isRunning = true;
            Log.e("TAG", "startLog:开始 ");
            task = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        //创建process，传入命令，打印所需要的日志信息
//                        Process process = Runtime.getRuntime().exec("logcat -v time  HttpRequest:E *:S ");
                        Process process = Runtime.getRuntime().exec(logCommand);
                        InputStream is = process.getInputStream();
                        InputStreamReader reader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        Log.e("TAG", "startLog:准备输出日志 ");
                        while ((line = bufferedReader.readLine()) != null && isRunning) {
                            publishProgress(line);
                        }
                        Log.e("TAG", "startLog：不符合运行条件 ");
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", "startLog：异常 " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    String line = values[0];
                    //根据传来的日志获取时间，须对应格式，普适性不强
//                    String time = line.substring(6, 14);
                    LogModel model = new LogModel();
//                    model.setLogTime(time);
                    model.setLogContent(line);
                    if (line.contains("异常信息")) {
                        model.setError(true);
                    }
                    logModels.add(model);
                    adapter.notifyDataSetChanged();
                    //recyclerView.smoothScrollToPosition(logModels.size()-1);
                }
            }.execute();
        }
    }

    //取消asyncTask，但是当onProgressUpdate有任务执行的时候，不会立即停下来，暂时废弃asyncTask
    public void cancleTask() {
        if (task != null) {
            task.cancel(true);
        }
    }

    /**
     * 新开线程，调用runOnUiThread方法与主线程通信
     * 暂时还好
     *
     * @param view
     */
    public void startLogByThread(View view) {
        if (!isRunning && adapter != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //创建process，传入命令，打印所需要的日志信息
//                        Process process = Runtime.getRuntime().exec("logcat -v time  HttpRequest:E *:S ");
                        Process process = Runtime.getRuntime().exec(logCommand);
                        InputStream is = process.getInputStream();
                        InputStreamReader reader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        Log.e("TAG", "startLog:准备输出日志 ");
                        isRunning = true;
                        synchronized (isRunning) {
                            while ((line = bufferedReader.readLine()) != null && isRunning) {
//                              String time = line.substring(6, 14);
                                LogModel model = new LogModel();
//                              model.setLogTime(time);
//                              model.setLogContent(line.substring(line.indexOf("):") + 2));
                                model.setLogContent(line);
                                if (line.contains("异常信息")) {
                                    model.setError(true);
                                }
                                logModels.add(model);
//                              final LogModelDiffCallback diffCallback = new LogModelDiffCallback(adapter.getLogModels(), logModels);
//                              final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//                              adapter.getLogModels().clear();
//                              adapter.getLogModels().addAll(logModels);

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemInserted(logModels.size() - 1);
//                                      diffResult.dispatchUpdatesTo(adapter);
                                    }
                                });
                            }
                        }
                        Log.e("TAG", "startLog：不符合运行条件 ");
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", "startLog：异常 " + e.getMessage());
                    }
                }
            }).start();
        }
    }

    //是否显示时间
    public void showLogTime(View view) {
        if (adapter != null) {
            adapter.setDispalyLogTime(isDisplayTime = !isDisplayTime);
        }
    }

    //初始化adapter
    public LogAdapter getAdapter() {
        if (adapter == null) {
            adapter = new LogAdapter(logModels) {
                @Override
                public int setItemViewType(int position) {
                    return R.layout.logitem;
                }

            };
        }
        return adapter;
    }

    //滑到底部
    public void toTheBottom(View view) {
        if (recyclerView != null && logModels.size() != 0) {
            recyclerView.smoothScrollToPosition(logModels.size() - 1);
        }
    }

    //清空日志
    public void clearLog(View view) {
        if (logModels.size() > 0) {
            logModels.clear();
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    public void changeLog(View view) {
        isRunning = false;
        logCommand = "logcat -v time";
    }

}
