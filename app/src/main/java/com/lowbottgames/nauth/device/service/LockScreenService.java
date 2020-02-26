package com.lowbottgames.nauth.device.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lowbottgames.nauth.device.receiver.LockScreenReceiver;

public class LockScreenService extends Service {

    private LockScreenReceiver lockScreenReceiver = new LockScreenReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(lockScreenReceiver);
        super.onDestroy();
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LockScreenService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

