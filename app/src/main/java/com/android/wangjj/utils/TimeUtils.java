package com.android.wangjj.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class TimeUtils {

//    yyyy:MM:dd HH:mm:ss

    /**
     * 获取当前时间:时、分
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年、月、日
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年、月、日
     *
     * @return
     */
    public static String getCurrentNumDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年、月、日
     *
     * @return
     */
    public static String getCurrentSSSDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年、月、日
     *
     * @return
     */
    public static String getCurrentFormaDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年
     *
     * @return
     */
    public static String getCurrentYearDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年
     *
     * @return
     */
    public static String getCurrentYearDateNoYeaer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期:年
     *
     * @return
     */
    public static String getCurrentYearNumDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 日期解析
     *
     * @return
     */
    public static String parseDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        return sdf.format(new Date((time)));
    }

    /**
     * 日期解析
     *
     * @return
     */
    public static String parseDate(Date time) {
        if (time == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
            return sdf.format(time);
        }
    }

    public static long strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos).getTime();
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}