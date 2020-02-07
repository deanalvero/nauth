package com.lowbottgames.nauth.device;

import android.content.Context;

public class Compass implements OrientationSensor.OrientationListener {

    private OrientationSensor orientationSensor;
    private Compass.AngleListener angleListener;

    public Compass(Context context) {
        orientationSensor = new OrientationSensor(
                context
        );
        orientationSensor.setOrientationListener(this);
    }

    public void setAngleListener(AngleListener listener) {
        angleListener = listener;
    }

    public void start() {
        orientationSensor.start();
    }

    public void stop() {
        orientationSensor.stop();
    }

    @Override
    public void onOrientationChange(float azimuth, float pitch, float roll) {
        if (angleListener != null) {
            float result = (float) Math.toDegrees(azimuth);
            if (result < 0.0f) {
                result += 360f;
            }
            angleListener.onAngleChange(result);
        }
    }

    public interface AngleListener {
        void onAngleChange(
                float angle
        );
    }
}
