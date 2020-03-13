package com.alirnp.alichtimerview.TimeView;

public interface OnSeekCirclesListener {
    void OnSeekChange(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute);

    void OnSeekComplete(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute);
}

