package com.android.texample2;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class MyTextRender {
	private GLText glText;                             // A GLText Instance
	private Context context;                           // Context (from Activity)
	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mVPMatrix = new float[16];
	public MyTextRender(Context context)  {
		super();
		this.context = context;                         // Save Specified Context
	}
	public void onCreated() {
		// Create the GLText
		glText = new GLText(context.getAssets());

		// Load the font from file (set size + padding), creates the texture
		// NOTE: after a successful call to this the font is ready for rendering!
		glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

		// enable texture + alpha blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}
	public void draw() {
		
		Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		
	
		// TEST: render some strings with the font
		glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color WHITE)
		glText.drawC("Test String 3D!", 0f, 0f, 0f, 0, -30, 0);
//		glText.drawC( "Test String :)", 0, 0, 0 );          // Draw Test String
		glText.draw( "Diagonal 1", 40, 40, 40);                // Draw Test String
		glText.draw( "Column 1", 100, 100, 90);              // Draw Test String
		glText.end();                                   // End Text Rendering
		
		glText.begin( 0.0f, 0.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color BLUE)
		glText.draw( "More Lines...", 50, 200 );        // Draw Test String
		glText.draw( "The End.", 50, 200 + glText.getCharHeight(), 180);  // Draw Test String
		glText.end();  
	}
	
	public void onSurfaceChanged(int width, int height)  {
		float ratio = (float) width / height;
		// Take into account device orientation
		if (width > height) {
			Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
		}
		else {
			Matrix.frustumM(mProjMatrix, 0, -1, 1, -1/ratio, 1/ratio, 1, 10);
		}
		
		
		int useForOrtho = Math.min(width, height);
		
		//TODO: Is this wrong?
		Matrix.orthoM(mVMatrix, 0,
				-useForOrtho/2,
				useForOrtho/2,
				-useForOrtho/2,
				useForOrtho/2, 0.1f, 100f);
	}
	
}