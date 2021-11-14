package boxMover;

import framework.ParcoursWalkable;
import framework.WalkableStatus;
import framework.Ports;
import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public class WallFollower implements ParcoursWalkable {
	
	private Brick brick = Ports.BRICK;
	private RegulatedMotor rightMotor = Ports.RIGHT_MOTOR;
	private RegulatedMotor leftMotor = Ports.LEFT_MOTOR;
	private EV3TouchSensor touchSensor = Ports.LEFT_TOUCH_SENSOR;
	private EV3UltrasonicSensor ultrasonicSensor = Ports.ULTRASONIC_SENSOR;
	
	public WallFollower() {
		Ports.LEFT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.ULTRASONIC_SENSOR.setCurrentMode("Distance");
	}

	/**
	 * follows the left side of a wall -> bumps into the wall, turns right and
	 * leaves the wall, drive slowly towards the left wall again, bump into it...
	 * uses the left touch sensor stops when a wall is less then _x_ Centimeters
	 * away
	 */
	@Override
	public WalkableStatus start_walking() {

		float[] touchSample = new float[touchSensor.sampleSize()];
		float[] ultrasonicSample = new float[ultrasonicSensor.sampleSize()];

		//while (!closeToBackWall(ultrasonicSample)) {
		while (true) {

			if (brick.getKey("Enter").isDown()) {
				return WalkableStatus.MENU;
			}

			if (brick.getKey("Escape").isDown()) {
				return WalkableStatus.STOP;
			}

			touchSensor.fetchSample(touchSample, 0);
			if (touchSample[0] > 0.3) { // sensor is pressed
				// turn right
				rightMotor.flt(true);
				leftMotor.rotate(200, false);

				// go back to the else part and slowly drive towards the wall

			} else { // sensor is not pressed

				// makes a left turn
				rightMotor.setSpeed(500);
				leftMotor.setSpeed(400);

				rightMotor.rotate(1000, true);
				leftMotor.rotate(1000, true);
			}
		}
		//return WalkableStatus.FINISHED;
	}

	/**
	 * Checks if the Sensor is close to the back wall
	 * 
	 * | ROBO-> | FALSE |
	 * 
	 * | ROBO-> | TRUE |
	 * 
	 * @param ultrasonicSample the array in which to save the measured values from
	 *                         the ultrasonic sensor
	 */
	private boolean closeToBackWall(float[] ultrasonicSample) {
		double closeToWallInMeter = 0.80;
		touchSensor.fetchSample(ultrasonicSample, 0);
		return ultrasonicSample[0] > closeToWallInMeter;
	}
}
