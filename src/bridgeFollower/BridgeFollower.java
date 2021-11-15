package bridgeFollower;

import display.Logger;
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
		this.onTrackMode = new SimpleUltrasonic(distanceMode);

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
					Logger.INSTANCE.log("Seeing edge -> right");
					setState(State.ROTATE_RIGHT);
				}
				/*if (timeout) {
					Logger.INSTANCE.log("Timeout -> turn");
					setState(State.TURN_LEFT);
					timeout = false;
					timer.stop();
				}*/
				if (leftMotor.getTachoCount() > 700) {
					Logger.INSTANCE.log("left tacho -> turn");
					setState(State.TURN_LEFT);
				}
				break;
			case ROTATE_RIGHT:
				if (isOnLine()) {
					Logger.INSTANCE.log("Seeing edge -> left");
					setState(State.DRIVING_LEFT);
				}
				break;
			case DRIVING_STRAIT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seeing edge -> right");
					setState(State.ROTATE_RIGHT);
				}
				break;
			case TURN_LEFT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seing edge -> right");
					setState(State.ROTATE_RIGHT);
				}
				if (leftMotor.getTachoCount() > 800) {
					Logger.INSTANCE.log("left tacho -> straight");
					setState(State.DRIVING_STRAIT);
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
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		
		switch (state) {
		case DRIVING_LEFT:
			leftMotor.setSpeed(300);
			rightMotor.setSpeed(200);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
			//timer.setDelay(2000);
			//timer.start();
			break;
		case ROTATE_RIGHT:
			leftMotor.setSpeed(0);
			rightMotor.setSpeed(200);
			leftMotor.rotate(1000, true);
			rightMotor.rotate(1000, true);
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
			leftMotor.setSpeed(500);
			rightMotor.setSpeed(100);
			leftMotor.rotate(4000, true);
			rightMotor.rotate(4000, true);
			//timer.setDelay(6000);
			//timer.start();
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
		int sampleSize = 5;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		
		// Logger.INSTANCE.log(onLine);
		return onLine;
	}
}
