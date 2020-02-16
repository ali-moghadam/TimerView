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




        mAliChTimerView.setOnSeekCirclesListener(new OnSeekCirclesListener() {
            @Override
            public void OnSeekChange(AliChTimerView.CircleID circleID, int hour, int minute) {
                String mStringHour = Utils.addZeroBeforeTime(hour);
                String mStringMinute = Utils.addZeroBeforeTime(minute);

                mTextView.setText(String.format("hour:%s \t minute:%s", mStringHour, mStringMinute));

                mAliChTimerView.setTextCenter(mStringHour + ":" + mStringMinute);


                String status;
                switch (circleID) {
                    case CIRCLE_START_TIME:
                        status = "start time";
                        break;
                    case CIRCLE_REPEAT_TIME:
                        status = "repeat time";
                        break;
                    case CIRCLE_END_TIME:
                        status = "end time";
                        break;

                    case NONE:
                    default:
                        status = "NONE";

                }

                //  mAliChTimerView.setTextStatus(status);
            }


        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
