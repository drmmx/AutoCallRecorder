package com.example.dev3rema.autocallrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton mToggleButton;

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        mToggleButton = findViewById(R.id.toggleButton);

    }

    public void toggleButton(View view) {

        boolean isChecked = ((ToggleButton) view).isChecked();

        if (isChecked) {
            startService(new Intent(this, CallRecorderService.class));
            Toast.makeText(this, "Call recorder started", Toast.LENGTH_SHORT).show();
        } else {
            stopService(new Intent(this, CallRecorderService.class));
            Toast.makeText(this, "Call recorder stopped", Toast.LENGTH_SHORT).show();
        }

    }

}
