package com.opengleswagonwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.utils.MyLogger;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class CricketGround extends AbstractOpenGLDraw {
	public static int fieldWidth = 137; // Cricket Field Width or left to right
	public static int fieldHeight = 157; // Cricket Field Width or top to  bottom
	public static int a = (int) (fieldWidth * 1.0f / 2);
	public static int b = (int) (fieldHeight * 1.0f / 2);
	public static int centerX=0; // 
	public static int centerY=15; // Positive Y-Axis shift of the pitch from center
	final float[] verticesData = new float[7*360];

	public CricketGround(Camera camera) {
		setCamera(camera);
		int stride = 7;
		for (int i = 0; i < 360; i++) {
			float x = (float) (a*Math.cos(Math.toRadians(i)));
			float y = (float) (b*Math.sin(Math.toRadians(i)));
			int j=0;
			verticesData[i*stride+ j] = x;
			verticesData[(i*stride)+ ++j] =  y;
			verticesData[(i*stride)+ ++j] = 0;
			verticesData[(i*stride)+ ++j] = 1;
			verticesData[(i*stride)+ ++j] = 1f;
			verticesData[(i*stride)+ ++j] = 1;
			verticesData[(i*stride)+ ++j] = 1f;
			
			MyLogger.log("Cricket Ground a, b="+a+", "+b+" x,y"+x+", "+y);
		}

		// Initialize the buffers.
		verticesFloatBuffer = ByteBuffer
				.allocateDirect(verticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		verticesFloatBuffer.put(verticesData).position(0);

	}
	

	public static float getRadiusFromAngle(float theta) {
		/**
		 * r²=a²b²/(b²cos^theta-a²sin²theta)
		 */
		double b_2 = Math.pow(b, 2);
		double a_2 = Math.pow(a, 2);
		double b_2cos_2 = b_2 * Math.pow(Math.cos(Math.toRadians(theta)), 2);
		double b_2sin_2 = a_2 * Math.pow(Math.sin(Math.toRadians(theta)), 2);
		double term = (a_2 * b_2) / (b_2cos_2 - b_2sin_2);
		float r =0;
		if(term>0) {
			r=(float) Math.sqrt(term);
		} else {
			r=(float) Math.cbrt(term);
		}

		return r;
	}

	public void draw() {
		// Pass in the position information
		verticesFloatBuffer.position(mPositionOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, verticesFloatBuffer);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		verticesFloatBuffer.position(mColorOffset);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, verticesFloatBuffer);

		GLES20.glEnableVertexAttribArray(mColorHandle);

		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix 
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glLineWidth(4);
		GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, 360);
	}

	
	

	
	public void onSurfaceCreated() {
		getCamera().getViewMatrix(mViewMatrix);
		int programHandle = Shaders.compileShaders(Shaders.vertexShader,
				Shaders.fragmentShader);
		// Set program handles. These will later be used to pass in values to
		// the program.
		mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle,
				"u_MVPMatrix");
		mPositionHandle = GLES20.glGetAttribLocation(programHandle,
				"a_Position");
		mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
		// Tell OpenGL to use this program when rendering.
		GLES20.glUseProgram(programHandle);
	}

	public void onSurfaceChanged(Camera camera) {
		camera.getFrustrum(mProjectionMatrix);
	}
}
