package com.example.dev3rema.autocallrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CallRecorderService extends Service {

    private MediaRecorder mRecorder;
    private boolean isRecordStarted = false;
    private File mFile;
    String path = "/sdcard/call_records/";

    public CallRecorderService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MM-dd-yy-hh-mm-ss", date.getTime());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath() + "/" + sequence + "call_record.3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
/*                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    mRecorder.start();
                }*/ if (state == TelephonyManager.CALL_STATE_IDLE && mRecorder == null && isRecordStarted) {
                    mRecorder.stop();
                    if (mRecorder != null) {
                        mRecorder.reset();
                        mRecorder.release();

                        isRecordStarted = false;
                        mRecorder = null;
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mRecorder.start();
                    isRecordStarted = true;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        return START_STICKY;
    }

}
