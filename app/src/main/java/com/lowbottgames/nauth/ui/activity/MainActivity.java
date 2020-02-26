package com.lowbottgames.nauth.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.service.LockScreenService;
import com.lowbottgames.nauth.ui.fragment.PinEntryFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkOverlayPermission();

        startService(new Intent(
                this,
                LockScreenService.class
        ));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, PinEntryFragment.newInstance())
                .commitAllowingStateLoss();
    }


    public void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

}
