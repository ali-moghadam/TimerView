package com.example.alichchartview;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alirnp.alichtimerview.TimeView.AliChTimerView;
import com.alirnp.alichtimerview.TimeView.OnSeekCirclesListener;
import com.example.alichchartview.Need.LockableScrollView;

public class FlexibleActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    private LockableScrollView mLockableScrollView;
    private AliChTimerView mAliChTimerView;

    private TextView mTextViewStartHour;
    private TextView mTextViewStartMinute;
    private TextView mTextViewStartAmPM;
    private TextView mTextViewEndHour;
    private TextView mTextViewEndMinute;
    private TextView mTextViewEndAmPM;
    private TextView mTextViewRepeatHour;
    private TextView mTextViewRepeatMinute;

    private SwitchCompat mSwitch;
    private ConstraintLayout mConstraintLayoutRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexable);

        findViews();
        setListener();
        config();
    }

    private void config() {
        visibleRepeatItems(false);
    }

    private void setListener() {
        mAliChTimerView.setOnMoveListener(moving -> {
            if (moving)
                mLockableScrollView.setScrollingEnabled(false);
        });

        mSwitch.setOnCheckedChangeListener(this);

        mAliChTimerView.setOnSeekCirclesListener(new OnSeekCirclesListener() {
            @Override
            public void OnSeekChange(AliChTimerView.CircleID circleID, AliChTimerView.AM_PM am_pm, int hour, int minute) {

                String hourText = Utils.addZeroBeforeTime(hour);
                String minuteText = Utils.addZeroBeforeTime(minute);

                switch (circleID) {

                    case CIRCLE_START_TIME:
                        mTextViewStartHour.setText(hourText);
                        mTextViewStartMinute.setText(minuteText);
                        mTextViewStartAmPM.setText(am_pm == AliChTimerView.AM_PM.AM ? "AM" : "PM");
                        break;

                    case CIRCLE_END_TIME:
                        mTextViewEndHour.setText(hourText);
                        mTextViewEndMinute.setText(minuteText);
                        mTextViewEndAmPM.setText(am_pm == AliChTimerView.AM_PM.AM ? "AM" : "PM");
                        break;


                    case CIRCLE_REPEAT_TIME:
                        mTextViewRepeatHour.setText(hourText);
                        mTextViewRepeatMinute.setText(minuteText);
                        break;
                }
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
        mAliChTimerView = findViewById(R.id.flexible_aliTimerViewIndicator);

        mTextViewStartHour = findViewById(R.id.flexible_textView_startTimeHour);
        mTextViewStartMinute = findViewById(R.id.flexible_textView_startTimeMinute);
        mTextViewStartAmPM = findViewById(R.id.flexible_textView_startTimeAmPM);
        mTextViewEndHour = findViewById(R.id.flexible_textView_endTimeHour);
        mTextViewEndMinute = findViewById(R.id.flexible_textView_endTimeMinute);
        mTextViewEndAmPM = findViewById(R.id.flexible_textView_endTimeAmPM);
        mTextViewRepeatHour = findViewById(R.id.flexible_textView_repeatTimeHour);
        mTextViewRepeatMinute = findViewById(R.id.flexible_textView_repeatTimeMinute);

        mSwitch = findViewById(R.id.flexible_switch_repeat);
        mConstraintLayoutRepeat = findViewById(R.id.flexible_constraintLayout_repeatTime);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        visibleRepeatItems(isChecked);

    }

    private void visibleRepeatItems(boolean isChecked) {
        mConstraintLayoutRepeat.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        mAliChTimerView.setShowRepeat(isChecked);

    }
}
