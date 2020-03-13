package com.lowbottgames.nauth.device;

import android.content.Context;
import android.content.SharedPreferences;

import com.lowbottgames.nauth.domain.SettingsRepository;

public class SettingsRepositoryImpl implements SettingsRepository {

    private SharedPreferences sharedPreferences;

    public SettingsRepositoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("nauth", Context.MODE_PRIVATE);
    }

    @Override
    public void setFlag(String key, boolean flag) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, flag);
        editor.commit();
    }

    @Override
    public boolean getFlag(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
