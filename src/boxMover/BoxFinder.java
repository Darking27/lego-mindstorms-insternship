package boxMover;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;
import lejos.utility.Delay;

public class BoxFinder implements ParcoursWalkable {

	public BoxFinder() {
		Ports.LEFT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.RIGHT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.ULTRASONIC_SENSOR.setCurrentMode("Distance");
		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.RIGHT_MOTOR.setSpeed(300);
	}

	@Override
	public WalkableStatus start_walking() {
		findBox();
		return WalkableStatus.FINISHED;
	}

	/**
	 * if the robot is near the box and approximately looks in the direction of the
	 * box, it rotates exactly to the box
	 * 
	 * this is done by measuring the distance while turning and going back to the
	 * direction where the distance was minimal
	 */
	private void findBox() {

		Ports.LEFT_MOTOR.resetTachoCount();

		DistanceTachoTuple distanceTuple;
		
		double boxDistanceThreshold = 30;

		do {
			if (Ports.ESCAPE.isDown()) {
				return;
			}

			Ports.LEFT_MOTOR.rotate(600, true);
			Ports.RIGHT_MOTOR.rotate(600, false);

			distanceTuple = searchBox(); //rotates right

			System.out.println(distanceTuple.dist);

			rotateBack(); //rotates left to inital position
		} while (distanceTuple.dist > boxDistanceThreshold);
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(true);
		
		System.out.println("BOX FOUND");
		Delay.msDelay(4000);
		

	}

	/**
	 * rotates the robot to the right and returns a tuple of the closest distance and the according TachoCount
	 */
	private DistanceTachoTuple searchBox() {
		SampleProvider s = new MedianFilter(Ports.ULTRASONIC_SENSOR.getDistanceMode(), 5);
		float[] sample = new float[1];

		Collection<DistanceTachoTuple> distances = new LinkedList<DistanceTachoTuple>();

		int search_radius = 600;

		Ports.LEFT_MOTOR.resetTachoCount();

		Ports.LEFT_MOTOR.rotate(search_radius, true);

		while (Ports.LEFT_MOTOR.isMoving()) {
			s.fetchSample(sample, 0);
			float x = sample[0];
			if (x < 0.01)
				x = 1000;
			distances.add(new DistanceTachoTuple(x, Ports.LEFT_MOTOR.getTachoCount()));
		}

		return Collections.min(distances);
	}

	private void rotateBack() {
		while (Ports.LEFT_MOTOR.getTachoCount() > 0) {
			if (Ports.ESCAPE.isDown()) {
				return;
			}
			Ports.LEFT_MOTOR.rotate(-100, true);
		}
		Ports.LEFT_MOTOR.stop();
	}

	private class DistanceTachoTuple implements Comparable<DistanceTachoTuple> {
		public final float dist;
		public final int tacho;

		public DistanceTachoTuple(float dist, int tacho) {
			this.dist = dist;
			this.tacho = tacho;
		}

		@Override
		public int compareTo(DistanceTachoTuple o) {
			return (int) (1000 * (this.dist - o.dist));
		}
	}

}
