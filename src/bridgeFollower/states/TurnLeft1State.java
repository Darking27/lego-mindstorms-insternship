package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft1State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {		
		driveStraight(550, true, 250);
		turn(460, false, 250);
		driveStraight(700, true, 250);
		RobotUtils.stopMotors();
		return State.FOLLOW_TOP;
	}

}
