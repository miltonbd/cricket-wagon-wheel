package wagonwheel;

import com.opengleswagonwheel.BallPathLine;
import com.opengleswagonwheel.Camera;
import com.utils.BallTrajectoryInAir;
import com.utils.RunTypes;
import com.utils.Vector3;

public class WagonWheelDataLoader {
	private WagonWheel wagonWheel = new WagonWheel();
	private Camera camera;
	

	public WagonWheel loadWagonWheel(Camera camera) {
		this.camera = camera;
		
		
		return wagonWheel;
	}
//	
//	public void single1() {
//		WagonWheelBall ball1 = new WagonWheelBall();
//		Vector3 endPointBall1 = new Vector3(-30f, 30f, 0);
//		ball1.setEndPoint(endPointBall1);
//		ball1.setRunType(RunTypes.ONE);
//		float verticesData[] = { 0, 0, 0, 0, 0, 1, 1, endPointBall1.x,
//				endPointBall1.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine = new BallPathLine(camera, verticesData);
//		ball1.setWagonWheelBallShape(singleRunLine);
//		wagonWheel.add(ball1);
//	}
//	
//	public void single2() {
//		WagonWheelBall ball1 = new WagonWheelBall();
//		Vector3 endPointBall1 = new Vector3(-30f,-40f, 0);
//		ball1.setEndPoint(endPointBall1);
//		ball1.setRunType(RunTypes.ONE);
//		float verticesData[] = { 0, 0, 0, 0, 0, 1, 1, endPointBall1.x,
//				endPointBall1.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine = new BallPathLine(camera, verticesData);
//		ball1.setWagonWheelBallShape(singleRunLine);
//		wagonWheel.add(ball1);
//	}
//	
//
//	public void four1() {
//		WagonWheelBall ball1 = new WagonWheelBall();
//		Vector3 endPointBall1 = new Vector3(-2f, 66f, 0);
//		ball1.setEndPoint(endPointBall1);
//		ball1.setRunType(RunTypes.ONE);
//		float verticesData[] = { 0, 0, 0, 0, 0, 1, 1, endPointBall1.x,
//				endPointBall1.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine = new BallPathLine(camera, verticesData);
//		ball1.setWagonWheelBallShape(singleRunLine);
//		wagonWheel.add(ball1);
//	}
//	
//
//	public void four2() {
//		WagonWheelBall ball2 = new WagonWheelBall();
//		Vector3 endPointBall2 = new Vector3(-78, 0, 0);
//		ball2.setEndPoint(endPointBall2);
//		ball2.setRunType(RunTypes.FOUR);
//		float verticesData2[] = { 0, 0, 0, 0, 0, 1, 1, endPointBall2.x,
//				endPointBall2.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine2 = new BallPathLine(camera, verticesData2);
//		ball2.setWagonWheelBallShape(singleRunLine2);
//		wagonWheel.add(ball2);
//
//	}
//
//	public void fourWithAir1() {
//		WagonWheelBall ball3 = new WagonWheelBall();
//		Vector3 endPointBall3 = new Vector3(50, 50, 0);
//		Vector3 endPointInAir = new Vector3(30, 30, 0);
//		BallTrajectoryInAir trajectoryInAir = new BallTrajectoryInAir(camera,
//				40, endPointInAir);
//		ball3.setBallTrajectoryInAir(trajectoryInAir);
//		float verticesData3[] = { endPointInAir.x, endPointInAir.y, 0, 0, 0, 1,
//				1, endPointBall3.x, endPointBall3.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine3 = new BallPathLine(camera, verticesData3);
//		ball3.setWagonWheelBallShape(singleRunLine3);
//		ball3.setRunType(RunTypes.FOUR);
//		wagonWheel.add(ball3);
//
//	}
//	
//	public void fourWithAir2() {
//		WagonWheelBall ball3 = new WagonWheelBall();
//		Vector3 endPointBall3 = new Vector3(-50, -50, 0);
//		Vector3 endPointInAir = new Vector3(-40, -40, 0);
//		BallTrajectoryInAir trajectoryInAir = new BallTrajectoryInAir(camera,
//				40, endPointInAir);
//		ball3.setBallTrajectoryInAir(trajectoryInAir);
//		float verticesData3[] = { endPointInAir.x, endPointInAir.y, 0, 0, 0, 1,
//				1, endPointBall3.x, endPointBall3.y, 0, 0, 0, 1, 1 };
//		BallPathLine singleRunLine3 = new BallPathLine(camera, verticesData3);
//		ball3.setWagonWheelBallShape(singleRunLine3);
//		ball3.setRunType(RunTypes.FOUR);
//		wagonWheel.add(ball3);
//
//	}
//	public void six1() {
//		WagonWheelBall ball3 = new WagonWheelBall();
//		Vector3 endPointInAir = new Vector3(0, -70, 0);
//		BallTrajectoryInAir trajectoryInAir = new BallTrajectoryInAir(camera,
//				80, endPointInAir);
//		ball3.setBallTrajectoryInAir(trajectoryInAir);
//		ball3.setRunType(RunTypes.SIX);
//		wagonWheel.add(ball3);
//
//	}

}
