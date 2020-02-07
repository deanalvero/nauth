package com.lowbottgames.nauth.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magnetometerSensor;
    private Sensor accelerometerSensor;

    private float[] magnetometerData = new float[3];
    private float[] accelerometerData = new float[3];

    private OrientationListener orientationListener;

    public OrientationSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magnetometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD
        );
        accelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
        );
    }

    public void setOrientationListener(OrientationListener listener) {
        orientationListener = listener;
    }

    public void start() {
        if (magnetometerSensor != null) {
            sensorManager.registerListener(this, magnetometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, accelerometerData, magnetometerData);

        float[] orientationValues = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        if (orientationListener != null) {
            orientationListener.onOrientationChange(
                    orientationValues[0],
                    orientationValues[1],
                    orientationValues[2]
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface OrientationListener {
        void onOrientationChange(
                float azimuth,
                float pitch,
                float roll
        );
    }
}
