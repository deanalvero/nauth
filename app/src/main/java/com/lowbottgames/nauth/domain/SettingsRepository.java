package com.lowbottgames.nauth.domain;

public interface SettingsRepository {
    void setFlag(String key, boolean flag);
    boolean getFlag(String key);
}
