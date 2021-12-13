package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.Ports;

public class ParallelDriveRightState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		Ports.LEFT_MOTOR.setSpeed(200);
		Ports.RIGHT_MOTOR.setSpeed(200);
		
		Ports.LEFT_MOTOR.forward();
		while (Ports.LEFT_MOTOR.getTachoCount() < 600);
		Ports.LEFT_MOTOR.stop();
		
		Ports.RIGHT_MOTOR.forward();
		while (Ports.RIGHT_MOTOR.getTachoCount() < 600);
		Ports.RIGHT_MOTOR.stop();
		
		return State.DRIVE_STRAIT;
	}

}
