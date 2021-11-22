package boxMover;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class BoxMoverWrapper implements ParcoursWalkable {
	

	private float[] uSample;
	private SampleProvider ultrasonicSampleProvider;
	private RightAngleBoxFinder boxFinder;
	
	public BoxMoverWrapper() {
		uSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		boxFinder = new RightAngleBoxFinder();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		System.out.println("NOW BOX MOVE");
		
		Ports.RIGHT_MOTOR.rotate(700);
		
		ParallelDriver.drive();
		
		Ports.LEFT_MOTOR.rotate(400, false);
		Ports.RIGHT_MOTOR.rotate(400, false);
		System.out.println("driving 1800 units hardcoded");
		RobotUtils.driveStraight(1500);
		
		System.out.println("dirve til wall is close");
		boolean closeToWall = false;
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		while (!closeToWall) {
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
			
			float distanceThreshold = 0.7f;
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			closeToWall = uSample[0] < distanceThreshold;
		}
		RobotUtils.stopMotors();
		
		System.out.println("no close to wall");
		System.out.println("start walking");
		
		
		boxFinder.start_walking();
		
		return WalkableStatus.FINISHED;
	}
}
