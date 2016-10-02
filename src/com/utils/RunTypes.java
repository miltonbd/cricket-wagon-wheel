package com.utils;

import com.example.opengleswagonwheel.R;

public enum RunTypes {
	ZERO(new ColorVector(R.color.colorRun0)),
	ONE(new ColorVector(R.color.colorRun1)),
	TWO(new ColorVector(R.color.colorRun2)),
	THREE(new ColorVector(R.color.colorRun3)),
	FOUR(new ColorVector(R.color.colorRun4)), 
	SIX(new ColorVector(R.color.colorRun6));
	private ColorVector color;

	RunTypes(ColorVector color) {
		this.color = color;
	}

	public ColorVector getColor() {
		return color;
	}
}
