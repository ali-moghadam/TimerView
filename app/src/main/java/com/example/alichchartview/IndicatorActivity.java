package com.example.alichchartview;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alirnp.alichtimerview.TimeView.AliChTimerView;

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

    private void config() {
        mTextViewStartTime.setText(String.format("Start Time at -> %s", mAliChTimerView.getStartTime()));
    }

    private void findViews() {
        mTextViewStartTime = findViewById(R.id.indicator_textView_startTime);
        mAliChTimerView = findViewById(R.id.indicator_aliTimerView);
    }
}
