package com.opengleswagonwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;


public class BallPathLine extends AbstractOpenGLDraw {
	public BallPathLine(Camera camera, float[] verticesData) {
		setCamera(camera);
		// Initialize the buffers.
		verticesFloatBuffer = ByteBuffer
				.allocateDirect(verticesData.length*lineWidth * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		 float[] verticesDataNew = new float[verticesData.length*lineWidth *7];
		 int stride =14;
		 float sdPerc=.02f;
		for (int i = 0; i < lineWidth; i++) {
			verticesDataNew[i*stride+0]=verticesData[0]+sdPerc*verticesData[0];
			verticesDataNew[i*stride+1]=verticesData[1]+sdPerc*verticesData[1];
			verticesDataNew[i*stride+2]=verticesData[2];
			verticesDataNew[i*stride+3]=verticesData[3];
			verticesDataNew[i*stride+4]=verticesData[4];
			verticesDataNew[i*stride+5]=verticesData[5];
			verticesDataNew[i*stride+6]=verticesData[6];
			verticesDataNew[i*stride+7]=verticesData[7]+sdPerc*verticesData[7];
			verticesDataNew[i*stride+8]=verticesData[8]+sdPerc*verticesData[8];
			verticesDataNew[i*stride+9]=verticesData[9];
			verticesDataNew[i*stride+10]=verticesData[10];
			verticesDataNew[i*stride+11]=verticesData[11];
			verticesDataNew[i*stride+12]=verticesData[12];
			verticesDataNew[i*stride+13]=verticesData[13];
			sdPerc+=.02;
		}
		verticesFloatBuffer.put(verticesData).position(0);
	}

	public void draw() {
		//onSurfaceCreated();
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
		GLES20.glLineWidth(3);
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2*lineWidth);
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


}
