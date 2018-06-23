package com.example.dev3rema.autocallrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CallRecorderService extends Service {

    private static final String TAG = "CallRecorderService";

    private MediaRecorder mRecorder;
    private boolean isRinging = false;
    private File mFile;
//    String path = "/sdcard/call_records/";

    public CallRecorderService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //current date and time
/*        Date date = new Date();
        CharSequence sequence = DateFormat.format("MM-dd-yy-hh-mm-ss", date.getTime());*/

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (isRinging) {
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();

                        isRinging = false;
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "prepare() failed");
                    }

                    mRecorder.start();
                    isRinging = true;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (manager != null) {
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        return START_STICKY;
    }

}
