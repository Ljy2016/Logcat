package com.example.azadljy.logcatutil.model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.azadljy.logcatutil.R;
import com.example.azadljy.logcatutil.adapter.LogAdapter;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：Ljy on 2017/3/3.
 * 邮箱：enjoy_azad@sina.com
 */
public class LogcatViewModel {
    public final String dTime = "显示时间";
    public final String hTime = "隐藏时间";
    public static Boolean isRunning = false;
    private RecyclerView recyclerView;
    private AsyncTask task;
    private boolean isDisplayTime;
    private Activity context;
    private boolean isCleared;
    private boolean isWaiting;
    private String logCommand = "logcat -v raw";
    private ArrayAdapter<String> myAdapter;
    private List<String> spinnerList;
    private Spinner spinner;
    private List<LogModel> logModels;
    private LogAdapter adapter;
    private Handler handler;
    private Map<String, String> commandInfo;

    public LogcatViewModel(RecyclerView recyclerView, Activity context, Spinner spinner) {
        logModels = new ArrayList<>();
        spinnerList = new ArrayList<>();
        commandInfo = new HashMap<>();
        spinnerList.add("Verbose");
        spinnerList.add("Debug");
        spinnerList.add("Info");
        spinnerList.add("Warn");
        spinnerList.add("Error");
        commandInfo.put("Verbose", "logcat -v long *:V");
        commandInfo.put("Debug", "logcat -v long *:D");
        commandInfo.put("Info", "logcat -v long *:I");
        commandInfo.put("Warn", "logcat -v long *:W");
        commandInfo.put("Error", "logcat -v long *:E");
        this.spinner = spinner;
        this.recyclerView = recyclerView;
        this.context = context;
        loadDataForSpinner(spinner);
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
//                      Process process = Runtime.getRuntime().exec("logcat -v time  HttpRequest:E *:S ");
                        Process process = Runtime.getRuntime().exec(logCommand);
                        InputStream is = process.getInputStream();
                        InputStreamReader reader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        Log.e("TAG", "startLog:准备输出日志 ");
                        isRunning = true;
                        synchronized (isRunning) {
                            while ((line = bufferedReader.readLine()) != null && isRunning) {
                                LogModel model = new LogModel();
                                model.setLogContent(line);
                                if (line.contains("异常信息")) {
                                    model.setError(true);
                                }
                                logModels.add(model);
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isCleared) {
                                            logModels.clear();
                                            adapter.notifyDataSetChanged();
                                            isCleared = false;
                                        } else if (!isWaiting) {
                                            adapter.notifyItemInserted(logModels.size() - 1);
                                        }
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
            isCleared = true;
        }
    }


    public void changeLog(String commandKey) {
        isRunning = false;
        logCommand = commandInfo.get(commandKey);
        clearLog(null);
        startLogByThread(null);
    }


    private void loadDataForSpinner(final Spinner spinner) {
        myAdapter = new ArrayAdapter<>(context, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, spinnerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                changeLog(spinnerList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

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
}
