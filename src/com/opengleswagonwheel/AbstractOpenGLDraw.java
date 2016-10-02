package com.opengleswagonwheel;

import java.nio.FloatBuffer;

public abstract class AbstractOpenGLDraw {
	protected int lineWidth=6;
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	public float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	protected float[] mViewMatrix = new float[16];

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	protected float[] mProjectionMatrix = new float[16];
	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	protected float[] mMVPMatrix = new float[16];
	
	/** How many bytes per float. */
	protected final int mBytesPerFloat = 4;
	
	/** How many elements per vertex. */
	protected final int mStrideBytes = 7 * mBytesPerFloat;	
	
	/** Offset of the position data. */
	protected final int mPositionOffset = 0;
	
	/** Size of the position data in elements. */
	protected final int mPositionDataSize = 3;
	
	/** Offset of the color data. */
	protected final int mColorOffset = 3;
	
	/** Size of the color data in elements. */
	protected final int mColorDataSize = 4;	
	/** Store our model data in a float buffer. */
	protected FloatBuffer verticesFloatBuffer=null;

	/** This will be used to pass in the transformation matrix. */
	protected int mMVPMatrixHandle;

	/** This will be used to pass in model position information. */
	protected int mPositionHandle;

	/** This will be used to pass in model color information. */
	protected int mColorHandle;
	private Camera camera;
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	public void onSurfaceChanged(Camera camera) {
		camera.getFrustrum(mProjectionMatrix);
	}
	public void onSurfaceChanged() {
		camera.getFrustrum(mProjectionMatrix);
	}
	public abstract void onSurfaceCreated();
	public abstract void draw();
}