package com.lowbottgames.nauth.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.Compass;

public class MainActivity extends AppCompatActivity {

    private Compass compass;

    private TextView textViewAngle;
    private TextView textViewValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compass = new Compass(this);
        compass.setAngleListener(new Compass.AngleListener() {
            @Override
            public void onAngleChange(float angle) {
                textViewAngle.setText(String.valueOf(angle));
                textViewValue.setText(String.valueOf((int) angle / 10));
            }
        });

        textViewAngle = findViewById(R.id.textView_angle);
        textViewValue = findViewById(R.id.textView_value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }
}
