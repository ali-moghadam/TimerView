package com.example.alichchartview;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private AliChTimerView mAliChTimerView;
    private SeekBar mSeekBar;
    private SeekBar mSeekBarMinute;
    private TextView mTextView;
    private SwitchCompat mSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAliChTimerView = findViewById(R.id.aliTimerView);
        mSeekBar = findViewById(R.id.seekBarHour);
        mSeekBarMinute = findViewById(R.id.seekBarMinute);
        mTextView = findViewById(R.id.textView);
        mSwitch = findViewById(R.id.mySwitch);

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliChTimerView.setIsIndicator(mSwitch.isChecked());
                mAliChTimerView.invalidate();

            }
        });

        mAliChTimerView.setOnSeekCirclesListener(new OnSeekCirclesListener() {
            @Override
            public void OnSeekChangeStartHour(int hour) {
                mTextView.setText(String.valueOf(hour));
            }

            @Override
            public void OnSeekChangeEndHour(int hour) {
                mTextView.setText(hour + " end");
            }

            @Override
            public void OnSeekChangeRepeat(int hour) {
                mTextView.setText(hour + " repeat");
            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // mAliChTimerView.setStartTimeHour(progress);
                // mAliChTimerView.setEndTimeHour(progress);
                mAliChTimerView.setLeftTimeHour(progress);
                mTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarMinute.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // mAliChTimerView.setStartTimeMinute(progress);
                // mAliChTimerView.setEndTimeMinute(progress);
                mAliChTimerView.setLeftTimeMinute(progress);
                mTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
