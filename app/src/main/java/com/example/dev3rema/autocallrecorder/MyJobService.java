package com.example.dev3rema.autocallrecorder;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by dev3rema
 */
public class MyJobService extends JobIntentService {

    public static final int JOB_ID = 0x01;

    private static final String TAG = "CallRecorderService";

    private MediaRecorder mRecorder;
    private boolean isRinging = false;
    private File mFile;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MM-dd-yy-hh-mm-ss", date.getTime());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath() + "/" + sequence + "_call_record.mp3");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (isRinging) {
                        mRecorder.stop();
                        mRecorder.release();
                        mRecorder = null;

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

    }

}