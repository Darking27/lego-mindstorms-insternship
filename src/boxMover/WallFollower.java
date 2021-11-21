package boxMover;


import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;

public class WallFollower implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;

	public WallFollower() {
		uSample = new float[1];
		lTouchSample = new float[1];
		ultrasonicSampleProvider = new MedianFilter(Ports.ULTRASONIC_SENSOR.getDistanceMode(), 5);
		leftTouchSampleProvider = new MedianFilter(Ports.LEFT_TOUCH_SENSOR.getTouchMode(), 5);
	}

	@Override
	public WalkableStatus start_walking() {
		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.RIGHT_MOTOR.setSpeed(310); // different speed: slowly drift to the left wall

		boolean nearTheBackWall = false;
		while (!nearTheBackWall) {
			if (Ports.ESCAPE.isDown())
				return WalkableStatus.STOP;
			if (Ports.ENTER.isDown())
				return WalkableStatus.MENU;

			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			if (lTouchSample[0] > 0.5) {  // turn a bit right when the left touch sensor is pressed
				Ports.RIGHT_MOTOR.flt(true);
				Ports.LEFT_MOTOR.rotate(40, false);
			}
			
			Ports.LEFT_MOTOR.rotate(100, true);
			Ports.RIGHT_MOTOR.rotate(100, true);
			
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			float backWallDistanceThreshold = 0.80f;
			nearTheBackWall = uSample[0] < backWallDistanceThreshold;
		}
		
		return WalkableStatus.FINISHED;
	}
}
