package com.android.texample2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class Texample2Renderer implements GLSurfaceView.Renderer  {

	private static final String TAG = "TexampleRenderer";
	private MyTextRender myTextRender;
	

	public Texample2Renderer(Context context)  {
		super();
		myTextRender = new MyTextRender(context);           
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		myTextRender.onCreated();

	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		int clearMask = GLES20.GL_COLOR_BUFFER_BIT;
		GLES20.glClear(clearMask);
		myTextRender.draw();

	                                 // End Text Rendering
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) { //		gl.glViewport( 0, 0, width, height ); 
		GLES20.glViewport(0, 0, width, height);
		myTextRender.onSurfaceChanged(width, height);

	}
}
