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
		
		Ports.RIGHT_MOTOR.rotate(-400); // turn right to 

		Ports.LEFT_MOTOR.rotate(-1000, true); //drive back
		Ports.RIGHT_MOTOR.rotate(-1000);

		Ports.LEFT_MOTOR.rotate((int) (0.98 * 400), true); //look at wall again
		Ports.RIGHT_MOTOR.rotate(-400, false);	
		
		// 0.34
//		Ports.LEFT_MOTOR.rotate((int) (0.98 * 455), true);
//		Ports.RIGHT_MOTOR.rotate(-455, false);

		RobotUtils.forward();			//richte an der wand aus
		while (!bothTouchSensorsDown())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();

		RobotUtils.backward();
		while (!correctDistanceToWall())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();

		Ports.LEFT_MOTOR.rotate((int) (0.98*480), true);
		Ports.RIGHT_MOTOR.rotate(-480, false);
		
		RobotUtils.forward();
		while (!RGBColorSensor.getInstance().isFinishLine()) {
			RobotUtils.checkForKeyPress();
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			if (lTouchSample[0] > 0.5) { // check for hitting the wall
				RobotUtils.stopMotors();
				RobotUtils.setSpeed(500);
				Ports.LEFT_MOTOR.rotate(-400, false);
				Ports.RIGHT_MOTOR.rotate(-400, false);
				RobotUtils.forward();
			}
		}
		;

//		Ports.RIGHT_MOTOR.setSpeed(700);
//		Ports.LEFT_MOTOR.setSpeed(350);
//		Ports.LEFT_MOTOR.backward();
//		Ports.RIGHT_MOTOR.rotate(-1920);
//		RobotUtils.stopMotors();
//		
//		RobotUtils.setSpeed(500);
//		System.out.println("turn 90 right");
//		RobotUtils.turn90DegreesRight();
//		
//		RobotUtils.setMaxSpeed();
//		System.out.println("drive forward");
//		
//		RobotUtils.setSpeed(400);
//		RobotUtils.forward();
//		
//		while (!RGBColorSensor.getInstance().isFinishLine()) {
//			RobotUtils.checkForKeyPress();
//			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
//			if (lTouchSample[0]>0.5) {	// check for hitting the wall
//				RobotUtils.stopMotors();
//				Ports.LEFT_MOTOR.rotate(-400, false);
//				Ports.RIGHT_MOTOR.rotate(-400, false);			
//				RobotUtils.forward();
////				RobotUtils.stopMotors();
////				RobotUtils.setSpeed(400);
////				RobotUtils.straight(-200);
////				Ports.LEFT_MOTOR.rotate(150);
////				RobotUtils.forward();
//			}	
//		};

		RobotUtils.stopMotors();
		return WalkableStatus.FINISHED;
	}

	private boolean bothTouchSensorsDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		return lTouchSample[0] > 0.5f && rTouchSample[0] > 0.5f;
	}

	private boolean correctDistanceToWall() {
		float correct_distance = 0.31f;
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] >= correct_distance;
	}
}
