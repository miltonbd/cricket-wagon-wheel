package com.utils;

public class Vector3 {
	public float z;
	public float x;
	public float y;

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "Vector3 [z=" + z + ", x=" + x + ", y=" + y + "]";
	}
}
