package com.vibaroo.btnow;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(null);
    startService(new Intent(this, HUD.class));
    finish();
}

}