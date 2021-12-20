package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft2State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		driveStraight(200, false, 400);
		turn(450, false, 400);
		driveStraight(700, true, 250);
		//return State.FOLLOW_LEFT_DOWN;
		RobotUtils.stopMotors();
		System.out.println("gerade runter");
		return State.DRIVE_STRAIT_DOWN;
	}

}
