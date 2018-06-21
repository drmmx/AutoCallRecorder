package com.example.dev3rema.autocallrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import java.io.File;
import java.util.Date;

public class CallRecorderService extends Service {

    private MediaRecorder mRecorder;
    private boolean isRecordStarted;
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
        mRecorder.setOutputFile(mFile.getAbsolutePath() + "/" + sequence + "voice_record.3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext()
                .getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        return super.onStartCommand(intent, flags, startId);
    }

}
