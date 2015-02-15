package com.vibaroo.btnow;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
    AudioManager audioManager;
    int activateHow;
    private static final int ACTIVATE_DIRECTLY = 0;
    private static final int ACTIVATE_BY_VOICE = 1;

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(null);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_main);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    activateHow = Integer.parseInt(prefs.getString("pref_launch",getString(R.string.pref_launchType_default)));

    audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
}
    @Override
    public void onStart() {
        super.onStart();
        switch (activateHow) {
            case ACTIVATE_DIRECTLY:
                startDirectly();
                break;
            case ACTIVATE_BY_VOICE:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                if (startBluetooth() != 1) finish();
                break;
            default:
                startDirectly();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopBluetooth();
    }

    private int startBluetooth() {
        try {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.startBluetoothSco();
            audioManager.setBluetoothScoOn(true);
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }
    }

    private int stopBluetooth() {
        try {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }
    }

    private int startDirectly() {
        startService(new Intent(this, HUD.class));
        finish();
        return(1);
    }

}