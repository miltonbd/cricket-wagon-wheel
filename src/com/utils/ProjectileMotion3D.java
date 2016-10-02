package com.utils;

public class ProjectileMotion3D {
	private float thetaDegree;
	private float phiDegree;
	private float velocity;
	private double vH;
	private double vV;
	private double tmax;
	private double vE;
	private double vN;
	private double xmax;
	private double ymax;

	public ProjectileMotion3D(float velocity, float thetaDegree, float phiDegree, float xmax, float ymax) {
		this.velocity = velocity;
		this.thetaDegree = thetaDegree;
		this.phiDegree = phiDegree;
		this.xmax = xmax;
		this.ymax = ymax;
		calculate();
		interpolate();
	}

	public void calculate() {
		vH = velocity * Math.cos(Math.PI * thetaDegree / 180);
		vV = velocity * Math.sin(Math.PI * thetaDegree / 180);
		tmax = vV / 4.9;
		vE = vH * Math.cos(Math.PI * phiDegree / 180);
		vN = vH * Math.sin(Math.PI * phiDegree / 180);
		tmax = vE /xmax ;
	};

	public void interpolate() {
		int totalTime=(int) (tmax+.5);
		for (float time = .2f; time <= totalTime; ) {
			float percent = time*1.0f/totalTime;
			float	x=(float) (vE*time);
			float y=(float) (vN*time);
			float z=(float) (vV*time-4.9*time*time);
		MyLogger.log("X "+x+" , Y "+y+" Z "+z);
			Vector3 point = new Vector3(x,y, 0);
			time=time+.2f;
		}
	} 
}
