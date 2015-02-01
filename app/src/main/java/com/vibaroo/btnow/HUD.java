package com.vibaroo.btnow;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class HUD extends Service {
	View myView;
	KeyguardLock keyguardLock;
	BroadcastReceiver mReceiver;
	Intent launchIntent;
	
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    	final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    	WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                 PixelFormat.TRANSLUCENT);
	    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    myView = inflater.inflate(R.layout.activity_main, null);

    // Add layout to window manager
	    wm.addView(myView, params);	    	
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
	    mReceiver = new OffReceiver();
	    registerReceiver(mReceiver, filter); 
	    launchIntent = new Intent(Intent.ACTION_MAIN, null);
	    launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	    ComponentName cn = new ComponentName("com.google.android.googlequicksearchbox",
	    		"com.google.android.googlequicksearchbox.VoiceSearchActivity");
	    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    launchIntent.setComponent(cn);


/*        final Handler handler = new Handler();
        final long oneMinuteMs = 10 * 1000;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("LogTag", "Keyguard"+keyguardManager.isKeyguardSecure());
                handler.postDelayed(this, oneMinuteMs);
            }
        };
        handler.postDelayed(runnable, oneMinuteMs);*/
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startActivity(launchIntent);
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver); 
        keyguardLock.reenableKeyguard();
        if(myView != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(myView);
            myView = null;
        }
    }
}