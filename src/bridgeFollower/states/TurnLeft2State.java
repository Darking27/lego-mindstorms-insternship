package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft2State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		driveStraight(200, false, 200);
		turn(450, false, 200);
		driveStraight(700, true, 200);
		//RobotUtils.driveStraight(-200);
		//RobotUtils.turn90DegreesLeft();
		return State.FOLLOW_LEFT_DOWN;
	}

}
