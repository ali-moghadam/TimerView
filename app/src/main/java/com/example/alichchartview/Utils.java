package com.example.alichchartview;

public class Utils {
    public static String addZeroBeforeTime(int time) {
        if (time <= 9)
            return "0" + time;
        else
            return String.valueOf(time);
    }
}
