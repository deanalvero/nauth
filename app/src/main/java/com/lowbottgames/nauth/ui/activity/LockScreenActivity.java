package com.lowbottgames.nauth.ui.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.service.LockScreenService;

public class LockScreenActivity extends AppCompatActivity {

    public WindowManager windowManager;
    public RelativeLayout relativeLayout;
    private View mainView;

    private boolean isCheckingOverlayPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            showLockScreen();
        } catch (WindowManager.BadTokenException e) {
            checkOverlayPermission();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && isCheckingOverlayPermission
                && Settings.canDrawOverlays(this)) {
            recreate();
        }
    }

    private void showLockScreen() {
        if (!LockScreenService.isRunning(this)) {
            startService(new Intent(getBaseContext(), LockScreenService.class));
        }

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                PixelFormat.TRANSLUCENT);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager != null) {
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("IN");
            keyguardLock.disableKeyguard();
        }

        Window window = getWindow();
        window.setAttributes(localLayoutParams);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        relativeLayout = new RelativeLayout(getBaseContext());
        mainView = View.inflate(this, R.layout.activity_lock_screen, this.relativeLayout);

        windowManager = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.addView(this.relativeLayout, localLayoutParams);
        }

        mainView.findViewById(R.id.button_unlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(relativeLayout);
                relativeLayout.removeAllViews();

                finish();
            }
        });
    }

    public void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                isCheckingOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

}
