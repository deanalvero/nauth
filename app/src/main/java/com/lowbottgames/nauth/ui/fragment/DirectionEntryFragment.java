package com.lowbottgames.nauth.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.lowbottgames.nauth.R;
import com.lowbottgames.nauth.device.Compass;
import com.lowbottgames.nauth.domain.DirectionEntry;
import com.lowbottgames.nauth.ui.view.DirectionView;

public class DirectionEntryFragment extends Fragment {

    private Compass compass;
    private TextView textViewInput;
    private TextView textViewAngle;
    private TextView textViewValue;
    private DirectionEntry directionEntry;
    private DirectionEntryFragment.Listener listener;

    private DirectionView directionView;
    private float currentAngle;

    private static final String KEY_TYPE = "KEY_TYPE";

    private int type;

    public static DirectionEntryFragment newInstance(int type) {
        DirectionEntryFragment fragment = new DirectionEntryFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt(KEY_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direction_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        directionEntry = new DirectionEntry(new DirectionEntry.Listener() {
            @Override
            public void onInputCount(int count) {
                textViewInput.setText(
                        new String(new char[count]).replace("\0", "*")
                );
            }

            @Override
            public void onDirectionsEntered(int[] directions) {
                if (listener != null) {
                    listener.onDirectionsEntered(type, directions);
                }
            }
        });

        compass = new Compass(getContext());
        compass.setAngleListener(new Compass.AngleListener() {
            @Override
            public void onAngleChange(float angle) {
                textViewAngle.setText(String.valueOf(angle));
                textViewValue.setText(String.valueOf((int) angle / 10));
                updateUIDirectionView(angle);
            }
        });

        textViewInput = (TextView) view.findViewById(R.id.textView_input);
        textViewAngle = (TextView) view.findViewById(R.id.textView_angle);
        textViewValue = (TextView) view.findViewById(R.id.textView_value);
        directionView = (DirectionView) view.findViewById(R.id.directionView);

        view.findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionEntry.enter(
                        (int) compass.getAngle() / 10
                );
            }
        });


        view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionEntry.delete();
            }
        });
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DirectionEntryFragment.Listener) context;
    }

    public interface Listener {
        void onDirectionsEntered(int type, int[] directions);
    }

}
