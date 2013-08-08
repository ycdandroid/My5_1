package com.example.six;

import com.example.my5_1.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	SurfaceView surfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ÉèÖÃÎªÈ«ÆÁ
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		surfaceView = new SurfaceView(this);
		setContentView(surfaceView);
		
		surfaceView.requestFocus();
		surfaceView.setFocusableInTouchMode(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		surfaceView.onResume();
	}
	
	public void onPause(){
		super.onPause();
		surfaceView.onPause();
	}

}
