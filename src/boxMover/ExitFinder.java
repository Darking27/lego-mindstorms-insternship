package boxMover;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lineFollower.colorSensor.RGBColorSensor;

public class ExitFinder implements ParcoursWalkable {

	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public ExitFinder() {
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		RobotUtils.stopMotors();
		
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.LEFT_MOTOR.setSpeed(190);
		Ports.LEFT_MOTOR.backward();
		Ports.RIGHT_MOTOR.rotate(-2300);
		RobotUtils.stopMotors();
		
		RobotUtils.turn90DegreesRight();
	
		Ports.LEFT_MOTOR.setSpeed(360);
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		while (!RGBColorSensor.getInstance().isFinishLine());
		
		RobotUtils.stopMotors();
		
		
		return WalkableStatus.FINISHED;
	}

}
