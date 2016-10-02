package newfeatures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.android.texample2.GLText;
import com.opengleswagonwheel.AbstractOpenGLDraw;
import com.opengleswagonwheel.Camera;
import com.opengleswagonwheel.CricketGround;
import com.opengleswagonwheel.MyApp;
import com.opengleswagonwheel.Shaders;
import com.utils.MyLogger;

public class FieldRunSegment   extends AbstractOpenGLDraw{
	final float[] verticesData=new float[14];
	private float startDegree;
	private float endDegree;
	private GLText glText;
	private Camera camera2;
	private GL10 glUnused;
	@Override
	public String toString() {
		return "FieldRunSegment [startDegree=" + startDegree + ", endDegree="
				+ endDegree + ", radius=" + radius + "]";
	}


	private float radius;
	public FieldRunSegment(Camera camera, float startDegree, float endDegree, float radius) {
		camera2 = camera;
		this.radius = radius;
		int j=0;
		int a=CricketGround.a;
		int b=CricketGround.b;
		float x = (float) (a*Math.cos(Math.toRadians(startDegree)));
		float y = (float) (b*Math.sin(Math.toRadians(endDegree)));
		verticesData[ j] = 0;
		verticesData[ ++j] =  CricketGround.centerY;
		verticesData[ ++j] = 0;
		verticesData[ ++j] = .5f;
		verticesData[ ++j] = 1;
		verticesData[ ++j] = 0;
		verticesData[ ++j] = 1;
		
		 x = (float) (a*Math.cos(Math.toRadians(endDegree)));
		 y = (float) (b*Math.sin(Math.toRadians(endDegree)));
		verticesData[ ++j] = x;
		verticesData[ ++j] =  y;
		verticesData[ ++j] = 0;
		verticesData[ ++j] = .5f;
		verticesData[ ++j] = 1;
		verticesData[ ++j] = 0;
		verticesData[ ++j] = 1;
		
		this.startDegree = startDegree;
		this.endDegree = endDegree;
		setCamera(camera);
		// Initialize the buffers.
		verticesFloatBuffer = ByteBuffer
				.allocateDirect(verticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		verticesFloatBuffer.put(verticesData).position(0);
		MyLogger.log("Run Segment Start end x, y = "+x+" , "+y);
		
		
	}


	public float getStartDegree() {
		return startDegree;
	}


	public float getEndDegree() {
		return endDegree;
	}


	public void draw() {
		
//		
//		glText.drawTexture( camera2.getWidth()/2, camera2.getHeight()/2, mMVPMatrix);            // Draw the Entire Texture
//		
//		// TEST: render some strings with the font
//		glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mMVPMatrix );         // Begin Text Rendering (Set Color WHITE)
//		glText.drawC("Test String 3D!", 0f, 0f, 0f, 0, -30, 0);
////		glText.drawC( "Test String :)", 0, 0, 0 );          // Draw Test String
//		glText.draw( "Diagonal 1", 40, 40, 40);                // Draw Test String
//		glText.end();  
		
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
		GLES20.glLineWidth(2);
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
	
                                 // End Text Rendering

		
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
        
        
    	// Create the GLText
		glText = new GLText(MyApp.activity.getApplicationContext().getAssets());

		// Load the font from file (set size + padding), creates the texture
		// NOTE: after a successful call to this the font is ready for rendering!
		glText.load( "Roboto-Regular.ttf", 14, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

		// enable texture + alpha blending
		//GLES20.glEnable(GLES20.GL_BLEND);
		//GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
	}

	public void onSurfaceChanged(Camera camera) {
		camera.getFrustrum(mProjectionMatrix);
	}
	
	public boolean isRunInsideThisSegment(float degree) {
			boolean result = degree>=startDegree&&degree<endDegree;
			MyLogger.log("Testing run zone for "+degree+" between "+startDegree+" to "+endDegree+" = "+result);
			return	result;
	}


	public void onTextSurfaceCreated(GL10 glUnused, EGLConfig config) {
		this.glUnused = glUnused;
	}


	public void onTextSurfaceChanged(GL10 glUnused, int width, int height) {
		
	}


	public float getRadius() {
		return radius;
	}

}
