package com.alirnp.alichtimerview;

public interface OnSeekCirclesListener {
    void OnSeekChange(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute);

    void OnSeekComplete(AliChTimerView.CircleID circleID, int hour, int minute);
}

