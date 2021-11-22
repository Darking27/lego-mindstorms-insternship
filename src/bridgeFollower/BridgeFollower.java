package bridgeFollower;

import display.Logger;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * ParcoursWalkable for the bridge
 * 
 * @author Niklas Arlt
 *
 */
public class BridgeFollower implements ParcoursWalkable {
	SampleProvider distanceMode;
	SampleProvider onTrackMode;
	SampleProvider leftTouch = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	SampleProvider rightTouch = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();

	private BridgeFollowerState state;

	public BridgeFollower() {
		this.distanceMode = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		this.onTrackMode = new SimpleUltrasonic(distanceMode);
	}
	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Key escape = Ports.BRICK.getKey("Escape");
		Key enter = Ports.BRICK.getKey("Enter");
		
		setState(BridgeFollowerState.DRIVING_STRAIT);

		while (true) {
			// Handle keys
			if (escape.isDown()) {
				return WalkableStatus.STOP;
			}
			if (enter.isDown()) {
				return WalkableStatus.MENU;
			}
			
			// Handle obstacle
			if (touchLeft() || touchRight()) {
				return new TunnelFinder().start_walking();
			}
			
			// Switch states if necessary
			switch(state) {
			case DRIVING_LEFT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seeing edge -> right");
					setState(BridgeFollowerState.ROTATE_RIGHT);
				}
				if (Ports.RIGHT_MOTOR.getTachoCount() > 700) {
					if (seeingEndRamp()) {
						Logger.INSTANCE.log("left tacho, ramp -> short turn");
						setState(BridgeFollowerState.TURN_LEFT_SHORT);
					} else {
						Logger.INSTANCE.log("left tacho -> long turn");
						setState(BridgeFollowerState.TURN_LEFT);
					}	
				}
				break;
			case ROTATE_RIGHT:
				if (isOnLine()) {
					Logger.INSTANCE.log("Seeing edge -> left");
					setState(BridgeFollowerState.DRIVING_LEFT);
				}
				break;
			case DRIVING_STRAIT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seeing edge -> right");
					setState(BridgeFollowerState.ROTATE_RIGHT);
				}
				break;
			case TURN_LEFT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seing edge -> right");
					setState(BridgeFollowerState.ROTATE_RIGHT);
				}
				if (Ports.RIGHT_MOTOR.getTachoCount() > 800) {
					Logger.INSTANCE.log("left tacho -> straight");
					setState(BridgeFollowerState.DRIVING_STRAIT);
				}
				break;
			case TURN_LEFT_SHORT:
				if (!isOnLine()) {
					Logger.INSTANCE.log("Seing edge -> right");
					setState(BridgeFollowerState.ROTATE_RIGHT);
				}
				if (Ports.RIGHT_MOTOR.getTachoCount() > 450) {
					Logger.INSTANCE.log("left tacho -> straight");
					setState(BridgeFollowerState.DRIVING_STRAIT);
				}
				break;
				
			default:
				throw new IllegalArgumentException("state not valid");
			}
		}
		
		// End - currently not implemented
		// return WalkableStatus.FINISHED;
	}
	
	private void setState(BridgeFollowerState state) {
		this.state = state;
		Ports.RIGHT_MOTOR.resetTachoCount();
		Ports.LEFT_MOTOR.resetTachoCount();
		
		switch (state) {
		case DRIVING_LEFT:
			Ports.RIGHT_MOTOR.setSpeed(300);
			Ports.LEFT_MOTOR.setSpeed(150);
			Ports.RIGHT_MOTOR.rotate(1000, true);
			Ports.LEFT_MOTOR.rotate(1000, true);
			//timer.setDelay(2000);
			//timer.start();
			break;
		case ROTATE_RIGHT:
			Ports.RIGHT_MOTOR.setSpeed(0);
			Ports.LEFT_MOTOR.setSpeed(200);
			Ports.RIGHT_MOTOR.rotate(1000, true);
			Ports.LEFT_MOTOR.rotate(1000, true);
			break;
		case DRIVING_STRAIT:
			/* Drive a little bit to the left.
			 * This is needed to ensure that we do not fall off the right edge of the ramp.
			 */
			Ports.RIGHT_MOTOR.setSpeed(300);
			Ports.LEFT_MOTOR.setSpeed(250);
			Ports.RIGHT_MOTOR.rotate(3000, true);
			Ports.LEFT_MOTOR.rotate(3000, true);
			break;
		case TURN_LEFT_SHORT:
		case TURN_LEFT:
			Ports.RIGHT_MOTOR.setSpeed(500);
			Ports.LEFT_MOTOR.setSpeed(100);
			Ports.RIGHT_MOTOR.rotate(4000, true);
			Ports.LEFT_MOTOR.rotate(4000, true);
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
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		return onLine;
	}
	
	public boolean seeingEndRamp() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean endRamp = sample[0] < -0.5f;
		return endRamp;
	}
	
	private boolean touchLeft() {
		float[] sample = new float[1];
		leftTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
	
	private boolean touchRight() {
		float[] sample = new float[1];
		rightTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
}
