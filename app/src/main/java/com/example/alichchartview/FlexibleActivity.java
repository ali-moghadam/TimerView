package com.example.alichchartview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alirnp.alichtimerview.AliChTimerView;
import com.alirnp.alichtimerview.OnSeekCirclesListener;
import com.example.alichchartview.Need.LockableScrollView;

public class FlexibleActivity extends AppCompatActivity {


    private LockableScrollView mLockableScrollView;
    private AliChTimerView mAliChTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexable);

        findViews();
        setListener();
    }

    private void setListener() {
        mAliChTimerView.setOnMoveListener(moving -> {
            if (moving)
                mLockableScrollView.setScrollingEnabled(false);
        });

        mAliChTimerView.setOnSeekCirclesListener(new OnSeekCirclesListener() {
            @Override
            public void OnSeekChange(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute) {

            }

            @Override
            public void OnSeekComplete(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute) {
                mLockableScrollView.setScrollingEnabled(true);
            }
        });
    }

    private void findViews() {
        mLockableScrollView = findViewById(R.id.flexible_scrollView);
        mAliChTimerView = findViewById(R.id.flexible_aliTimerViewIndicator);
    }
}
