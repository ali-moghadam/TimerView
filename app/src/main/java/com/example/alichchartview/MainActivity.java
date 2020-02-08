package com.example.alichchartview;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AliChTimerView mAliChTimerView;
    private SeekBar mSeekBar;
    private SeekBar mSeekBarMinute;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAliChTimerView = findViewById(R.id.aliTimerView);
        mSeekBar = findViewById(R.id.seekBarHour);
        mSeekBarMinute = findViewById(R.id.seekBarMinute);
        mTextView = findViewById(R.id.textView);


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
