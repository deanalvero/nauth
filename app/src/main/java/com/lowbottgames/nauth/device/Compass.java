package com.lowbottgames.nauth.device;

import android.content.Context;

public class Compass implements OrientationSensor.OrientationListener {

    private OrientationSensor orientationSensor;
    private Compass.AngleListener angleListener;
    private float angle;

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
        float result = (float) Math.toDegrees(azimuth);
        if (result < 0.0f) {
            result += 360f;
        }
        if (angleListener != null) {
            angleListener.onAngleChange(result);
        }
        angle = result;
    }

    public float getAngle() {
        return angle;
    }

    public interface AngleListener {
        void onAngleChange(
                float angle
        );
    }
}
