package com.lowbottgames.nauth.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lowbottgames.nauth.device.SettingsRepositoryImpl;
import com.lowbottgames.nauth.domain.SettingsRepository;
import com.lowbottgames.nauth.ui.activity.LockScreenActivity;

public class LockScreenReceiver extends BroadcastReceiver {

    private static final String KEY_ENABLE_LOCK = "KEY_ENABLE_LOCK";

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsRepository settingsRepository = new SettingsRepositoryImpl(context);

        if (settingsRepository.getFlag(KEY_ENABLE_LOCK)) {
            Intent newIntent = new Intent(context, LockScreenActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
    }
}
