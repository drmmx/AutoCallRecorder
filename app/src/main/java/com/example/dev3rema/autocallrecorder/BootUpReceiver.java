package com.example.dev3rema.autocallrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dev3rema
 */
public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, CallRecorderService.class));
            Toast.makeText(context, "Service Explicitly", Toast.LENGTH_SHORT).show();
        }
    }
}
