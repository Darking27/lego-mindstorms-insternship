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
		RobotUtils.stopMotors();
		
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.LEFT_MOTOR.setSpeed(190);
		Ports.LEFT_MOTOR.backward();
		Ports.RIGHT_MOTOR.rotate(-1920);
		RobotUtils.stopMotors();
		
		RobotUtils.turn90DegreesRight();
	
		Ports.LEFT_MOTOR.setSpeed(360);
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		
		while (!RGBColorSensor.getInstance().isFinishLine()) {
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			if (lTouchSample[0]>0.5) {
				RobotUtils.stopMotors();
				Ports.LEFT_MOTOR.rotate(-400, false);
				Ports.RIGHT_MOTOR.rotate(-400, false);			
				Ports.LEFT_MOTOR.forward();
				Ports.RIGHT_MOTOR.forward();
			}	
		};
		
		RobotUtils.stopMotors();
		
		
		return WalkableStatus.FINISHED;
	}

}
