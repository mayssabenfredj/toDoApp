package com.example.todo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.todo.Utils.DatabaseHandler;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private FrameLayout containerCalandar;
    private FrameLayout containerToDo;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LinearLayout mainLayout;
    private String selectedDate;
    private DatabaseHandler databaseHandler;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean isAuthenticated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerCalandar = findViewById(R.id.calander);
        containerToDo = findViewById(R.id.toDo);
        mainLayout = findViewById(R.id.mainLayout);
        databaseHandler = new DatabaseHandler(this);

        if (savedInstanceState != null) {
            isAuthenticated = savedInstanceState.getBoolean("isAuthenticated", false);
        }

        if (!isAuthenticated) {
            authenticateWithBiometric();
        } else {
            mainLayout.setVisibility(View.VISIBLE);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.calander, new calandar())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.toDo, new toDo())
                .commit();
    }

    private void authenticateWithBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Device Doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Not Working", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                isAuthenticated = true;
                mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("To Do Calendar")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isAuthenticated", isAuthenticated);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            if (Math.abs(x) > Math.abs(y)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mainLayout.setOrientation(LinearLayout.HORIZONTAL);
                containerCalandar.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                containerCalandar.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                containerCalandar.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                containerCalandar.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void setSelectedDate(String date) {
        selectedDate = date;
        toDo toDoFragment = (toDo) getSupportFragmentManager().findFragmentById(R.id.toDo);
        if (toDoFragment != null) {
            toDoFragment.updateSelectedDate(selectedDate);
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }
}
