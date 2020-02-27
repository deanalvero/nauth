package com.lowbottgames.nauth.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.DirectionRepositoryImpl;
import com.lowbottgames.nauth.device.service.LockScreenService;
import com.lowbottgames.nauth.domain.DirectionEntry;
import com.lowbottgames.nauth.domain.DirectionManager;
import com.lowbottgames.nauth.ui.fragment.MainFragment;
import com.lowbottgames.nauth.ui.fragment.DirectionEntryFragment;

public class MainActivity extends AppCompatActivity implements
        MainFragment.Listener,
        DirectionEntryFragment.Listener
{
    private DirectionManager directionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directionManager = new DirectionManager(
                new DirectionRepositoryImpl(this)
        );

        checkOverlayPermission();

        startService(new Intent(
                this,
                LockScreenService.class
        ));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_main, MainFragment.newInstance())
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

    @Override
    public void onClickAuthenticate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_content, DirectionEntryFragment.newInstance(DirectionEntry.TYPE_AUTHENTICATE))
                .commitAllowingStateLoss();
    }

    @Override
    public void onClickChangeDirections() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_content, DirectionEntryFragment.newInstance(DirectionEntry.TYPE_CHANGE_DIRECTION))
                .commitAllowingStateLoss();
    }

    @Override
    public void onDirectionsEntered(int type, int[] directions) {
        switch (type) {
            case DirectionEntry.TYPE_AUTHENTICATE:
                if (directionManager.authenticate(directions)) {
                    showToastMessage("Authentication successful");
                } else {
                    showToastMessage("Authentication failed");
                }
                clearContentView();
                break;
            case DirectionEntry.TYPE_CHANGE_DIRECTION:
                directionManager.changeDirection(directions);
                showToastMessage("Directions changed");
                clearContentView();
                break;
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void clearContentView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_content, new Fragment())
                .commitAllowingStateLoss();
    }

}
