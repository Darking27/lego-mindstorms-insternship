package bridgeFollower;

import framework.ParcoursWalkable;
import framework.WalkableStatus;
import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

/**
 * ParcoursWalkable for the bridge
 * 
 * @author Niklas Arlt
 *
 */
public class BridgeFollower implements ParcoursWalkable {
	Brick brick;
	SampleProvider distanceMode;
	SampleProvider onTrackMode;
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
		this.onTrackMode = new AutoAdjustUltrasonic(distanceMode);

		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
	}
	
	@Override
	public WalkableStatus start_walking() {
		Key escape = brick.getKey("Escape");
		Key enter = brick.getKey("Enter");

		rightMotor.setSpeed(600);
		leftMotor.setSpeed(600);

		setState(State.DRIVING_STRAIT);
		timeout = false;
		timer = new Timer(200, new TimerListener() {
			@Override
			public void timedOut() {
				timeout = true;
			}
		});

		while (true) {
			// Handle keys
			if (escape.isDown()) {
				return WalkableStatus.STOP;
			}
			if (enter.isDown()) {
				return WalkableStatus.MENU;
			}
			
			// Switch states if necessary
			switch(state) {
			case DRIVING_LEFT:
				if (!isOnLine()) {
					System.out.println("Seeing edge -> right");
					setState(State.DRIVING_RIGHT);
				}
				if (timeout) {
					System.out.println("Timeout -> turn");
					setState(State.TURN_LEFT);
					timeout = false;
					timer.stop();
				}
				break;
			case DRIVING_RIGHT:
				if (timeout) {
					System.out.println("Timeout -> left");
					state = State.DRIVING_LEFT;
					timeout = false;
					timer.stop();
				}
				break;
			case DRIVING_STRAIT:
				if (!isOnLine()) {
					System.out.println("Seeing edge -> right");
					setState(State.DRIVING_RIGHT);
				}
				break;
			case TURN_LEFT:
				if (!isOnLine()) {
					System.out.println("Seing edge -> right");
					setState(State.DRIVING_RIGHT);
				}
				break;
			default:
				throw new IllegalArgumentException("state not valid");
			}
		}
		
		// End - currently not implemented
		// return WalkableStatus.FINISHED;
	}
	
	private void setState(State state) {
		this.state = state;
		isOnLine(true);
		switch (state) {
		case DRIVING_LEFT:
			leftMotor.setSpeed(300);
			rightMotor.setSpeed(150);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			timer.setDelay(400);
			timer.start();
			break;
		case DRIVING_RIGHT:
			leftMotor.setSpeed(150);
			rightMotor.setSpeed(300);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			timer.setDelay(150);
			timer.start();
			break;
		case DRIVING_STRAIT:
			/* Drive a little bit to the left.
			 * This is needed to ensure that we do not fall off the right edge of the ramp.
			 */
			leftMotor.setSpeed(300);
			rightMotor.setSpeed(250);
			leftMotor.rotate(3000, true);
			rightMotor.rotate(3000, true);
			break;
		case TURN_LEFT:
			leftMotor.setSpeed(300);
			rightMotor.setSpeed(100);
			leftMotor.rotate(4000, true);
			rightMotor.rotate(4000, true);
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

	public boolean isOnLine() {
		return isOnLine(false);
	}
	
	/**
	 * Checks whether the robot is on the line or not
	 * 
	 * @return
	 */
	public boolean isOnLine(boolean printInfo) {
		int sampleSize = 5;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		
		if (printInfo) {
			System.out.println("Line: " + sample[0] + " [" + sample[1] + ":" + sample[2] + ":" + sample[3] + "]");
			System.out.println("Conf: " + sample[4]);
		}
		
		// System.out.println(onLine);
		return onLine;
	}
}
