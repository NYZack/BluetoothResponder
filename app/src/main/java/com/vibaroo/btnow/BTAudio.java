package com.vibaroo.btnow;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;

public class BTAudio extends Service {
    Runnable runnable;
    Handler handler;
    final long delayTimems = 30000;

    AudioManager audioManager;
    public BTAudio() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        runnable = new Runnable() {
            @Override
            public void run() {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
            }
        };
        handler = new Handler();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.startBluetoothSco();
        audioManager.setBluetoothScoOn(true);
        handler.postDelayed(runnable,delayTimems);
        return START_STICKY;
    }

}
