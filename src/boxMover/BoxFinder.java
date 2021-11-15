package boxMover;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.utility.Delay;

public class BoxFinder implements ParcoursWalkable {

	public BoxFinder() {
		Ports.LEFT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.RIGHT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.ULTRASONIC_SENSOR.setCurrentMode("Distance");
	}

	@Override
	public WalkableStatus start_walking() {
		while (!Ports.ESCAPE_KEY.isDown()) {
			float[] ultrasonicSample = new float[Ports.ULTRASONIC_SENSOR.sampleSize()];
			Ports.ULTRASONIC_SENSOR.fetchSample(ultrasonicSample, 0);
			System.out.println(ultrasonicSample[0]);
			return WalkableStatus.STOP;
		}
		
		lookInBoxDirection();
		Delay.msDelay(1000);
		return WalkableStatus.FINISHED;
	}

	/**
	 * drives Forward until it hits the box
	 */
	private WalkableStatus driveForwad() {
		float[] leftTouchSample = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
		float[] rightTouchSample = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];

		do {
			if (Ports.ENTER_KEY.isDown()) {
				return WalkableStatus.MENU;
			}
			if (Ports.ESCAPE_KEY.isDown()) {
				return WalkableStatus.STOP;
			}
			Ports.LEFT_TOUCH_SENSOR.fetchSample(leftTouchSample, 0);
			Ports.RIGHT_TOUCH_SENSOR.fetchSample(rightTouchSample, 0);
		} while (median(leftTouchSample) < 0.5 && median(rightTouchSample) < 0.5);

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
		int search_radius = 700;

		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.LEFT_MOTOR.rotate(search_radius, true);

		// tilt the robot to the left, while moving measure distances
		while (Ports.LEFT_MOTOR.isMoving()) {
			Ports.ULTRASONIC_SENSOR.fetchSample(ultrasonicSample, 0);
			double distance_median = ultrasonicSample[0];
			
			int tacho_count = Ports.LEFT_MOTOR.getTachoCount();
			
			distances.add(new DistanceTachoTuple(distance_median, tacho_count));
		}
		
		System.out.println("it: " + distances.size());
		System.out.println("max: " + new DecimalFormat("##.##").format(Collections.max(distances).dist));
		System.out.println("min: " + new DecimalFormat("##.##").format(Collections.min(distances).dist));
		
		// get the lowest distances measured in the loop before
		int rotateBack = Collections.min(distances).tacho;

		// rotates to the lowest distance measured
		while (Ports.LEFT_MOTOR.getTachoCount() >= rotateBack) {
			Ports.LEFT_MOTOR.rotate(-100);
		}
		Ports.LEFT_MOTOR.stop();

		System.out.println(Ports.LEFT_MOTOR.getTachoCount() + " " + search_radius);
	}

	float median(float[] arr) {
		Arrays.sort(arr);
		return arr[arr.length / 2];
	}

	private class DistanceTachoTuple implements Comparable<DistanceTachoTuple> {

		public final double dist;
		public final int tacho;

		public DistanceTachoTuple(double dist, int tacho) {
			this.dist = dist;
			this.tacho = tacho;
		}

		/**
		 * compare by distance only
		 */
		@Override
		public int compareTo(DistanceTachoTuple o) {
			return (int) (1000*(this.dist - o.dist));
		}
	}
}
