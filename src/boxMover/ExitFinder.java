package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lineFollower.colorSensor.RGBColorSensor;

public class ExitFinder implements ParcoursWalkable {

	private float[] lTouchSample;
	private SampleProvider leftTouchSampleProvider;

	public ExitFinder() {
		lTouchSample = new float[1];
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Ports.RIGHT_MOTOR.setSpeed(700);
		Ports.LEFT_MOTOR.setSpeed(350);
		Ports.LEFT_MOTOR.backward();
		Ports.RIGHT_MOTOR.rotate(-1920);
		RobotUtils.stopMotors();
		
		System.out.println("turn 90 right");
		RobotUtils.turn90DegreesRight();
		RobotUtils.turnDegreesRight(10, 360);
	
		System.out.println("drive forward");
		RobotUtils.setMaxSpeed();
		RobotUtils.forward();
		while (!RGBColorSensor.getInstance().isFinishLine()) {
			RobotUtils.checkForKeyPress();
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			if (lTouchSample[0]>0.5) {	// check for hitting the wall
				RobotUtils.stopMotors();
				Ports.LEFT_MOTOR.rotate(-400, false);
				Ports.RIGHT_MOTOR.rotate(-400, false);			
				RobotUtils.forward();
			}	
		};
		
		RobotUtils.stopMotors();
		return WalkableStatus.FINISHED;
	}

}
