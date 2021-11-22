package boxMover;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
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
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	}
	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		boolean boxFound = false;
		while (!boxFound) {
			RobotUtils.turn90DegreesRight();

			float maxBoxDistance = 0.40f;
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			boxFound = uSample[0] < maxBoxDistance;
			
			System.out.println("distance= " + uSample[0]);
			System.out.println("found= " + boxFound);

			RobotUtils.turnToNeutralTacho();

			RobotUtils.driveStraight(400);
		}
		
		RobotUtils.turn90DegreesRight();  // move box to the right wall
		RobotUtils.driveStraight(1900); 

		RobotUtils.driveStraight(-200); // navigate around the box
		RobotUtils.turn90DegreesRight();
		RobotUtils.driveStraight(800);
		RobotUtils.turn90DegreesLeft();

		System.out.println("drive til touched");
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		do {							 // drive to the wall
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
			
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		} while (lTouchSample[0] < 0.5f || rTouchSample[0] < 0.5f);
		
		RobotUtils.stopMotors();
		
		System.out.println("Wall found");
		
		RobotUtils.driveStraight(-100);  // drive box into the back corner
		RobotUtils.turn90DegreesLeft();
		System.out.println("turned left");
		RobotUtils.driveStraight(1900);
		return WalkableStatus.FINISHED;
	}
}
