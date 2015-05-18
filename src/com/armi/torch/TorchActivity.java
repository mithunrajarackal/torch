package com.armi.torch;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TorchActivity extends Activity {
	@SuppressWarnings("deprecation")
	boolean click=false,flip=false;
    Camera cam;
    Parameters p;
    ImageView v1,v2,f1,f2;
    CameraInfo camInfo;
    Context context;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this.getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	  //  View view = this.getWindow().getDecorView();
	   // view.setBackgroundColor(Color.WHITE);
	    setContentView(R.layout.activity_torch);
	    camInfo=new CameraInfo();
	    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	   /* if(currentapiVersion>android.os.Build.VERSION_CODES.LOLLIPOP)
	    {
	    	CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
			try {
				String[] camList=manager.getCameraIdList();
			} catch (CameraAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }*/
	    	
	    ImageButton b;
	    b=(ImageButton)findViewById(R.id.ibutton);
	    b.setClickable(false);
	    v1=(ImageView)findViewById(R.id.off);
	    v2=(ImageView)findViewById(R.id.on);
	    f1=(ImageView)findViewById(R.id.flipfront);
	    f2=(ImageView)findViewById(R.id.flipback);
	    v1.setVisibility(View.VISIBLE);
	    v2.setVisibility(View.INVISIBLE);
	    f1.setVisibility(View.VISIBLE);
	    f2.setVisibility(View.INVISIBLE);
	   
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
		/*curBrightnessValue=Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
		
		Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
		//this.wait(5000);
		Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS,(int) curBrightnessValue);*/
		
	}
	
	//Function of back button
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	v1.setVisibility(View.VISIBLE);
		    v2.setVisibility(View.INVISIBLE);
		    if(cam!=null)
	    	cam.release();	
	        // do something on back.
	        //return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    v1.setVisibility(View.VISIBLE);
	    v2.setVisibility(View.INVISIBLE);
	    // Release the Camera because we don't need it when paused
	    // and other activities might need to use it.
	    if (cam != null) {
	        cam.release();
	        cam= null;
	    }
	}
	
	//Light up the torch based on LED availability.If LED not found, screen will be used as torch.
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void lightTorch(View v)
	 {
		if(flip==false)// if LED used is the back LED
		{
		if(click==false) //Current state is OFF
		{
		try
		{
		 cam=Camera.open(0);    
		 p = cam.getParameters();
		 List<String> flashModes = p.getSupportedFlashModes();
		 if(flashModes.contains(Parameters.FLASH_MODE_TORCH)) //check if the flash mode contains torch mode.
		 {
	    	v1.setVisibility(View.INVISIBLE);
		    v2.setVisibility(View.VISIBLE);
			click=true;
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
    		cam.setParameters(p);
    		cam.startPreview();
		 }
		 else //if torch mode is not available light up the display in the new activity.
		 {
			cam.release();
			 click=true;
			 Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
			 TorchActivity.this.startActivity(intent);
		 }
		}
		catch(RuntimeException e)
		{
			Toast.makeText(context,"Camera is used by some other application.Please close that application and try again.", Toast.LENGTH_LONG).show();
		}
		}
		else //Current state is ON
		{
			v1.setVisibility(View.VISIBLE);
		    v2.setVisibility(View.INVISIBLE);
			click=false;
			if(cam!=null)
			{
			 cam.stopPreview();
			 cam.release();
			 cam=null;
			}
    		
		}	
		}
		else   //if the LED used is of secondary camera(Front camera)
		{
		  if(click==false) //Current state is OFF
		  {
			try
			{
		    cam=Camera.open(1);    
		    p = cam.getParameters();
		    click=true;
			List<String> flashModes = p.getSupportedFlashModes();
			if(flashModes.contains(Parameters.FLASH_MODE_TORCH))
			{
		    	v1.setVisibility(View.INVISIBLE);
			    v2.setVisibility(View.VISIBLE);
				
				
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    		cam.setParameters(p);
	    		cam.startPreview();
			}
			else
			{
			 cam.release();
			 cam=null;
			 Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
			 TorchActivity.this.startActivity(intent);
			}
		  }
			catch(RuntimeException e)
			{
				Toast.makeText(context,"Camera is used by some other application.Please close that application and try again.", Toast.LENGTH_LONG).show();
			}
		  }
		  else //Current state is ON
			{
				v1.setVisibility(View.VISIBLE);
			    v2.setVisibility(View.INVISIBLE);
				click=false;
				if(cam!=null)
				{
				cam.stopPreview();
				cam.release();
				cam=null;
				}
	    		
			}
		  }
	 }
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void TorchFlip(View v)
	{
		if(flip==false) //Currently using back LED
		{
		 flip=true;
		 f1.setVisibility(View.INVISIBLE);
		 f2.setVisibility(View.VISIBLE);
		 if(click==true) //Current state is ON
		 {
			 v1.setVisibility(View.VISIBLE);
			    v2.setVisibility(View.INVISIBLE);
				click=false;
				if(cam!=null)
				{
				cam.stopPreview();
				cam.release();
				cam=null;
				}
				
		 }
		 //cam.release();
		 //cam=Camera.open(1);
		 //p=cam.getParameters();
		}
		else //Currently using Front LED
		{
			f1.setVisibility(View.VISIBLE);
			 f2.setVisibility(View.INVISIBLE);
		 flip=false;
		 if(click==true) //Current state is ON
		 {
			    v1.setVisibility(View.VISIBLE);
			    v2.setVisibility(View.INVISIBLE);
				click=false;
				if(cam!=null)
				{
				cam.stopPreview();
				cam.release();
				cam=null;
				}
				
		 }
		 //cam.release();
		 //cam=Camera.open(0);
		 //p=cam.getParameters();
		}
	}
}
