package com.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.opengleswagonwheel.Camera;
import com.opengleswagonwheel.Shaders;
import com.opengleswagonwheel.AbstractOpenGLDraw;


public class BallTrajectoryInAir extends AbstractOpenGLDraw  {
	private float range;
	private float maxHeight;
	private Vector3 endPoint;
	private float elevationAngleRadian = 0;
	private float velocity = 0;
	private float time;
	private List<Vector3> points = new ArrayList<Vector3>();
	private float azimuthAngleRadian;
	private double vH;
	private double vV;
	private double vE;
	private double vN;
	private Vector3 startPoint;
	private RunTypes runTypes;

	public BallTrajectoryInAir(Camera camera,  float maxHeight, Vector3 startPoint, Vector3 endPoint) {
		MyLogger.log("Start point "+startPoint);
		MyLogger.log("end point "+endPoint);
		this.startPoint = startPoint;
		setCamera(camera);
		this.range = (float) Math.sqrt(Math.pow(endPoint.x-startPoint.x, 2)+Math.pow(endPoint.y-startPoint.y, 2));
		this.maxHeight = maxHeight;
		this.endPoint = endPoint;
		getBallFireElevationAngle();
		getBallFireAzimuthAngle();
		getBallFireVelocity();
		getTime();
		calculateVelocityComponents();
	}

	
	public float getBallFireElevationAngle() {
		elevationAngleRadian = (float) Math.atan2(4 * maxHeight, range);
		MyLogger.log("elevation Angle in degree "+ Math.toDegrees(elevationAngleRadian));
		return elevationAngleRadian;
	}
	
	public float getBallFireAzimuthAngle() {
		azimuthAngleRadian = (float) Math.atan2(endPoint.y, endPoint.x);
		MyLogger.log("Azimuth Angle in degree "+ Math.toDegrees(azimuthAngleRadian));
		return azimuthAngleRadian;
	}

	public float getBallFireVelocity() {
		velocity = (float) Math.sqrt((2 * 9.8 * maxHeight)
				/ Math.pow(Math.sin(elevationAngleRadian), 2));
		MyLogger.log("Velocity "+ velocity);
		return velocity;
	}

	public float getTime() {
		float theta = (float) Math.atan2(4 * maxHeight, range);
		float velocity = (float) Math.sqrt((2 * 9.8 * maxHeight)
				/ Math.pow(Math.sin(theta), 2));
		float vx = (float) (velocity * Math.cos(theta));
		float vy = (float) (velocity * Math.sin(theta));
		time = (float) (2 * vy / 9.8);
		return time;
	}

	public void calculateVelocityComponents() {
		vH = velocity * Math.cos(elevationAngleRadian );
		vV = velocity * Math.sin(elevationAngleRadian );
		vE = vH * Math.cos( azimuthAngleRadian);
		vN = vH * Math.sin(azimuthAngleRadian );
	};

	
	private float getY(float t) {
		float y=(float) (velocity*t*Math.sin(elevationAngleRadian)-.5*9.8*Math.pow(t, 2));
		return y;
	}
	public void interpolate() {
		int totalTime=(int) (time+.5);
		for (float time = .2f; time <= totalTime; ) {
			float percent = time*1.0f/totalTime;
			float	x,y,z;
			 x=(float) (vE*time)+startPoint.x;
			 y=(float) (vN*time)+startPoint.y;
			 z=(float) (vV*time-4.9*time*time);
			
			if (x>Math.abs(endPoint.x)||y>Math.abs(endPoint.y)||Math.abs(z)<0) {
				x=endPoint.x;
			    y=endPoint.y;
			 z=0;
			}
			
			MyLogger.log("X "+x+" , Y "+y+" Z, "+z);
			Vector3 point = new Vector3(x,y, z);
			points.add(point);
			time=time+.2f;
		}
		
		float [] verticesData = new float[points.size()*7];
		verticesData[0] = startPoint.x;
		verticesData[1] = startPoint.y;
		verticesData[2] = startPoint.z;
		ColorVector colorVctor = runTypes.getColor();
		verticesData[3] = colorVctor.getR();
		verticesData[4] = colorVctor.getG();
		verticesData[5] = colorVctor.getB();
		verticesData[6] = colorVctor.getAlpha();
		
		    for(int i =1; i <points.size(); i++){
		    	Vector3 point=points.get(i);
		    	verticesData[(i * 7)+ 0] = point.x;
		    	verticesData[(i * 7)+ 1] = point.y;
		    	verticesData[(i * 7)+ 2] = point.z;
		    	verticesData[(i * 7)+ 3] =colorVctor.getR();
		    	verticesData[(i * 7)+ 4] = colorVctor.getG();
		    	verticesData[(i * 7)+ 5] = colorVctor.getB();
		    	verticesData[(i * 7)+ 6] = colorVctor.getAlpha();
		    }
		    for (int i = 0; i < verticesData.length; i++) {
				
		    	MyLogger.log("Vertices data "+verticesData[i]);
			}
		    

		verticesFloatBuffer = ByteBuffer
				.allocateDirect(verticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		verticesFloatBuffer.put(verticesData).position(0);
		MyLogger.log("air trajecory data length: "+verticesData.length * mBytesPerFloat);
	}

	public List<Vector3> getPoints() {
		return points;
	}

	public void setPoints(List<Vector3> points) {
		this.points = points;
	}

	public void draw() {
		//MyLogger.log("drawing trajectory.");
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
		GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, points.size());
		
		// TODO build own geometry and render with triangles.
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


	public void setRunType(RunTypes runTypes) {
		this.runTypes = runTypes;
		interpolate();
	}
}


