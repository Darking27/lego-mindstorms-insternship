package other.bridgeFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class BridgeFollower {
	
	public enum State {
		DRIVING_STRAIT, DRIVING_LEFT, DRIVING_RIGHT, TURN_LEFT
	}

	Brick brick;
	SampleProvider distanceMode;
	SampleProvider reflectedLight;
	EV3UltrasonicSensor usSensor;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;
	private State state;
	private boolean timeout;
	private Timer timer;

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

		rightMotor.setSpeed(600);
		leftMotor.setSpeed(600);

		boolean onTrack = false;
		state = State.DRIVING_STRAIT;
		timeout = false;
		timer = new Timer(200, new TimerListener() {
			@Override
			public void timedOut() {
				timeout = true;
			}
		});

		while (!escape.isDown()) {
			switch(state) {
			case DRIVING_LEFT:
				if (!isOnLine()) {
					setState(State.DRIVING_RIGHT);
				}
				if (timeout) {
					setState(State.TURN_LEFT);
					timeout = false;
					timer.stop();
				}
				break;
			case DRIVING_RIGHT:
				if (timeout) {
					state = State.DRIVING_LEFT;
					timeout = false;
					timer.stop();
				}
				break;
			case DRIVING_STRAIT:
				if (!isOnLine()) {
					setState(State.DRIVING_RIGHT);
				}
				break;
			case TURN_LEFT:
				if (!isOnLine()) {
					setState(State.DRIVING_RIGHT);
				}
				break;
			default:
				break;
			
			}
			
			if (isOnLine() && !onTrack) {
				leftMotor.stop(true);
				rightMotor.rotate(1000, true);
				onTrack = true;
			}
			if (!isOnLine() && onTrack) {
				rightMotor.stop(true);
				leftMotor.rotate(1000, true);
				onTrack = false;
			}
		}
	}
	
	private void setState(State state) {
		this.state = state;
		switch (state) {
		case DRIVING_LEFT:
			leftMotor.setSpeed(600);
			rightMotor.setSpeed(450);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			timer.setDelay(400);
			timer.start();
			break;
		case DRIVING_RIGHT:
			leftMotor.setSpeed(450);
			rightMotor.setSpeed(600);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			timer.setDelay(200);
			timer.start();
			break;
		case DRIVING_STRAIT:
			// Drive a little bit to the left
			leftMotor.setSpeed(600);
			rightMotor.setSpeed(520);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			break;
		case TURN_LEFT:
			leftMotor.setSpeed(300);
			rightMotor.setSpeed(50);
			leftMotor.rotate(2000, true);
			rightMotor.rotate(2000, true);
			break;
		default:
			throw new IllegalArgumentException("state not valid");
		}
	}
	
	/*
	 * Idea:
	 * Use 3 different states for ascending, on the bridge and descending
	 * Ascending:
	 * The robot points towards the top of the ramp and climbs it. Distance to the ramp itself
	 * is pretty steady, the distance to the bottom increases over time.
	 * Strategy: Drive up the ramp, a bit to the left and keep an eye on the distance over time. If it increases then
	 * the robot is too far to the left and found the line. If it is steady then the robot is on the ramp.
	 * 
	 * First turn to the left:
	 * Do not make a sharp turn, instead also turn the left motor (but slower than the right one).
	 */

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
