package wagonwheel;


import java.util.ArrayList;
import java.util.List;

import com.opengleswagonwheel.AbstractOpenGLDraw;
import com.utils.BallTrajectoryInAir;
import com.utils.RunTypes;
import com.utils.Vector3;

/**
 * This is class is the each ball of the wagon Wheel. 
 * if there is ball trajectory in air, the point where the ball touches the ground is starting point of line. for six this point is zero.
 * 
 * @author milton
 *
 */
public class WagonWheelBall {
	private Vector3 endPoint,startPoint;
	private float angle=0;
	private RunTypes runType;
	private List<BallTrajectoryInAir> ballTrajectoryInAir= new ArrayList<BallTrajectoryInAir>();
	private AbstractOpenGLDraw wagonWheelBallShape;

	public RunTypes getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		switch (Integer.parseInt(runType)) {
		case 0:
			this.runType = RunTypes.ZERO;
			break;
		case 1:
			this.runType = RunTypes.ONE;
			break;

		case 2:
			this.runType = RunTypes.TWO;
			break;

		case 3:
			this.runType = RunTypes.THREE;
			break;
			
		case 4:
			this.runType = RunTypes.FOUR;
			break;

		case 6:
			this.runType = RunTypes.SIX;
			break;
			
		default:
			break;
		}
	}
	

	public Vector3 getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector3 endPoint) {
		this.endPoint = endPoint;
	}

	public Vector3 getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3 startPoint) {
		this.startPoint = startPoint;
	}
//	public float getDistance() {
//		return startPoint.dst(endPoint);
//	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public List<BallTrajectoryInAir> getBallTrajectoryInAir() {
		return ballTrajectoryInAir;
	}

	public void addBallTrajectoryInAir(BallTrajectoryInAir ballTrajectoryInAir) {
		this.ballTrajectoryInAir.add(ballTrajectoryInAir);
	}
	
	public AbstractOpenGLDraw getWagonWheelBallShape() {
		return wagonWheelBallShape;
	}

	public void setWagonWheelBallShape(AbstractOpenGLDraw wagonWheelBallShape) {
		this.wagonWheelBallShape = wagonWheelBallShape;
	}
	@Override
	public String toString() {
		return "WagonWheelBall [endPoint=" + endPoint + ", startPoint="
				+ startPoint + ", angle=" + angle + ", runType=" + runType
				+ ", ballTrajectoryInAir=" + ballTrajectoryInAir
				+ ", wagonWheelBallShape=" + wagonWheelBallShape + "]";
	}

}
