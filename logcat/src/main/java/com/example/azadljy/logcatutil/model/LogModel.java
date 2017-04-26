package com.example.azadljy.logcatutil.model;

import android.graphics.Color;

/**
 * 作者：Ljy on 2017/2/28.
 * 邮箱：enjoy_azad@sina.com
 */

public class LogModel {
    private String logTime;
    private String logContent;
    /**
     * Add a flag for this model
     */
    private boolean isSpecialInfo;
    private boolean isShowTime;
    private int logColor = Color.BLACK;
    private int logTimeColor = Color.BLACK;
    private int id;

    public int getLogColor() {
        return logColor;
    }

    /**
     * Set the log text color,default:"#000000"
     *
     * @param logColor
     */
    public void setLogColor(String logColor) {
        try {
            this.logColor = Color.parseColor(logColor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.logColor = Color.BLACK;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSpecialInfo() {
        return isSpecialInfo;
    }

    public void setSpecialInfo(boolean specialInfo) {
        isSpecialInfo = specialInfo;
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

    public boolean isShowTime() {
        return isShowTime;
    }

    public void setShowTime(boolean showTime) {
        isShowTime = showTime;
    }

    public int getLogTimeColor() {
        return logTimeColor;
    }

    public void setLogTimeColor(String logTimeColor) {
        try {
            this.logTimeColor = Color.parseColor(logTimeColor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.logTimeColor = Color.BLACK;
        }
    }


}
