package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;

public class FindLeftState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		System.out.println("Find left");
		
		RobotUtils.stopMotors();
		
		Ports.RIGHT_MOTOR.setSpeed(400);
		Ports.LEFT_MOTOR.setSpeed(330);
		Ports.RIGHT_MOTOR.forward();
		Ports.LEFT_MOTOR.forward();
		
		while (isOverBridge()) {
			handleKeyPressed();
		}
		
		RobotUtils.stopMotors();
		return State.FOLLOW_LEFT_UP;
	}

}
