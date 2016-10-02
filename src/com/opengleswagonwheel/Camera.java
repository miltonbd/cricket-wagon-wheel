package com.opengleswagonwheel;

import android.opengl.Matrix;

public class Camera {
	private int height;
	private int width;
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	// Position the eye behind the origin.
	// Position the eye behind the origin.
	final float eyeX = 0.0f;
	final float eyeY = 40.0f;
	final float eyeZ = 80f;

	// We are looking toward the distance
	final float lookX = 0.0f;
	final float lookY = 0.0f;
	final float lookZ = -5.0f;

	// Set our up vector. This is where our head would be pointing were we holding the camera.
	final float upX = 0.0f;
	final float upY = 1.0f;
	final float upZ = 0.0f;

			// Set the view matrix. This matrix can be said to represent the camera position.
			// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
			// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		public Camera() {
			
		}
		
		public float[] getViewMatrix(float[] mViewMatrix) {
			 Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
			 return mViewMatrix;
		}

		public float[] getFrustrum(float[] mProjectionMatrix) {
			// Create a new perspective projection matrix. The height will stay the same
			// while the width will vary as per aspect ratio.
			final float left = -10;
			final float right = 10;
			final float bottom = -10.0f;
			final float top = 10.0f;
			final float near = 8.0f;
			final float far =500.0f;
			Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);	
			
			return mProjectionMatrix;
		}
		public float[] getFrustrum(float[] mProjectionMatrix, int width, int height) {
			// Create a new perspective projection matrix. The height will stay the same
			// while the width will vary as per aspect ratio.
		float ratio= height*1.f/width;
			final float left = -10*ratio;
			final float right = 10*ratio;
			final float bottom = -10.0f;
			final float top = 10.0f;
			final float near = 8.0f;
			final float far =500.0f;
			Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);	
			return mProjectionMatrix;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

}
