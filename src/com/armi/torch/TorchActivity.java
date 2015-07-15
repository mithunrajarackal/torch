package com.armi.torch;

import java.util.List;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TorchActivity extends Activity {
	boolean click=false,flip=false;
    Camera cam;
    Parameters p;
    ImageView v1,v2,f1,f2;
    CameraInfo camInfo;
    Context context;
    boolean camer = true;
    int val = 0;
    AnimatorSet set;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this.getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.activity_torch);
	    camInfo=new CameraInfo();
	     	
	    ImageButton b;
	    b=(ImageButton)findViewById(R.id.ibutton);
	    b.setClickable(false);
	    v1=(ImageView)findViewById(R.id.off);
	    v2=(ImageView)findViewById(R.id.on);
	    f1=(ImageView)findViewById(R.id.flipfront);
	    f2=(ImageView)findViewById(R.id.flipback);
	    v1.setVisibility(View.INVISIBLE);
	    v2.setVisibility(View.INVISIBLE);
	    f1.setVisibility(View.VISIBLE);
	    f2.setVisibility(View.INVISIBLE);
	   
	    if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
		{
			b.setClickable(true);
		}
		else {
			b.setClickable(true);
			f1.setVisibility(View.INVISIBLE);
			camer = false;
		}
			
	}
	
	//Function of back button
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	rl.setBackgroundResource(R.drawable.flashlight1);
	    	v1.setVisibility(View.INVISIBLE);
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
	    RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
	    click=false;
	    v1.setVisibility(View.INVISIBLE);
	    v2.setVisibility(View.INVISIBLE);
	    // Release the Camera because we don't need it when paused
	    // and other activities might need to use it.
	    rl.setBackgroundResource(R.drawable.flashlight1);
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
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
		if(flip==false)// if LED used is the back LED
		{
			if(click==false) //Current state is OFF
			{
				try
				{
					if(camer == true) {
						rl.setBackgroundResource(R.drawable.backlight2);
						cam=Camera.open(0);    
						p = cam.getParameters();
						List<String> flashModes = p.getSupportedFlashModes();
						if(flashModes.contains(Parameters.FLASH_MODE_TORCH)) //check if the flash mode contains torch mode.
						{
							v1.setVisibility(View.INVISIBLE);
						    v2.setVisibility(View.INVISIBLE);
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
					else //if torch mode is not available light up the display in the new activity.
					{
						//cam.release();
						click=true;
						Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
						TorchActivity.this.startActivityForResult(intent,val);
					}
				}
				catch(RuntimeException e)
				{
					Toast.makeText(context,"Flash is used by some other application.Please close that application and try again.", Toast.LENGTH_LONG).show();
					click=true;
					Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
					TorchActivity.this.startActivityForResult(intent,val);
				}
			}
			else //Current state is ON
			{
				rl.setBackgroundResource(R.drawable.flashlight1);
				v1.setVisibility(View.INVISIBLE);
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
				rl.setBackgroundResource(R.drawable.backlight2);
				try
				{
				    cam=Camera.open(1);    
				    p = cam.getParameters();
				    click=true;
					List<String> flashModes = p.getSupportedFlashModes();
					if(flashModes.contains(Parameters.FLASH_MODE_TORCH))
					{
				    	v1.setVisibility(View.INVISIBLE);
					    v2.setVisibility(View.INVISIBLE);
						
						
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			    		cam.setParameters(p);
			    		cam.startPreview();
					}
					else
					{
						cam.release();
						cam=null;
						Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
						TorchActivity.this.startActivityForResult(intent,val);
					}
				}
				catch(RuntimeException e)
				{
					if(cam!=null)
					cam.release();
					cam=null;
					click = true;
					Intent intent=new Intent(TorchActivity.this,WhiteActivity.class);
					TorchActivity.this.startActivityForResult(intent,val);
					//Toast.makeText(context,"Flash is used by some other application.Please close that application and try again.", Toast.LENGTH_LONG).show();
				}
			}
			else //Current state is ON
			{
				v1.setVisibility(View.INVISIBLE);
			    v2.setVisibility(View.INVISIBLE);
			    rl.setBackgroundResource(R.drawable.flashlight1);
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
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
		if(flip==false) //Currently using back LED
		{
			flip=true;
			f1.setVisibility(View.INVISIBLE);
			f2.setVisibility(View.VISIBLE);
			if(click==true) //Current state is ON
			{
				rl.setBackgroundResource(R.drawable.flashlight1);
				v1.setVisibility(View.INVISIBLE);
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
				rl.setBackgroundResource(R.drawable.flashlight1);
				v1.setVisibility(View.INVISIBLE);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout);
			rl.setBackgroundResource(R.drawable.flashlight1);
			click = false;
		}
	}
}
