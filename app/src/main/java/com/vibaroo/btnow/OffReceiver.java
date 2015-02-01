package com.vibaroo.btnow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OffReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    String intentAction = intent.getAction();
    if (!Intent.ACTION_SCREEN_OFF.equals(intentAction)) {
        return;
    }
    Intent mIntent = new Intent(context, HUD.class);
    context.stopService(mIntent);
  }
} 