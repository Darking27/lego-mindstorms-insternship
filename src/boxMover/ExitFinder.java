package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lineFollower.colorSensor.RGBColorSensor;

public class ExitFinder implements ParcoursWalkable {

	private float[] lTouchSample;
	private float[] rTouchSample;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;
	private float[] uSample;
	private SampleProvider ultrasonicSampleProvider;

	public ExitFinder() {
		lTouchSample = new float[1];
		rTouchSample = new float[1];
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
		uSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		RobotUtils.setMaxSpeed();
		Ports.LEFT_MOTOR.rotate(-600,true);  // drive back
		Ports.RIGHT_MOTOR.rotate(-600); 
		

		Ports.RIGHT_MOTOR.rotate(200); //turn a bit left
		
		Ports.LEFT_MOTOR.rotate(400,true); //drive straight to gain distance to the wall
		Ports.RIGHT_MOTOR.rotate(400);
		
		Ports.RIGHT_MOTOR.rotate(-300); // turn right to 

		Ports.LEFT_MOTOR.rotate(-1000, true); //drive back
		Ports.RIGHT_MOTOR.rotate(-1000);

		Ports.LEFT_MOTOR.rotate((int) (0.98 * 410), true); //look at wall again
		Ports.RIGHT_MOTOR.rotate(-410, false);	
		

		RobotUtils.forward();			//richte an der wand aus
		while (!bothTouchSensorsDown())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();

		RobotUtils.setSpeed(400);
		RobotUtils.backward();
		while (!correctDistanceToWall())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();

		Ports.LEFT_MOTOR.rotate((int) (0.98*490), true);
		Ports.RIGHT_MOTOR.rotate(-490, false);
		
		RobotUtils.forward();
		while (!RGBColorSensor.getInstance().isFinishLine()) {
			RobotUtils.checkForKeyPress();
		}

		RobotUtils.stopMotors();
		return WalkableStatus.FINISHED;
	}

	private boolean bothTouchSensorsDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		boolean a =  lTouchSample[0] > 0.5f && rTouchSample[0] > 0.5f;
		
		Delay.msDelay(70);
		
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		boolean b =  lTouchSample[0] > 0.5f && rTouchSample[0] > 0.5f;
		
		return a && b;
	}

	private boolean correctDistanceToWall() {
		float correct_distance = 0.363f;
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] >= correct_distance;
	}
}
