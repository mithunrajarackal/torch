package com.armi.torch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class WhiteActivity extends Activity {
	boolean click=false,flip=false;
	int curBrightness=0;
    ImageView v1,v2,f1,f2;
    
   	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Context context=this.getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN); //display the screen without settings bars.(Full Screen)
	    View view = this.getWindow().getDecorView();
	   	view.setBackgroundColor(Color.WHITE); //sets background color to white
	    setContentView(R.layout.activity_white);
	    try {
			curBrightness=Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS); //get the current display brightness
			Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255); //sets the display brightness to 255 which is max.
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	}
   	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightness); //restores the system brightness on back key press.
	    	Intent intent = new Intent();
	    	setResult(RESULT_OK, intent);
	    	finish();
	    	// do something on back.
	        //return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
   	
   	@Override
   	public void onPause() {
   	    super.onPause();  // Always call the superclass method first
   	    Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightness); //restores the system brightness on home key press.
   	}
   	
   	public void onResume() {
   	    super.onPause();  // Always call the superclass method first
   	    Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255); //restores the max brightness on resume.
   	}
   	
   	public void closeTorch(View v) {
   		Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightness); //restores the system brightness on back key press.
    	Intent intent = new Intent();
    	setResult(RESULT_OK, intent);
    	finish();
   	}

}