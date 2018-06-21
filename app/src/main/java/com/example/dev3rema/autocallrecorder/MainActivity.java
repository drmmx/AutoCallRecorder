package com.example.dev3rema.autocallrecorder;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder recorder;
    private boolean isRecordStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMediaRecorder(getAudioSource("VOICE_CALL"));
    }

    private boolean startMediaRecorder(final int audioSource){
        recorder = new MediaRecorder();
        try{
            recorder.reset();
            recorder.setAudioSource(audioSource);
            recorder.setAudioSamplingRate(8000);
            recorder.setAudioEncodingBitRate(12200);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String fileName = getExternalCacheDir().getAbsolutePath();
            recorder.setOutputFile(fileName);

            /*MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder arg0, int arg1, int arg2) {
                    Log.e(TAG, "OnErrorListener " + arg1 + "," + arg2);
                    terminateAndEraseFile();
                }
            };
            recorder.setOnErrorListener(errorListener);

            MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
                public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
                    Log.e(TAG, "OnInfoListener " + arg1 + "," + arg2);
                    terminateAndEraseFile();
                }
            };
            recorder.setOnInfoListener(infoListener);*/


            recorder.prepare();
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            recorder.start();
            isRecordStarted = true;
            return true;
        }catch (Exception e){
            e.getMessage();
            return false;
        }
    }

    public static int getAudioSource(String str) {
        if (str.equals("MIC")) {
            return MediaRecorder.AudioSource.MIC;
        }
        else if (str.equals("VOICE_COMMUNICATION")) {
            return MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        }
        else if (str.equals("VOICE_CALL")) {
            return MediaRecorder.AudioSource.VOICE_CALL;
        }
        else if (str.equals("VOICE_DOWNLINK")) {
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;
        }
        else if (str.equals("VOICE_UPLINK")) {
            return MediaRecorder.AudioSource.VOICE_UPLINK;
        }
        else if (str.equals("VOICE_RECOGNITION")) {
            return MediaRecorder.AudioSource.VOICE_RECOGNITION;
        }
        else if (str.equals("CAMCORDER")) {
            return MediaRecorder.AudioSource.CAMCORDER;
        }
        else {
            return MediaRecorder.AudioSource.DEFAULT;
        }
    }
}
