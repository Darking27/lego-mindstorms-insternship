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

public class ExitFinder2 implements ParcoursWalkable {

	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;

	public ExitFinder2() {
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		RobotUtils.stopMotors();

		Ports.RIGHT_MOTOR.rotate(-400);
		Ports.LEFT_MOTOR.rotate(-400);

		RobotUtils.turn90DegreesLeft();
		RobotUtils.turn90DegreesLeft();

		System.out.println("drive til touch 2");
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		do {
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();

			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		} while (lTouchSample[0] < 0.5f || rTouchSample[0] < 0.5f);
		RobotUtils.stopMotors();

		Ports.LEFT_MOTOR.rotate(-800);
		RobotUtils.driveStraight(-400);
		Ports.RIGHT_MOTOR.rotate(-500);

		Ports.LEFT_MOTOR.setSpeed(360);
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();

		while (!RGBColorSensor.getInstance().isFinishLine())
			;

		RobotUtils.stopMotors();

		return WalkableStatus.FINISHED;
	}

}
