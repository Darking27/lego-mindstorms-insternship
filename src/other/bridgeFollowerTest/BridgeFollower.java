package other.bridgeFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class BridgeFollower {

	Brick brick;
	SampleProvider distanceMode;
	SampleProvider reflectedLight;
	EV3UltrasonicSensor usSensor;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;

	public BridgeFollower(Brick brick, String usSensorPort, String leftMotorPort, String rightMotorPort) {
		this.brick = brick;
		Port sensorPort = brick.getPort(usSensorPort);
		this.usSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceMode = usSensor.getDistanceMode();

		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
	}

	public void followLineSimple() {
		Key escape = brick.getKey("Escape");

		rightMotor.setSpeed(300);
		leftMotor.setSpeed(300);

		boolean onLine = false;

		while (!escape.isDown()) {
			if (isOnLine() && !onLine) {
				leftMotor.stop(true);
				rightMotor.rotate(1000, true);
				onLine = true;
			}
			if (!isOnLine() && onLine) {
				rightMotor.stop(true);
				leftMotor.rotate(1000, true);
				onLine = false;
			}
		}
	}

	/**
	 * Checks whether the robot is on the line or not
	 * 
	 * @return
	 */
	public boolean isOnLine() {
		int sampleSize = distanceMode.sampleSize();
		float[] sample = new float[sampleSize];
		distanceMode.fetchSample(sample, 0);
		/* Display individual values in the sample. */
		for (int i = 0; i < sampleSize; i++) {
			System.out.print(sample[i] + " ");
		}
		boolean onLine = (sample[0] <= 0.26) || (sample[0] > 300);
		System.out.println(onLine);
		return onLine;
	}
}
