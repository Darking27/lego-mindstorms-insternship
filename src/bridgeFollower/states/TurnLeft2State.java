package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft2State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		RobotUtils.driveStraight(-200);
		RobotUtils.turn90DegreesLeft();
		return State.FIND_LEFT;
	}

}
