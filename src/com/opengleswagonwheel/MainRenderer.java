package com.opengleswagonwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import newfeatures.FieldRunSegmentRenderer;

import com.android.texample2.MyTextRender;
import com.utils.MyLogger;
import com.utils.OpenGLOperationParams;

import de.greenrobot.event.EventBus;
import wagonwheel.FieldPosition;
import wagonwheel.WagonWheelRenderer;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class MainRenderer implements GLSurfaceView.Renderer 
{
	private Camera camera;
	private FieldPlain fieldPlain;
	private CricketGround ground;
	private CricketPitch pitch;
	private WagonWheelRenderer wagonWheelRenderer;
	private FieldPositionRenderer fieldPositionsRender;
	private Context context;
	private FieldRunSegmentRenderer fieldRunSegmentRenderer;
	private int width;
	private int height;
	private RelativeLayout relativeLayout;
	private MyGLSurfaceView myGLSurfaceView;
	public MainRenderer(Context context, RelativeLayout relativeLayout)
	{	
		this.context = context;
		this.relativeLayout = relativeLayout;
		camera = new Camera();
		fieldPlain = new FieldPlain(camera);
		ground = new CricketGround(camera);
		pitch = new CricketPitch(camera);
		fieldPositionsRender=new FieldPositionRenderer(context,camera);
		fieldRunSegmentRenderer=new FieldRunSegmentRenderer(camera, context);
		wagonWheelRenderer = new WagonWheelRenderer(camera,context,fieldRunSegmentRenderer,relativeLayout );
	}
	

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		// Set the background clear color to gray.
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		fieldPlain.onSurfaceCreated();
		ground.onSurfaceCreated();
	
		wagonWheelRenderer.onSurfaceCreated();
		fieldRunSegmentRenderer.onSurfaceCreated(glUnused,config);
		fieldPositionsRender.onSurfaceCreated();
		pitch.onSurfaceCreated();
	}	
	
	public void showRunText() {
		wagonWheelRenderer.showRunText();
	}
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{
		this.width = width;
		this.height = height;
		// Set the OpenGL viewport to the same size as the surface.
		float fieldRatio = CricketGround.b*1.0f/CricketGround.a;
		float newHeight=fieldRatio*width;
		GLES20.glViewport(0, 0, width, (int)newHeight);
		camera.setHeight((int) newHeight);
		camera.setWidth(width);
		fieldPlain.onSurfaceChanged(camera);
		ground.onSurfaceChanged(camera);
	
		fieldPositionsRender.onSurfaceChanged(camera);
		fieldRunSegmentRenderer.onSurfaceChanged(camera,glUnused,width,height);
		pitch.onSurfaceChanged(camera);
		wagonWheelRenderer.onSurfaceChanged(camera);
			EventBus.getDefault().post(new OnNewSurfaceSizeSet(width, newHeight));
	}	

	@Override
	public void onDrawFrame(GL10 glUnused) 
	{
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);			        
//		myTextRender.draw();
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        angleInDegrees=0;
        // Draw the triangle facing straight on.
        Matrix.setIdentityM(fieldPlain.mModelMatrix, 0);
        Matrix.rotateM(fieldPlain.mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);        
        fieldPlain.draw();
        
        // Draw the triangle facing straight on.
        Matrix.setIdentityM(ground.mModelMatrix, 0);
        Matrix.rotateM(ground.mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);        
        ground.draw();
        
        // Draw the triangle facing straight on.
        Matrix.setIdentityM(pitch.mModelMatrix, 0);
        Matrix.rotateM(pitch.mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);        
        pitch.draw();
        
        OpenGLOperationParams params = new OpenGLOperationParams();
        
        fieldPositionsRender.draw(params);
        fieldRunSegmentRenderer.draw(params);
      //  params.rotateAngle=angleInDegrees;
        wagonWheelRenderer.draw(params);
	}


	public void setGLSurafceView(MyGLSurfaceView myGLSurfaceView) {
		this.myGLSurfaceView = myGLSurfaceView;
	}	
}


