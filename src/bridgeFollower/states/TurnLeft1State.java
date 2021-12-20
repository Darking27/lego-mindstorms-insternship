package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft1State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {		
		driveStraight(550, true, 600);
		turn(460, false, 600);
		driveStraight(700, true, 600);
		RobotUtils.stopMotors();
		return State.FOLLOW_TOP;
	}

}
