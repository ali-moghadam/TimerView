package com.example.alichchartview;

public class Utils {
    public static String addZeroBeforeTime(int time) {
        if (time <= 9)
            return "0" + time;
        else
            return String.valueOf(time);
    }

    public static String addZeroBeforeTime(String time) {
        int timeInt = Integer.parseInt(time);
        if (timeInt <= 9)
            return "0" + timeInt;
        else
            return time;
    }
}
