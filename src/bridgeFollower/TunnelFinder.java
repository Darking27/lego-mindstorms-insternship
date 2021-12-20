package bridgeFollower;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.robotics.SampleProvider;
import lineFollower.colorSensor.RGBColorSensor;

public class TunnelFinder implements ParcoursWalkable {
	public static final int MOTOR_SPEED = 300;
	public static final int ROTATION = 40;
	public static final int SET_BACK_DISTANCE = 300;
	
	SampleProvider leftTouch = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	SampleProvider rightTouch = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	TunnelFinderState state;

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Key escape = Ports.BRICK.getKey("Escape");
		Key enter = Ports.BRICK.getKey("Enter");
		
		Ports.ULTRASONIC_MOTOR.setSpeed(80);
		Ports.ULTRASONIC_MOTOR.rotateTo(0, true);
		
		Ports.RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
		Ports.LEFT_MOTOR.setSpeed(MOTOR_SPEED);
		
		setState(TunnelFinderState.DRIVING_STRAIT);

		while (true) {
			// Handle keys
			if (escape.isDown()) {
				stopMotors();
				return WalkableStatus.STOP;
			}
			if (enter.isDown()) {
				stopMotors();
				return WalkableStatus.MENU;
			}
			
			switch (state) {
			case DRIVING_BACK_RIGHT:
				if (!Ports.LEFT_MOTOR.isMoving() && !Ports.RIGHT_MOTOR.isMoving())
					setState(TunnelFinderState.ROTATE_RIGHT);
				break;
			case DRIVING_STRAIT:
				if (touchLeft()) {
					stopMotors();
					setState(TunnelFinderState.DRIVING_BACK_RIGHT);
				}
				if (touchRight()) {
					stopMotors();
					setState(TunnelFinderState.DRIVNG_BACK_LEFT);
				}
				if (RGBColorSensor.getInstance().isFinishLine()) {
					stopMotors();
					return WalkableStatus.FINISHED;
				}
				break;
			case DRIVNG_BACK_LEFT:
				if (!Ports.LEFT_MOTOR.isMoving() && !Ports.RIGHT_MOTOR.isMoving())
					setState(TunnelFinderState.ROTATE_LEFT);
				break;
			case ROTATE_LEFT:
			case ROTATE_RIGHT:
				if (!Ports.LEFT_MOTOR.isMoving() && !Ports.RIGHT_MOTOR.isMoving())
					setState(TunnelFinderState.DRIVING_STRAIT);
				break;
			default:
				throw new IllegalArgumentException("state not valid");
			}
			
		}
	}
	
	private void setState(TunnelFinderState state) {
		this.state = state;
		switch (state) {
		case DRIVING_BACK_RIGHT:
		case DRIVNG_BACK_LEFT:
			Ports.RIGHT_MOTOR.rotate(-SET_BACK_DISTANCE, true);
			Ports.LEFT_MOTOR.rotate(-SET_BACK_DISTANCE, true);
			break;
		case DRIVING_STRAIT:
			Ports.RIGHT_MOTOR.forward();
			Ports.LEFT_MOTOR.forward();
			break;
		case ROTATE_LEFT:
			rotate(-ROTATION);
			break;
		case ROTATE_RIGHT:
			rotate(ROTATION);
			break;
		default:
			throw new IllegalArgumentException("state not valid");
		}
	}
	
	private void stopMotors() {
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
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
	
	private void rotate(int rotation) {
		Ports.RIGHT_MOTOR.rotate(-rotation, true);
		Ports.LEFT_MOTOR.rotate(rotation, true);
	}

}
