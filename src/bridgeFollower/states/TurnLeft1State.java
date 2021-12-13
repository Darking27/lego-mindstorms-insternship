package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft1State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		RobotUtils.driveStraight(200);
		RobotUtils.turn90DegreesLeft();
		return State.FOLLOW_TOP;
	}

}
