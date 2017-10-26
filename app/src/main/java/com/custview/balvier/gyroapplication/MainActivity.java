package com.custview.balvier.gyroapplication;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    TextView gyroreading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gyroreading = (TextView) findViewById(R.id.gyroreading);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() != 0) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private long lastUpdate, actualTime;
    float[] history = new float[3];
    String[] direction = new String[3];

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            actualTime = System.currentTimeMillis();
            if (actualTime - lastUpdate < 1000) {
                return;
            }
            lastUpdate = actualTime;

            float xChange = history[0] - event.values[0];
            float yChange = history[1] - event.values[1];

            history[0] = event.values[0];
            history[1] = event.values[1];

            if (xChange > 2) {
                direction[0] = "LEFT";
                //hardCorneringVisibility()
                Toast.makeText(this, "LEFT", Toast.LENGTH_SHORT).show();

            } else if (xChange < -2) {
                // hardCorneringVisibility()
                direction[0] = "RIGHT";
                Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();
            }

            if (yChange > 2) {
                direction[1] = "DOWN";
                Toast.makeText(this, "DOWN", Toast.LENGTH_SHORT).show();
            } else if (yChange < -2) {
                direction[1] = "UP";
                Toast.makeText(this, "UP", Toast.LENGTH_SHORT).show();
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroreading.setText("x values : " + convertRadPerSecondTodegreePerSecon(event.values[0])
                    + " \n y values : " + convertRadPerSecondTodegreePerSecon(event.values[1])
                    + " \n y values : " + convertRadPerSecondTodegreePerSecon(event.values[2]));
            if ((convertRadPerSecondTodegreePerSecon(event.values[0]) >= 15000
                    || convertRadPerSecondTodegreePerSecon(event.values[0]) <= -15000f)
                    || (convertRadPerSecondTodegreePerSecon(event.values[1]) >= 15000f
                    || convertRadPerSecondTodegreePerSecon(event.values[1]) <= -15000f)
                    || (convertRadPerSecondTodegreePerSecon(event.values[2]) >= 15000f
                    || convertRadPerSecondTodegreePerSecon(event.values[2]) <= -15000f)) {
                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
            } else {
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            }

        }
    }

    double convertRadPerSecondTodegreePerSecon(float radPerSecond) {
        return (radPerSecond * 180) / Math.PI;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
