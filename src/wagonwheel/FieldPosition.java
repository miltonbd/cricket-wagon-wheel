package wagonwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.opengleswagonwheel.R;
import com.opengleswagonwheel.Camera;
import com.opengleswagonwheel.FieldPositionRenderer;
import com.opengleswagonwheel.Shaders;
import com.opengleswagonwheel.AbstractOpenGLDraw;
import com.utils.MyLogger;
import com.utils.XMLParser;

public class FieldPosition extends AbstractOpenGLDraw {
	private int fieldWidth = 157;
	private int fieldHeight = 137;
	private int a= (int)(fieldWidth*1.0f/2);
	private int b=(int)(fieldHeight*1.0f/2);
	private float[] verticesData;
	private Document fieldPositionDom;
	private String KEY_ITEM="field_position";
	private String KEY_NAME="name";
	private String KEY_X="r";
	private String KEY_Y="theta";
	private Camera camera2;
	private int interpolationCount=36;
	
	
public FieldPosition(Context context, Camera camera, FieldPositionPojo fieldPositionPojo) {
	camera2 = camera;
	this.setCamera(camera);
	int stride=7;
	verticesData = new float[7*interpolationCount];
    	float x=fieldPositionPojo.getX();
    	float y=fieldPositionPojo.getY();
    	MyLogger.log("Field Position X, Y "+x+" , "+y);
    	int j=-1;
    	float radiusFielder=1;
    	int i=0;
    	for (int j2 = 1; j2 < 360; j2+=10) {
    		float finalX = x+(float) (radiusFielder*Math.cos(Math.toRadians(j2)));
    		float finalY = y+ (float) (radiusFielder*Math.sin(Math.toRadians(j2)));
    		MyLogger.log("Final X "+finalX+" , Final Y "+finalY);
    		verticesData[( i *stride)+ ++j] = finalX;
			verticesData[( i *stride)+ ++j] =finalY;
    		verticesData[(i * stride)+ ++j] = 0;
    		verticesData[(i * stride)+ ++j] = 1;
    		verticesData[(i * stride)+ ++j] = 0;
    		verticesData[(i * stride)+ ++j] = 0;
    		verticesData[(i * stride)+ ++j] = 1;
		}
    
// Initialize the buffers.
verticesFloatBuffer = ByteBuffer
		.allocateDirect(verticesData.length * mBytesPerFloat)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();

verticesFloatBuffer.put(verticesData).position(0);
}


public void getY() {
	
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
	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, interpolationCount );
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
}
