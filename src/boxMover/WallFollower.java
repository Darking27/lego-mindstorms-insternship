package boxMover;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class WallFollower implements ParcoursWalkable {
	
	private float[] uSample;
	private SampleProvider ultrasonicSampleProvider;
	
	public WallFollower() {
		uSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	}


	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		Ports.RIGHT_MOTOR.rotate(100);
		
		ParallelDriver.drive();
		
		Ports.LEFT_MOTOR.rotate(300, false);
		Ports.RIGHT_MOTOR.rotate(310, false);
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
			
			float distanceThreshold = 0.65f;
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			closeToWall = uSample[0] < distanceThreshold;
		}
		RobotUtils.stopMotors();
		
		System.out.println("now close to wall");
		
		return WalkableStatus.FINISHED;
	}

}
