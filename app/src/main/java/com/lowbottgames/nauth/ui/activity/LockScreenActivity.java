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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.Compass;
import com.lowbottgames.nauth.device.DirectionRepositoryImpl;
import com.lowbottgames.nauth.device.service.LockScreenService;
import com.lowbottgames.nauth.domain.DirectionEntry;
import com.lowbottgames.nauth.domain.DirectionManager;
import com.lowbottgames.nauth.ui.view.DirectionView;

public class LockScreenActivity extends AppCompatActivity {

    public WindowManager windowManager;
    public RelativeLayout relativeLayout;

    private DirectionManager directionManager;
    private Compass compass;
    private TextView textViewInput;
    private TextView textViewAngle;
    private TextView textViewValue;
    private DirectionEntry directionEntry;

    private boolean isCheckingOverlayPermission = false;

    private DirectionView directionView;
    private float currentAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compass = new Compass(this);
        compass.start();
        directionManager = new DirectionManager(
                new DirectionRepositoryImpl(this)
        );

        try {
            showUI();
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

    @Override
    protected void onDestroy() {
        compass.stop();
        super.onDestroy();
    }

    private void showUI() {
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

        relativeLayout = new RelativeLayout(getBaseContext());
        View mainView = View.inflate(this, R.layout.activity_lock_screen, this.relativeLayout);

        windowManager = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.addView(this.relativeLayout, localLayoutParams);
        }

        compass.setAngleListener(new Compass.AngleListener() {
            @Override
            public void onAngleChange(float angle) {
                textViewAngle.setText(String.valueOf(angle));
                textViewValue.setText(String.valueOf((int) angle / 10));
                updateUIDirectionView(angle);
            }
        });

        textViewInput = (TextView) mainView.findViewById(R.id.textView_input);
        textViewAngle = (TextView) mainView.findViewById(R.id.textView_angle);
        textViewValue = (TextView) mainView.findViewById(R.id.textView_value);
        directionView = (DirectionView) mainView.findViewById(R.id.directionView);

        directionEntry = new DirectionEntry(new DirectionEntry.Listener() {
            @Override
            public void onInputCount(int count) {
                textViewInput.setText(
                        new String(new char[count]).replace("\0", "*")
                );
            }

            @Override
            public void onDirectionsEntered(int[] directions) {
                if (directionManager.authenticate(directions)) {
                    showToastMessage("Authentication successful");

                    windowManager.removeView(relativeLayout);
                    relativeLayout.removeAllViews();
                    finish();
                } else {
                    showToastMessage("Authentication failed");
                }
            }
        });

        mainView.findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionEntry.enter(
                        (int) compass.getAngle() / 10
                );
            }
        });

        mainView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionEntry.delete();
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

    private void updateUIDirectionView(float angle) {
        Animation animation = new RotateAnimation(
                -currentAngle,
                -angle,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        currentAngle = angle;

        animation.setDuration(500);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);

        directionView.startAnimation(animation);
    }

    private void showToastMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}
