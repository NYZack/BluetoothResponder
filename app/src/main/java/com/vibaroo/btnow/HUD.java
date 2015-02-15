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
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
	    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    myView = inflater.inflate(R.layout.empty_layout, null);

    // Add layout to window manager
	    wm.addView(myView, params);	    	
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
	    mReceiver = new OffReceiver();
	    registerReceiver(mReceiver, filter);

        ComponentName cn = new ComponentName("com.google.android.googlequicksearchbox",
                "com.google.android.googlequicksearchbox.VoiceSearchActivity");
        launchIntent = new Intent();
//        launchIntent = new Intent("android.intent.action.VOICE_ASSIST");
//        launchIntent.setFlags(0x1c082000);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.setComponent(cn);
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