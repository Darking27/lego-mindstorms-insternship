package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;
import lejos.utility.Delay;

public class RightAngleBoxFinder implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public RightAngleBoxFinder() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = new MedianFilter(Ports.LEFT_TOUCH_SENSOR.getTouchMode(), 5);
		rightTouchSampleProvider = new MedianFilter(Ports.RIGHT_TOUCH_SENSOR.getTouchMode(), 5);
	}
	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		Delay.msDelay(100);  // TODO remove delay
		boolean boxFound = false;

		while (!boxFound) {
			RobotUtils.turn90DegreesRight();

			float maxBoxDistance = 0.40f;
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			boxFound = uSample[0] < maxBoxDistance;
			
			System.out.println("distance = " + uSample[0]);
			System.out.println("Box found = " + boxFound);

			RobotUtils.turnToNeutralTacho();

			RobotUtils.driveStraight(400);
		}
		
		
		RobotUtils.turn90DegreesRight();  // move box to the right wall
		RobotUtils.driveStraight(1500); 

		RobotUtils.driveStraight(-200); // navigate around the box
		RobotUtils.turn90DegreesRight();
		RobotUtils.driveStraight(500);
		RobotUtils.turn90DegreesLeft();

		do {							 // drive to the wall
			Ports.LEFT_MOTOR.rotate(100, true);
			Ports.RIGHT_MOTOR.rotate(100, true);
			
			RobotUtils.checkForKeyPress();
			
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			rightTouchSampleProvider.fetchSample(lTouchSample, 0);
		} while (lTouchSample[0] < 0.5 && rTouchSample[0] < 0.5);

		RobotUtils.driveStraight(2000); // drive box into the back corner
		return WalkableStatus.FINISHED;
	}
}
