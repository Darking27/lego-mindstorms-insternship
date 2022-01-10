package new_bridge;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lineFollower.colorSensor.RGBColorSensor;

public class BridgeFollower implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private float[] colorSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;
	private SampleProvider colorSampleProvider;

	public BridgeFollower() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		colorSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
		colorSampleProvider = Ports.COLOR_SENSOR.getColorIDMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		RobotUtils.resetTachos();
		RobotUtils.setSpeed(444);
		RobotUtils.rotate(500);
		
		RobotUtils.setSpeed(333, 444);
		RobotUtils.forward();

		while (onBridge())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();
		System.out.println("rand der brücke gefunden");

		
		BridgeController.run(20000);
		

		RobotUtils.setSpeed(500);
		RobotUtils.forward();

		while (onBridgeColor())
			RobotUtils.checkForKeyPress();

		RobotUtils.setMaxSpeed();
		RobotUtils.rotate(-200);
		
		RobotUtils.turn90DegreesLeft();
		RobotUtils.rotate(700);
		
		RobotUtils.resetTachos();

		RobotUtils.setSpeed(333, 444);
		RobotUtils.forward();
		System.out.println("fahre leicht links");

		while (onBridge())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();
		System.out.println("rand der brücke gefunden");
		
		// regler -- aborts after length driven
		BridgeController.run(2300);

		RobotUtils.setSpeed(500);
		RobotUtils.forward();

		while (onBridgeColor())
			RobotUtils.checkForKeyPress();

		RobotUtils.setMaxSpeed();
		RobotUtils.rotate(-200);

		RobotUtils.turn90DegreesLeft();

		RobotUtils.setSpeed(200);
		RobotUtils.rotate(300);

		RobotUtils.setSpeed(500);

		RobotUtils.forward();

		while (!RGBColorSensor.getInstance().isFinishLine()) {
			RobotUtils.checkForKeyPress();
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			rightTouchSampleProvider.fetchSample(rTouchSample, 0);

			if (lTouchSample[0] > 0.5f) { // check for hitting the wall
				RobotUtils.stopMotors();
				RobotUtils.setSpeed(500);
				Ports.LEFT_MOTOR.rotate(-200, false);
				Ports.RIGHT_MOTOR.rotate(-200, false);
				RobotUtils.forward();
			} else if (rTouchSample[0] > 0.5f) {
				RobotUtils.stopMotors();
				RobotUtils.setSpeed(500);
				Ports.RIGHT_MOTOR.rotate(-200, false);
				Ports.LEFT_MOTOR.rotate(-200, false);
				RobotUtils.forward();
			}
		}

		return WalkableStatus.FINISHED;
	}

	private boolean onBridge() {
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < 0.13f;
	}

	private boolean onBridgeColor() {
		colorSampleProvider.fetchSample(colorSample, 0);
		return colorSample[0] != -1;
	}

}
