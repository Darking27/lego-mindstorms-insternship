package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class TurnBoxFinder implements ParcoursWalkable {
	
	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public TurnBoxFinder() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		boolean boxFound = false;
		while (!boxFound) {
			RobotUtils.turn90DegreesRight();

			boxFound = boxFound();
			System.out.println("found= " + boxFound);

			RobotUtils.turnToNeutralTacho();
			RobotUtils.driveStraight(400);
		}
		
		RobotUtils.turn90DegreesRight();
		RobotUtils.setMaxSpeed();
		RobotUtils.forward();
		
		while(!oneTouchSensorDown()); //drive until the box is hit
		System.out.println("box hit");
		RobotUtils.stopMotors();
		
		Ports.RIGHT_MOTOR.forward();
		
		while(true) RobotUtils.checkForKeyPress();
	}
	
	private boolean boxFound() {
		float maxBoxDistance = 0.40f;
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < maxBoxDistance;
	}
	
	private boolean oneTouchSensorDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		return lTouchSample[0] > 0.5f || rTouchSample[0] > 0.5f;
	}

}
