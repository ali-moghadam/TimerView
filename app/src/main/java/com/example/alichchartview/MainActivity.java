package com.example.alichchartview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonFlexible = findViewById(R.id.button_flexible);
        Button mButtonIndicator = findViewById(R.id.button_indicator);

        mButtonFlexible.setOnClickListener(v -> startActivity(new Intent(this, FlexibleActivity.class)));
        mButtonIndicator.setOnClickListener(v -> startActivity(new Intent(this, IndicatorActivity.class)));

    }


}
