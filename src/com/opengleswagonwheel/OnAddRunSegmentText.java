package com.opengleswagonwheel;

public class OnAddRunSegmentText {
	private String text;
	private float leftMargin;
	private float topMargin;
	private Camera camera;
	public Camera getCamera() {
		return camera;
	}
	public OnAddRunSegmentText(String text,float textX, float textY, Camera camera) {
		this.text = text;
		this.leftMargin = textX;
		this.topMargin = textY;
		this.camera = camera;
	}
	public String getText() {
		return text;
	}
	public float getX() {
		return leftMargin;
	}
	public float getY() {
		return topMargin;
	}
}
