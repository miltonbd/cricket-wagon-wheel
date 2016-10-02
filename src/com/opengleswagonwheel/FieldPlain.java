package com.opengleswagonwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class FieldPlain extends AbstractOpenGLDraw {

	public FieldPlain(Camera camera) {
		setCamera(camera);
		final float[] verticesData = {
				// X, Y, Z,
				// R, G, B, A
				-100f, -100f, 0.0f, 0.0f, .5f, 0.0f, 1f,

				100f, -100f, 0.0f, 0.0f, .5f, 0.0f, 1f,

				100f, 100f, 0.0f, 0.0f, .5f, 0.0f, 1f,
				-100f, 100f, 0.0f, 0.0f, 0.5f, 0.0f, 1f,};

		// Initialize the buffers.
		verticesFloatBuffer = ByteBuffer
				.allocateDirect(verticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		verticesFloatBuffer.put(verticesData).position(0);

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
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
	}

	public void onSurfaceCreated() {
		getCamera().getViewMatrix(mViewMatrix);
		int programHandle =Shaders.compileShaders(Shaders.vertexShader, Shaders.fragmentShader);
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");        
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");        
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle); 		
	}

	public void onSurfaceChanged(Camera camera) {
		camera.getFrustrum(mProjectionMatrix);
	}

	public void onSurfaceChanged(Camera camera, int width, int height) {
		camera.getFrustrum(mProjectionMatrix, width, height);
	}
}
