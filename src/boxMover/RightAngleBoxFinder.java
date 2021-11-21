package boxMover;

import exception.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;

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
		uSample = new float[1];
		ultrasonicSampleProvider = new MedianFilter(Ports.ULTRASONIC_SENSOR.getDistanceMode(), 5);
		leftTouchSampleProvider = new MedianFilter(Ports.LEFT_TOUCH_SENSOR.getTouchMode(), 5);
		rightTouchSampleProvider = new MedianFilter(Ports.RIGHT_TOUCH_SENSOR.getTouchMode(), 5);
	}

	@Override
	public WalkableStatus start_walking() {
		try {
			boolean boxFound = false;

			while (!boxFound) {
				MoveUtils.turn90DegreesRight();

				float maxBoxDistance = 40;
				ultrasonicSampleProvider.fetchSample(uSample, 0);
				boxFound = uSample[0] < maxBoxDistance;

				MoveUtils.turnToNeutralTacho();

				MoveUtils.driveStraight(150);
			}

			MoveUtils.driveStraight(1000); // move box to the right wall

			MoveUtils.driveStraight(-100); // navigate around the box
			MoveUtils.turn90DegreesRight();
			MoveUtils.driveStraight(500);
			MoveUtils.turn90DegreesLeft();

			do {
				Ports.LEFT_MOTOR.rotate(100, true); // drive to the wall
				Ports.RIGHT_MOTOR.rotate(100, true);

				leftTouchSampleProvider.fetchSample(lTouchSample, 0);
				rightTouchSampleProvider.fetchSample(lTouchSample, 0);
			} while (lTouchSample[0] < 0.5 && rTouchSample[0] < 0.5);

			MoveUtils.driveStraight(2000); // drive box into the back corner

		} catch (KeyPressedException e) {
			return e.getStatus();
		}
		return WalkableStatus.FINISHED;
	}
}
