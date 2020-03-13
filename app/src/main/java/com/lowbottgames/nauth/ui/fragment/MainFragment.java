package com.lowbottgames.nauth.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.SettingsRepositoryImpl;
import com.lowbottgames.nauth.domain.SettingsRepository;

public class MainFragment extends Fragment {

    private Switch switchEnableLock;
    private SettingsRepository settingsRepository;
    private MainFragment.Listener listener;

    private static final String KEY_ENABLE_LOCK = "KEY_ENABLE_LOCK";

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        settingsRepository = new SettingsRepositoryImpl(getContext());

        switchEnableLock = (Switch) view.findViewById(R.id.switch_enable_lock);
        switchEnableLock.setChecked(settingsRepository.getFlag(KEY_ENABLE_LOCK));
        switchEnableLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsRepository.setFlag(KEY_ENABLE_LOCK, b);
            }
        });

        view.findViewById(R.id.button_authenticate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickAuthenticate();
                }
            }
        });
        view.findViewById(R.id.button_change_directions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickChangeDirections();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public interface Listener {
        void onClickAuthenticate();
        void onClickChangeDirections();
    }

}
