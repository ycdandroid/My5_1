package com.example.six;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class SurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = 180.0f/320; 
	SceneRenderer mRenderer;
	private float mPreX;
	private float mPreY;
	public SurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//		Log.e("surfaceview", "false");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e){
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreY;
			float dx = x - mPreX;
			for (SixPoint h : mRenderer.ha) {
				h.yAngle += dx * TOUCH_SCALE_FACTOR;
				h.xAngle += dy * TOUCH_SCALE_FACTOR;
			}
			break;

		default:
			break;
		}
		mPreX = x;
		mPreY = y;
		return true;
	}
	
	
	
	private class SceneRenderer implements GLSurfaceView.Renderer{
		SixPoint[] ha = new SixPoint[6];
		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			for(SixPoint h: ha){
				h.drawSelf();
			}
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			float radio = (float)width/height;
			MatrixState.setProjectOrtho(-radio, radio, -1, 1, 1, 10);
			MatrixState.setCamera(0, 0, 3f, 0, 0, 0f, 0f, 1.0f, 0.0f);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			for (int i = 0; i < ha.length; i++) {
				ha[i] = new SixPoint(SurfaceView.this, 0.2f, 0.5f, -0.3f*i);
			}
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//			Log.e("onSurfaceCreate", "false");
		}
		
		
	}

}
