package com.lowbottgames.nauth.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.ui.fragment.PinEntryFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, PinEntryFragment.newInstance())
                .commitAllowingStateLoss();
    }
}
