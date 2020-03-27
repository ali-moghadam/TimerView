package com.example.alichchartview;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alirnp.alichtimerview.TimeView.AliChTimerView;

import java.util.Timer;
import java.util.TimerTask;

public class IndicatorActivity extends AppCompatActivity {

    private TextView mTextViewStartTime;
    private AliChTimerView mAliChTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        findViews();
        config();

    }

    private int number = 0 ;
    private void config() {
        mTextViewStartTime.setText(String.format("Start Time at -> %s", mAliChTimerView.getStartTime()));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                number ++ ;
                runOnUiThread(() -> mAliChTimerView.setTextCenter(String.valueOf(number)));

            }
        },1000,1000);

        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                number ++ ;
                runOnUiThread(() -> mAliChTimerView.setTextStatus(String.valueOf(number*10)));
                timer.cancel();

            }
        },5000,1000);
    }

    private void findViews() {
        mTextViewStartTime = findViewById(R.id.indicator_textView_startTime);
        mAliChTimerView = findViewById(R.id.indicator_aliTimerView);
    }
}
