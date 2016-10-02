package com.utils;

import android.content.Context;
import android.graphics.Color;

import com.opengleswagonwheel.MyApp;

public class ColorVector {
	private float r, g, b, alpha = 1;

	public ColorVector(int colorResourceID) {
		Context context = MyApp.activity.getApplicationContext();
		String strColor = context.getResources().getString(colorResourceID);
		int intColor = Color.parseColor(strColor);
		this.r = Color.red(intColor) / 255.0f;
		this.g = Color.green(intColor) / 255.0f;
		this.b = Color.blue(intColor) / 255.0f;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

}
