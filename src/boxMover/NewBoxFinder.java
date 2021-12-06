package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class NewBoxFinder implements ParcoursWalkable {
	
	private float[] uSample;
	private SampleProvider ultrasonicSampleProvider;
	
	public NewBoxFinder() {
		uSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	}
	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		float dist = Float.POSITIVE_INFINITY;
		int leftTacho = 0;
		int rightTacho = 0;
		
		RobotUtils.resetTachos();
		RobotUtils.setSpeed(150);
		int turn = 500;
		Ports.LEFT_MOTOR.rotate(turn, true);
		Ports.RIGHT_MOTOR.rotate(-turn, true);
		
		while (Ports.LEFT_MOTOR.isMoving()) {
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			if (uSample[0]<dist) {
				dist = uSample[0];
				leftTacho = Ports.LEFT_MOTOR.getTachoCount();
				rightTacho = Ports.RIGHT_MOTOR.getTachoCount();
			}
		}
		
		Ports.LEFT_MOTOR.rotateTo(leftTacho-12, true);
		Ports.RIGHT_MOTOR.rotateTo(rightTacho+12, false);
		
		RobotUtils.setMaxSpeed();
		RobotUtils.forward();
		RobotUtils.checkForKeyPressAsLongAsMoving();
		
		return WalkableStatus.FINISHED;
	}
	
	
	

}
