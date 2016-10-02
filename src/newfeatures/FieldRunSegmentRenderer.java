package newfeatures;


import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import wagonwheel.FieldPositionPojo;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.opengleswagonwheel.R;
import com.opengleswagonwheel.Camera;
import com.utils.MyLogger;
import com.utils.OpenGLOperationParams;
import com.utils.XMLParser;

public class FieldRunSegmentRenderer extends GLES20 {
	public static int numberSegment=8;
	public static float runSegmentIncrement=360.f/numberSegment;
	private float startDegree;
	private float endDegree;
	private FieldRunSegments runSegements = new FieldRunSegments();
	private Camera camera;
	private  float[] verticesData= new float[28];
	private Context context;
	private String KEY_ITEM = "run_segment";
	private String KEY_NAME = "name";
	private String KEY_START_THETA = "start_theta";
	private String KEY_END_THETA = "end_theta";
	private Document fieldPositionDom;
	
	public FieldRunSegmentRenderer(Camera camera, Context context) {
		this.camera = camera;
		this.context = context;
		load();
	}
	public FieldRunSegments getRunSegements() {
		return runSegements;
	}
	
	public float getStartDegree() {
		return startDegree;
	}
	public float getEndDegree() {
		return endDegree;
	}
	static double truncateTo( double unroundedNumber, int decimalPlaces ){
	    int truncatedNumberInt = (int)( unroundedNumber * Math.pow( 10, decimalPlaces ) );
	    double truncatedNumber = (double)( truncatedNumberInt / Math.pow( 10, decimalPlaces ) );
	    return truncatedNumber;
	}
	
	private  void load() {
		List<FieldPositionPojo> fieldPositionsPojo = new ArrayList<FieldPositionPojo>();
		XMLParser xmlParser = new XMLParser(context);
		fieldPositionDom = xmlParser.loadRawXML(R.raw.field_run_segment);
		NodeList nl = fieldPositionDom.getElementsByTagName(KEY_ITEM);
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			String name = xmlParser.getValue(e, KEY_NAME); // name child value
			Float startTheta =Float.valueOf( xmlParser.getValue(e, KEY_START_THETA)); // cost child value
			Float endTheta = Float.valueOf(xmlParser.getValue(e, KEY_END_THETA)); // description child value
			FieldPositionPojo fieldPositionPojo = new FieldPositionPojo();
			fieldPositionPojo.setName(name);
			fieldPositionPojo.setX(Float.valueOf(startTheta));
			fieldPositionPojo.setY(Float.valueOf(endTheta));
			fieldPositionsPojo.add(fieldPositionPojo);
			FieldRunSegment fieldPosition = new FieldRunSegment(camera, startTheta, endTheta, fieldPositionPojo.getX() );
		    runSegements.add(fieldPosition);
		}
		MyLogger.log("All Run Segment "+runSegements.size());
	}
	
	@Override
	public String toString() {
		return "FieldRunSegment [startDegree=" + startDegree + ", endDegree="
				+ endDegree + "]";
	}
	
	
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		for (FieldRunSegment wagonWheelBall : runSegements) {
			if (wagonWheelBall!=null) {
				wagonWheelBall.onSurfaceCreated( );
				wagonWheelBall.onTextSurfaceCreated(glUnused,  config);
			}
		}
	}

	public void onSurfaceChanged(Camera camera,GL10 glUnused, int width, int height) {
		for (FieldRunSegment fieldRunSegment : runSegements) {
			if (fieldRunSegment!=null) {
				fieldRunSegment.onSurfaceChanged();
				fieldRunSegment.onTextSurfaceChanged( glUnused,  width,  height);
			}
		}
	}

	public void draw(OpenGLOperationParams params) {
		//MyLogger.log("Total Balls "+wagonWheel.size());
		for (FieldRunSegment item : runSegements) {
			 // Draw the triangle facing straight on.
	  
			Matrix.setIdentityM(item.mModelMatrix, 0);
		    Matrix.rotateM(item.mModelMatrix, 0, params.rotateAngle, 0.0f, 0.0f, 1.0f);
		    item.draw();
		}
	}
}
