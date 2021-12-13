package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;

public class ParallelDriveRightState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		System.out.println("Parallel right");
		
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		Ports.LEFT_MOTOR.setSpeed(200);
		Ports.RIGHT_MOTOR.setSpeed(200);
		RobotUtils.stopMotors();
		
		Ports.LEFT_MOTOR.forward();
		while (Ports.LEFT_MOTOR.getTachoCount() < 300);
		Ports.LEFT_MOTOR.stop();
		
		Ports.RIGHT_MOTOR.forward();
		while (Ports.RIGHT_MOTOR.getTachoCount() < 400);
		Ports.RIGHT_MOTOR.stop();
		
		return State.DRIVE_STRAIT;
	}

}
