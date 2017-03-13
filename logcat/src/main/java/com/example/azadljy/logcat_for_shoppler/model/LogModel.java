package com.example.azadljy.logcat_for_shoppler.model;

/**
 * 作者：Ljy on 2017/2/28.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogModel {
    private String logTime;
    private String logContent;
    private boolean isError;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }


    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }


}
