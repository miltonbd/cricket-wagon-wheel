package com.opengleswagonwheel;

import newfeatures.MultisampleConfigChooser;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

public class MyGLSurfaceView extends GLSurfaceView  implements GestureDetector.OnGestureListener, OnTouchListener{
	private MainRenderer mRenderer;
	private GestureDetector gestureScanner;
	private LayoutParams params;

	public MyGLSurfaceView(Context context, MainRenderer mRenderer) {
		super(context);
		this.mRenderer = mRenderer;
		mRenderer.setGLSurafceView(this);
        init(context);
	}
	
	private void init(Context context) {
		// Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        MultisampleConfigChooser config = new MultisampleConfigChooser();
        setEGLConfigChooser(config);
      
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer( mRenderer);
        gestureScanner = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
       
	}

	public MyGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    init(context);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
	        if (gestureScanner.onTouchEvent(event))
	            return gestureScanner.onTouchEvent(event);
	        else
	            return false;
	}

}
