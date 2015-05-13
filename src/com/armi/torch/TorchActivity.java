package com.armi.torch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TorchActivity extends Activity {
	@SuppressWarnings("deprecation")
	boolean click=false;
    Camera cam;
    Parameters p;
    ImageView v1,v2;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context=this.getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	  //  View view = this.getWindow().getDecorView();
	   // view.setBackgroundColor(Color.WHITE);
	    setContentView(R.layout.activity_torch);
	    cam=Camera.open();    
		p = cam.getParameters();
	    ImageButton b;
	    b=(ImageButton)findViewById(R.id.ibutton);
	    b.setClickable(false);
	    v1=(ImageView)findViewById(R.id.off);
	    v2=(ImageView)findViewById(R.id.on);
	    v1.setVisibility(View.VISIBLE);
	    v2.setVisibility(View.INVISIBLE);
	   	float curBrightnessValue = 0;
	    try {
	    	if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
	    	{
	    		b.setClickable(true);	
	    		//b.setOnClickListener((OnClickListener) this);
	    		//Toast.makeText(getApplicationContext(), "You can use LED torch :)", Toast.LENGTH_LONG).show();
	    		 
	    		/* b.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							
							if(click==false)
							{
								click=true;					
		    		    		
							}
							else
							{
								click=false;
							}
						}
	    		    	
	    		    });*/
	    	               
	    		
	    	
	    	}
	    	else
	    		Toast.makeText(getApplicationContext(), "Sorry LED torch not available :(", Toast.LENGTH_LONG).show();
	    	curBrightnessValue=Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			
	    	Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
            //this.wait(5000);
            Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS,(int) curBrightnessValue);

	    } catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	cam.release();	
	        // do something on back.
	        //return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	@SuppressWarnings("deprecation")
	public void lightTorch(View v)
	 {
		if(click==false)
		{
			v1.setVisibility(View.INVISIBLE);
		    v2.setVisibility(View.VISIBLE);
			click=true;
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
    		cam.setParameters(p);
    		cam.startPreview();
		}
		else
		{
			v1.setVisibility(View.VISIBLE);
		    v2.setVisibility(View.INVISIBLE);
			click=false;
			cam.stopPreview();
    		
		}
	 }
}
