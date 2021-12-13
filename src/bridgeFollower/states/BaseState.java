package bridgeFollower.states;

import bridgeFollower.SimpleUltrasonic;
import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public abstract class BaseState {
	protected SampleProvider distanceMode = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	protected SampleProvider onTrackMode = new SimpleUltrasonic(distanceMode);
	protected SampleProvider leftTouch = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	protected SampleProvider rightTouch = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	protected Key escape = Ports.BRICK.getKey("Escape");
	protected Key enter = Ports.BRICK.getKey("Enter");
	
	/**
	 * Run until the state should be switched
	 * @return next state
	 */
	public abstract State handleState() throws KeyPressedException;
	
	protected void handleKeyPressed() throws KeyPressedException {
		if (escape.isDown()) {
			throw new StopException();
		}
		if (enter.isDown()) {
			Ports.ULTRASONIC_MOTOR.setSpeed(80);
			Ports.ULTRASONIC_MOTOR.rotateTo(0);
			RobotUtils.stopMotors();
			throw new MenuException();
		}
	}
	
	protected boolean isOverBridge() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		return onLine;
	}
	
	protected boolean seeingEndRamp() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean endRamp = sample[0] < -0.5f;
		return endRamp;
	}
	
	protected boolean touchLeft() {
		float[] sample = new float[1];
		leftTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
	
	protected boolean touchRight() {
		float[] sample = new float[1];
		rightTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
	
	protected boolean seeingEdge() {
		return false;
	}
	
	protected boolean seeingLightBrown() {
		return false;
	}
}
