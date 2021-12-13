package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft1State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {		
		driveStraight(600, true, 200);
		turn(460, false, 200);
		driveStraight(700, true, 200);
		RobotUtils.stopMotors();
		return State.FOLLOW_TOP;
	}

}
