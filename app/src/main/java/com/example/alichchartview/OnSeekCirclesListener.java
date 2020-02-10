package com.example.alichchartview;

public interface OnSeekCirclesListener {
    void OnSeekChangeStartHour(int hour, int minute);

    void OnSeekChangeEndHour(int hour, int minute);

    void OnSeekChangeRepeat(int hour, int minute);
}
