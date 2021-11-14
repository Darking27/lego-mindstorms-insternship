package boxMover;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;

public class BoxFinder implements ParcoursWalkable {

	public BoxFinder() {
		Ports.LEFT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.RIGHT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.ULTRASONIC_SENSOR.setCurrentMode("Distance");
	}

	@Override
	public WalkableStatus start_walking() {
		lookInBoxDirection();
		while(true) {
			if (Ports.EnterKey.isDown()) {
				return WalkableStatus.MENU;
			}

			if (Ports.EscapeKey.isDown()) {
				return WalkableStatus.STOP;
			}
		}
	}
	
	/**
	 * drives Forward until it hits the box
	 */
	private WalkableStatus driveForwad() {
		float[] leftTouchSample = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
		float[] rightTouchSample = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
		
		do {
			if (Ports.EnterKey.isDown()) {
				return WalkableStatus.MENU;
			}
			if (Ports.EscapeKey.isDown()) {
				return WalkableStatus.STOP;
			}
			Ports.LEFT_TOUCH_SENSOR.fetchSample(leftTouchSample, 0);
			Ports.RIGHT_TOUCH_SENSOR.fetchSample(rightTouchSample, 0);			
		} while(median(leftTouchSample) < 0.5 && median(rightTouchSample) < 0.5);
		
		return WalkableStatus.FINISHED;
	}

	/**
	 * if the robot is near the box and approximately looks in the direction of the
	 * box, it rotates exactly to the box
	 * 
	 * this is done by measuring the distance while turning and going back to the
	 * direction where the distance was minimal
	 */
	private void lookInBoxDirection() {
		Ports.LEFT_MOTOR.resetTachoCount();
		Collection<DistanceTachoTuple> distances = new LinkedList<DistanceTachoTuple>();
		float[] ultrasonicSample = new float[Ports.ULTRASONIC_SENSOR.sampleSize()];
		int search_radius = 1000;

		Ports.LEFT_MOTOR.setSpeed(100);
		Ports.LEFT_MOTOR.rotate(search_radius);

		while (Ports.LEFT_MOTOR.isMoving()) {
			Ports.ULTRASONIC_SENSOR.fetchSample(ultrasonicSample, 0);
			Arrays.sort(ultrasonicSample);
			double distance_median = ultrasonicSample[ultrasonicSample.length / 2];
			int tacho_count = Ports.LEFT_MOTOR.getTachoCount();
			distances.add(new DistanceTachoTuple(distance_median, tacho_count));
		}

		int rotateBack = Collections.min(distances).tacho;

		Ports.LEFT_MOTOR.rotate(rotateBack - search_radius);

		System.out.println(Ports.LEFT_MOTOR.getTachoCount() + " --should be " + search_radius);
	}

	float median(float[] arr) {
		Arrays.sort(arr);
		return arr[arr.length/2];
	}
	
	private class DistanceTachoTuple implements Comparable<DistanceTachoTuple> {

		public final double dist;
		public final int tacho;

		public DistanceTachoTuple(double dist, int tacho) {
			this.dist = dist;
			this.tacho = tacho;
		}

		@Override
		public int compareTo(DistanceTachoTuple o) {
			return (int) (this.dist - o.dist);
		}
	}
}
