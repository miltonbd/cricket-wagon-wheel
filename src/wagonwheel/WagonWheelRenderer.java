package wagonwheel;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConstants.Field;

import newfeatures.FieldRunSegment;
import newfeatures.FieldRunSegmentRenderer;
import newfeatures.FourSixUpdate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.opengleswagonwheel.R;
import com.opengleswagonwheel.BallPathLine;
import com.opengleswagonwheel.Camera;
import com.opengleswagonwheel.AbstractOpenGLDraw;
import com.opengleswagonwheel.CricketGround;
import com.opengleswagonwheel.MyApp;
import com.opengleswagonwheel.OnAddRunSegmentText;
import com.utils.BallTrajectoryInAir;
import com.utils.ColorVector;
import com.utils.MyLogger;
import com.utils.OpenGLOperationParams;
import com.utils.ProjectileMotion3D;
import com.utils.RunTypes;
import com.utils.Vector3;
import com.utils.XMLParser;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class WagonWheelRenderer extends GLES20 {
	private WagonWheel wagonWheel = new WagonWheel();
	private Camera camera;
	private Document fieldPositionDom;
	private String KEY_ITEM = "wagon_ball", KEY_RUN_TYPE = "run_type",
			KEY_START_POSITION = "start_position",
			KEY_END_POSITION = "end_position";
	private String KEY_NAME = "name", KEY_AIR_POSITION = "air_position",
			KEY_MAX_HEIGHT = "max_height";
	private String KEY_X = "x", KEY_Y = "y", KEY_Z = "z";
	private Context context;
	private HashMap<FieldRunSegment, Integer> fieldSegmentRunMap = new HashMap<FieldRunSegment, Integer>();
	private List<FieldRunSegment> fieldRunSegments;
	private FieldRunSegmentRenderer fieldRunSegmentRenderer;
	private RelativeLayout relativeLayout;
	

	public WagonWheelRenderer(Camera camera, Context context, FieldRunSegmentRenderer fieldRunSegmentRenderer, RelativeLayout relativeLayout) {
		this.camera = camera;
		this.context = context;
		this.fieldRunSegmentRenderer = fieldRunSegmentRenderer;
		this.relativeLayout = relativeLayout;
		this.fieldRunSegments = fieldRunSegmentRenderer.getRunSegements();
		loadWagonWheels();
		MyLogger.log(wagonWheel.toString());
	}

	public void loadWagonWheels() {
		 int fourCount=0;
		 int sixCount=0;
		XMLParser xmlParser = new XMLParser(context);
		fieldPositionDom = xmlParser.loadRawXML(R.raw.wagon_wheel_1);
		NodeList nl = fieldPositionDom.getElementsByTagName(KEY_ITEM);
		// looping through all item nodes <item>
		for (FieldRunSegment fieldRunSegment : fieldRunSegments) {
					fieldSegmentRunMap.put(fieldRunSegment, 0);
		}
		
		for (int i = 0; i < nl.getLength(); i++) {
			Element wagonBallFromXML = (Element) nl.item(i);
			String runType = xmlParser.getValue(wagonBallFromXML, KEY_RUN_TYPE); // name
																					// child
																					// value
			WagonWheelBall wagonBall = new WagonWheelBall();
			wagonBall.setRunType(runType);
			if(Integer.parseInt(runType)==4) {
				fourCount++;
			}
			if(Integer.parseInt(runType)==6) {
				sixCount++;
			}

			Element startPositionXML = (Element) wagonBallFromXML
					.getElementsByTagName("start_position").item(0);

			float startX = Float
					.valueOf(xmlParser.getValue(startPositionXML, KEY_X));
			float startY = Float
					.valueOf(xmlParser.getValue(startPositionXML, KEY_Y));
		//	startY=startY+15;
			float startZ = Float
					.valueOf(xmlParser.getValue(startPositionXML, KEY_Z));

			Vector3 startPosition = new Vector3(startX, startY, startZ);
			Element endPositionXML = (Element) wagonBallFromXML
					.getElementsByTagName("end_position").item(0);

			
			float endX = Float.valueOf(xmlParser.getValue(endPositionXML, KEY_X));
			float endY = Float.valueOf(xmlParser.getValue(endPositionXML, KEY_Y));
			float endZ = Float.valueOf(xmlParser.getValue(endPositionXML, KEY_Z));
			float theta = (float) Math.toDegrees(Math.atan2((endY)*CricketGround.a, endX*CricketGround.b));
			if (theta < 0) {
				theta = (theta + 360) % 360;
			}
			
			for (FieldRunSegment fieldRunSegment : fieldRunSegments) {
				if (fieldRunSegment.isRunInsideThisSegment(theta)) {
					MyLogger.log("Runs Field Zone Found.");
					if (!fieldSegmentRunMap.containsKey(fieldRunSegment)) {
						fieldSegmentRunMap.put(fieldRunSegment, 0);
					}
					int totalRun = fieldSegmentRunMap.get(fieldRunSegment)
							+ Integer.parseInt(runType);
					fieldSegmentRunMap.put(fieldRunSegment, totalRun);
//					MyLogger.log(fieldRunSegment.getStartDegree() + " to "
//							+ fieldRunSegment.getEndDegree() + " has runs: "
//							+ totalRun);
					// break;
				}
			}
			
			Vector3 endPosition = new Vector3(endX, endY, endZ);
			Vector3 ballDroppedInGround = startPosition;

			NodeList airPositionXML = (NodeList) wagonBallFromXML
					.getElementsByTagName("air_position");
			MyLogger.log("Air Position has children "
					+ airPositionXML.getLength());
			if (airPositionXML != null && airPositionXML.getLength() > 0) {
				for (int j = 0; j < airPositionXML.getLength(); j++) {
					Element AirPositionXML = (Element) airPositionXML.item(j);
					Element startPositionAirXML = (Element) AirPositionXML
							.getElementsByTagName("start_position").item(0);
					float maxHeight = Float.valueOf(xmlParser.getValue(
							AirPositionXML, KEY_MAX_HEIGHT));
					float xAir = Float.valueOf(xmlParser.getValue(
							startPositionAirXML, KEY_X));
					float yAir = Float.valueOf(xmlParser.getValue(
							startPositionAirXML, KEY_Y));
					float zAir = Float.valueOf(xmlParser.getValue(
							startPositionAirXML, KEY_Z));

					Vector3 startPositionAir = new Vector3(xAir, yAir, zAir);
					Element endPositionAirXML = (Element) AirPositionXML
							.getElementsByTagName("end_position").item(0);

					xAir = Float.valueOf(xmlParser.getValue(endPositionAirXML,
							KEY_X));
					yAir = Float.valueOf(xmlParser.getValue(endPositionAirXML,
							KEY_Y));
					zAir = Float.valueOf(xmlParser.getValue(endPositionAirXML,
							KEY_Z));

					Vector3 endPositionInAir = new Vector3(xAir, yAir, zAir);
					BallTrajectoryInAir trajectoryInAir = new BallTrajectoryInAir(
							camera, maxHeight, startPositionAir,
							endPositionInAir);
					trajectoryInAir.setRunType(wagonBall.getRunType());
					wagonBall.addBallTrajectoryInAir(trajectoryInAir);
					ballDroppedInGround = endPositionInAir;
				}
			}
			ColorVector runColor = wagonBall.getRunType().getColor();
			float verticesData3[] = { ballDroppedInGround.x,
					ballDroppedInGround.y, 0, runColor.getR(), runColor.getG(),
					runColor.getB(), runColor.getAlpha(), endPosition.x,
					endPosition.y, 0, runColor.getR(), runColor.getG(),
					runColor.getB(), runColor.getAlpha() };
			BallPathLine ballInGround = new BallPathLine(camera, verticesData3);
			wagonBall.setWagonWheelBallShape(ballInGround);
			wagonWheel.add(wagonBall);
		}

		EventBus.getDefault().post(new FourSixUpdate(fourCount, sixCount));

	}

	public void getRawFiles() {
		java.lang.reflect.Field[] fields = R.raw.class.getFields();
		// loop for every file in raw folder
		for (int count = 0; count < fields.length; count++) {

			int rid;
			try {
				rid = fields[count].getInt(fields[count]);
				// Use that if you just need the file name
				String filename = fields[count].getName();

				// Use this to load the file
				try {
					Resources res = context.getResources();
					InputStream in = res.openRawResource(rid);

					byte[] b = new byte[in.available()];
					in.read(b);
					// do whatever you need with the in stream
				} catch (Exception e) {
					// log error
				}
			} catch (IllegalAccessException | IllegalArgumentException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	

	public void onSurfaceCreated() {
		for (WagonWheelBall wagonWheelBall : wagonWheel) {
			AbstractOpenGLDraw wagonWheelBallShape = wagonWheelBall
					.getWagonWheelBallShape();
			if (wagonWheelBallShape != null) {
				wagonWheelBallShape.onSurfaceCreated();
			}

			List<BallTrajectoryInAir> ballTrajectoryInAir = wagonWheelBall
					.getBallTrajectoryInAir();
			if (ballTrajectoryInAir != null && ballTrajectoryInAir.size() > 0) {
				for (BallTrajectoryInAir ballInAir : ballTrajectoryInAir) {
					ballInAir.onSurfaceCreated();
				}
			}
		}

	}
	
	
	
public void showRunText() {
	for(Entry<FieldRunSegment, Integer> item :fieldSegmentRunMap.entrySet()) {
		FieldRunSegment key= item.getKey();
		Integer value = item.getValue();
		float runTextTheta=(key.getStartDegree()+key.getEndDegree())/2;
		float textX=(float) ((CricketGround.a-(CricketGround.a*.2))*Math.cos(Math.toRadians(runTextTheta)));
		float textY=(float) ((CricketGround.b-(CricketGround.b*.2))*Math.sin(Math.toRadians(runTextTheta)));
		MyLogger.log("showRunText: Run segment "+key+" , Run "+value);
		MyLogger.log("showRunText: Run segment textview position x "+textX+" , y "+textY);
		OnAddRunSegmentText runSegment= new OnAddRunSegmentText(String.valueOf(value),textX,textY,camera);
		EventBus.getDefault().post(runSegment );
		
	}
}
	public void onSurfaceChanged(Camera camera) {
		for (WagonWheelBall wagonWheelBall : wagonWheel) {
			AbstractOpenGLDraw wagonWheelBallShape = wagonWheelBall
					.getWagonWheelBallShape();
			if (wagonWheelBallShape != null) {
				wagonWheelBallShape.onSurfaceChanged();
			}
			List<BallTrajectoryInAir> ballTrajectoryInAir = wagonWheelBall
					.getBallTrajectoryInAir();
			if (ballTrajectoryInAir != null && ballTrajectoryInAir.size() > 0) {
				for (BallTrajectoryInAir ballInAir : ballTrajectoryInAir) {
					ballInAir.onSurfaceChanged(camera);
				}
			}
		}
		showRunText();
	}

	public void draw(OpenGLOperationParams params) {
		// MyLogger.log("Total Balls "+wagonWheel.size());
		for (WagonWheelBall wagonWheelBall : wagonWheel) {
			// Draw the triangle facing straight on.
			AbstractOpenGLDraw wagonWheelBallShape = wagonWheelBall
					.getWagonWheelBallShape();

			List<BallTrajectoryInAir> ballTrajectoryInAir = wagonWheelBall
					.getBallTrajectoryInAir();
			if (ballTrajectoryInAir != null && ballTrajectoryInAir.size() > 0) {
				for (BallTrajectoryInAir ballInAir : ballTrajectoryInAir) {
					Matrix.setIdentityM(ballInAir.mModelMatrix, 0);
					Matrix.rotateM(ballInAir.mModelMatrix, 0,
							params.rotateAngle, 0.0f, 0.0f, 1.0f);
					ballInAir.draw();
				}
			}
			Matrix.setIdentityM(wagonWheelBallShape.mModelMatrix, 0);
			Matrix.rotateM(wagonWheelBallShape.mModelMatrix, 0,
					params.rotateAngle, 0.0f, 0.0f, 1.0f);
			wagonWheelBallShape.draw();
		}
	}
}
