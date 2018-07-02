package com.example.dev3rema.autocallrecorder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CallRecorderService extends Service {

    private MediaRecorder mRecorder = new MediaRecorder();
    private boolean isRecording;
    private String mPhoneNumber;

//    private Date date = new Date();
//    private CharSequence sequence = DateFormat.format("MM-dd-yy-hh-mm-ss", date.getTime());

    BroadcastReceiver callRecorder = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {

                Toast.makeText(arg0, "Start Call " + isRecording + mPhoneNumber, Toast.LENGTH_LONG).show();

                startRecording();
            }

            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state) && isRecording) {

                Toast.makeText(arg0, "Stop Call: " + isRecording, Toast.LENGTH_LONG).show();

                stopRecording();
            }

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

                mPhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                Toast.makeText(getApplicationContext(), state + " : " + mPhoneNumber, Toast.LENGTH_LONG).show();
            }
        }
    };

    BroadcastReceiver OutGoingNumDetector = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_SHORT).show();

        IntentFilter RecFilter = new IntentFilter();
        RecFilter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callRecorder, RecFilter);
        IntentFilter OutGoingNumFilter = new IntentFilter();
        OutGoingNumFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(OutGoingNumDetector, OutGoingNumFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callRecorder);
        unregisterReceiver(OutGoingNumDetector);
        Toast.makeText(this, "Call recorder service closed", Toast.LENGTH_SHORT).show();
    }

    public void startRecording() {
        if (!isRecording) {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String file = Environment.getExternalStorageDirectory().toString();
            String filepath = file + "/call_records";
            File dir = new File(filepath);
            dir.mkdirs();

            filepath += "/" + mPhoneNumber + "_" + System.currentTimeMillis() + ".mp3";
            mRecorder.setOutputFile(filepath);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            isRecording = true;
        }
    }

    public void stopRecording() {
        if (isRecording) {
            Toast.makeText(getApplicationContext(), "Recorder released " + isRecording, Toast.LENGTH_LONG).show();

            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
//            broadcastIntent();
        }
    }

/*    public void broadcastIntent() {
        Intent intent = new Intent();
        intent.setAction("com.exampled.beta.CUSTOM_INTENT");
        sendBroadcast(intent);
        Toast.makeText(getApplicationContext(), "BroadCaste", Toast.LENGTH_LONG).show();
    }*/
}



