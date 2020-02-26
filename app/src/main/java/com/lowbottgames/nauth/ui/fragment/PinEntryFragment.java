package com.lowbottgames.nauth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.Compass;

public class PinEntryFragment extends Fragment {

    private Compass compass;
    private TextView textViewAngle;
    private TextView textViewValue;


    public static PinEntryFragment newInstance() {
        PinEntryFragment fragment = new PinEntryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compass = new Compass(getContext());
        compass.setAngleListener(new Compass.AngleListener() {
            @Override
            public void onAngleChange(float angle) {
                textViewAngle.setText(String.valueOf(angle));
                textViewValue.setText(String.valueOf((int) angle / 10));
            }
        });

        textViewAngle = (TextView) view.findViewById(R.id.textView_angle);
        textViewValue = (TextView) view.findViewById(R.id.textView_value);

        view.findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        getContext(),
                        String.valueOf((int) compass.getAngle() / 10),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        compass.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        compass.stop();
    }

}
