package boxMover;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class BoxMover implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public BoxMover() {
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
		RobotUtils.driveStraight(180);
		
		RobotUtils.turn90DegreesRight(); 	// move box to the right wall
		RobotUtils.driveStraight(2100);
		
		RobotUtils.setSpeed(360);
		RobotUtils.driveStraight(-200);  	//navigate around the box
		RobotUtils.turn90DegreesRight();
		RobotUtils.setMaxSpeed();
		RobotUtils.driveStraight(700);
		RobotUtils.turn90DegreesLeft();
		
		System.out.println("around the box");
		
		RobotUtils.forward();				// drive to back wall
		while(!bothTouchSensorsDown()) RobotUtils.checkForKeyPress();
		System.out.println("wall found - orthagonal");
		RobotUtils.stopMotors();
		
		RobotUtils.driveStraight(-120); 	// drive box into the back corner
		RobotUtils.turn90DegreesLeft();
		RobotUtils.driveStraight(2000);
		
		return WalkableStatus.FINISHED;
	}
	
	private boolean boxFound() {
		float maxBoxDistance = 0.40f;
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < maxBoxDistance;
	}
	
	private boolean bothTouchSensorsDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		return lTouchSample[0] > 0.5f && rTouchSample[0] > 0.5f;
	}
}
