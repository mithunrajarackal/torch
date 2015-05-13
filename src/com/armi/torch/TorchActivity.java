package com.armi.torch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class TorchActivity extends Activity {
	@SuppressWarnings("deprecation")
	boolean click=false;
    Camera cam;
    Parameters p;
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
	    cam=Camera.open();    
		p = cam.getParameters();
	    ImageButton b=new ImageButton(context);
	    b=(ImageButton)findViewById(R.id.button);
	    
	    float curBrightnessValue = 0;
	    try {
	    	if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
	    	{
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
		setContentView(R.layout.activity_torch);
	}
	
	@SuppressWarnings("deprecation")
	void lightTorch(View v)
	 {
		if(click==false)
		{
			click=true;
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
    		cam.setParameters(p);
    		cam.startPreview();
		}
		else
		{
			click=false;
			cam.stopPreview();
    		cam.release();	
		}
	 }
}
