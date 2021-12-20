package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class CurveBoxMover implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public CurveBoxMover() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {

		int searchTrys = searchBox();

		RobotUtils.setSpeed(600, 700); // drive until Box is hit
		RobotUtils.forward();
		while (!oneTouchSensorDown())
			RobotUtils.checkForKeyPress();
		RobotUtils.floatMotors();

		if (searchTrys <= 2) {
			System.out.println("front");
			RobotUtils.setSpeed(470, 700);
			Ports.LEFT_MOTOR.forward();
			Ports.RIGHT_MOTOR.rotate(3000, false);

			RobotUtils.setMaxSpeed();
			RobotUtils.driveStraight(-300);
			Ports.LEFT_MOTOR.rotate(400);
			Ports.RIGHT_MOTOR.rotate(-400);
			RobotUtils.forward();
			while (!bothTouchSensorsDown())
				RobotUtils.checkForKeyPress();
		} else {
			System.out.println("back");
			RobotUtils.setSpeed(500, 600);
			Ports.LEFT_MOTOR.forward();
			Ports.RIGHT_MOTOR.rotate(2800, false);

			RobotUtils.setMaxSpeed();
			Ports.LEFT_MOTOR.rotate(-600);
			Ports.RIGHT_MOTOR.rotate(-850);
			RobotUtils.forward();
			while (!bothTouchSensorsDown())
				RobotUtils.checkForKeyPress();
			RobotUtils.driveStraight(-300);
			RobotUtils.turn90DegreesRight();
		}

		RobotUtils.stopMotors();
		while (true)
			RobotUtils.checkForKeyPress();
	}

	private int searchBox() throws KeyPressedException {
		boolean boxFound = false;
		int searchTrys = 0;

		RobotUtils.setMaxSpeed();

		while (!boxFound) {
			searchTrys++;
			RobotUtils.resetTachos();
			RobotUtils.turn90DegreesRight();

			boxFound = boxFound();
			System.out.println("found= " + boxFound);

			if (!boxFound) {
				RobotUtils.turnToNeutralTacho();
				RobotUtils.driveStraight(400);
			}
		}

		return searchTrys;
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

	private boolean bothTouchSensorsDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		return lTouchSample[0] > 0.5f && rTouchSample[0] > 0.5f;
	}

}
