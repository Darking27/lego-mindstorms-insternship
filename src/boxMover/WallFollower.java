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
		RobotUtils.setMaxSpeed();
		
		Ports.RIGHT_MOTOR.rotate(200);
		
		ParallelDriver.drive();
		
		RobotUtils.setSpeed(400);
		Ports.LEFT_MOTOR.rotate(300, false);		//gain some distance to the wall
		Ports.RIGHT_MOTOR.rotate(300, false);
		
		System.out.println("dirve til wall is close");
		boolean closeToWall = false;
		
		RobotUtils.setMaxSpeed();
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		while (!closeToWall) {
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
			
			float distanceThreshold = 0.72f;
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			closeToWall = uSample[0] < distanceThreshold;
		}
		RobotUtils.stopMotors();
		
		System.out.println("now close to wall");
		
		return WalkableStatus.FINISHED;
	}

}
