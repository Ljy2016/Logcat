package com.example.azadljy.logcatutil.model;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.example.azadljy.logcatutil.R;
import com.example.azadljy.logcatutil.adapter.LogAdapter;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
    public static Boolean isRunning = false;
    public static String currentLogCommand = "Verbose";
    private RecyclerView recyclerView;
    private Activity context;
    private String logCommand = "logcat -v long *:V";
    private ArrayAdapter<String> myAdapter;
    private List<String> spinnerList;
    private List<LogModel> logModels;
    private LogAdapter adapter;


    private Map<String, String> commandInfo;//key:command name   value:command
    private LogThread logThread;


    public LogcatViewModel(RecyclerView recyclerView, Activity context, Spinner spinner) {
        logModels = new ArrayList<>();
        spinnerList = new ArrayList<>();
        commandInfo = new HashMap<>();
        //add common types to the spinner
        spinnerList.add("Verbose");
        spinnerList.add("Debug");
        spinnerList.add("Info");
        spinnerList.add("Warn");
        spinnerList.add("Error");
        spinnerList.add("MyCommand");
        //add common commands to the commandInfo
        commandInfo.put("Verbose", "logcat -v long *:V");
        commandInfo.put("Debug", "logcat -v long *:D");
        commandInfo.put("Info", "logcat -v long *:I");
        commandInfo.put("Warn", "logcat -v long *:W");
        commandInfo.put("Error", "logcat -v long *:E");
        commandInfo.put("MyCommand", "logcat -v time  logTest:V *:S ");
        this.recyclerView = recyclerView;
        this.context = context;
        loadDataForSpinner(spinner);
    }

    /***
     * Interrupt old thread and start a new one,
     * the interrupted thread will stop in the next cycle.
     *
     * @param view Use for view click event
     */
    public void startShowLog(View view) {
        if (null != logThread) {
            logThread.interrupt();
        }
        logThread = new LogThread(logCommand);
        logThread.start();
    }

    /***
     * Init recyclerView adapter
     *
     * @return
     */
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


    public void toTheBottom(View view) {
        if (recyclerView != null && logModels.size() != 0) {
            recyclerView.smoothScrollToPosition(logModels.size() - 1);
        }
    }


    public void clearLog(View view) {
        if (logModels.size() > 0) {
            logModels.clear();
            adapter.notifyDataSetChanged();
        }
    }

    /***
     * Change command and start a new thread.
     *
     * @param logCommand
     */
    public void changeCommand(String logCommand) {
        this.logCommand = logCommand;
        clearLog(null);
        startShowLog(null);
    }

    /***
     * Init the spinner
     *
     * @param spinner
     */
    private void loadDataForSpinner(final Spinner spinner) {
        myAdapter = new ArrayAdapter<>(context, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, spinnerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currentLogCommand = spinnerList.get(position);
                changeCommandFromCommandInfo(currentLogCommand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Get command from the Map:commandInfo according to the commandKey,
     * clear screen and start a new thread.
     *
     * @param commandKey
     */
    private void changeCommandFromCommandInfo(String commandKey) {
        logCommand = commandInfo.get(commandKey);
        if (!TextUtils.isEmpty(logCommand)) {
            clearLog(null);
            startShowLog(null);
        } else {
            Toast.makeText(context, "无法获得logCommand", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manage the logModel according to your needs
     */
    private LogManager manager;

    public void setManager(LogManager manager) {
        this.manager = manager;
    }

    /**
     * Get log info from stream and show it immediately,
     * when the thread was interrupted,it break while() and stop.
     */
    class LogThread extends Thread {
        Process process;
        InputStream is;
        InputStreamReader reader;
        BufferedReader bufferedReader;

        public LogThread(String command) {
            try {
                process = Runtime.getRuntime().exec(command);
                is = process.getInputStream();
                reader = new InputStreamReader(is);
                bufferedReader = new BufferedReader(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null && !isInterrupted()) {
                    LogModel model = new LogModel();
                    model.setLogContent(line);
                    if (manager != null) {
                        manager.manage(line, model);
                    }
                    logModels.add(model);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (logModels.size() > 0) {
                                adapter.notifyItemInserted(logModels.size() - 1);
                            }
                        }
                    });
                }
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
            }
        }
    }

    /**
     * The
     */
    public interface LogManager {
        void manage(String log, LogModel model);
    }

}
