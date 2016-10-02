package com.opengleswagonwheel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import wagonwheel.FieldPosition;
import wagonwheel.FieldPositionPojo;
import wagonwheel.WagonWheelBall;
import android.content.Context;
import android.opengl.Matrix;

import com.example.opengleswagonwheel.R;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.utils.BallTrajectoryInAir;
import com.utils.DatabaseHelper;
import com.utils.OpenGLOperationParams;
import com.utils.XMLParser;

public class FieldPositionRenderer {
	private int fieldWidth = 157;
	private int fieldHeight = 137;
	private int a = (int) (fieldWidth * 1.0f / 2);
	private int b = (int) (fieldHeight * 1.0f / 2);
	private float[] verticesData;
	private Document fieldPositionDom;
	private String KEY_ITEM = "field_position";
	private String KEY_NAME = "name";
	private String KEY_X = "x";
	private String KEY_Y = "y";
	private String KEY_SELECTED="selected";
	private List<FieldPosition> fieldPositions = new ArrayList<FieldPosition>();
	private DatabaseHelper databaseHelper;
	private Dao<FieldPositionPojo, Integer> dao;

	public FieldPositionRenderer(Context context, Camera camera) {
		List<FieldPositionPojo> fieldPositionsPojo = new ArrayList<FieldPositionPojo>();
		XMLParser xmlParser = new XMLParser(context);
		fieldPositionDom = xmlParser.loadRawXML(R.raw.field_position);
		NodeList nl = fieldPositionDom.getElementsByTagName(KEY_ITEM);
		databaseHelper = DatabaseHelper.getInstance(context);
		try {
			dao = databaseHelper.getDao();
			TableUtils.clearTable(databaseHelper.getConnectionSource(), FieldPositionPojo.class);
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				String name = xmlParser.getValue(e, KEY_NAME); // name child value
				String x = xmlParser.getValue(e, KEY_X); // cost child value
				String y = xmlParser.getValue(e, KEY_Y); // description child value
				FieldPositionPojo fieldPositionPojo = new FieldPositionPojo();
				fieldPositionPojo.setName(name);
				fieldPositionPojo.setX(Float.valueOf(x));
				fieldPositionPojo.setY(Float.valueOf(y));
				fieldPositionsPojo.add(fieldPositionPojo);
				FieldPosition fieldPosition = new FieldPosition(context, camera, fieldPositionPojo);
				fieldPositions.add(fieldPosition);
				try {
					dao.create(fieldPositionPojo);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	public void onSurfaceCreated() {
		for (FieldPosition wagonWheelBall : fieldPositions) {
			if (wagonWheelBall != null) {
				wagonWheelBall.onSurfaceCreated();
			}
		}

	}

	public void onSurfaceChanged(Camera camera) {
		for (FieldPosition fieldPosition : fieldPositions) {
			if (fieldPosition != null) {
				fieldPosition.onSurfaceChanged();
			}
		}
	}

	public void draw(OpenGLOperationParams params) {
		// MyLogger.log("Total Balls "+wagonWheel.size());
		for (FieldPosition wagonWheelBall : fieldPositions) {
			// Draw the triangle facing straight on.
			Matrix.setIdentityM(wagonWheelBall.mModelMatrix, 0);
			Matrix.rotateM(wagonWheelBall.mModelMatrix, 0,
					params.rotateAngle, 0.0f, 0.0f, 1.0f);
			wagonWheelBall.draw();
		}
	}

}
