package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.RobotUtils;

public class TurnLeft1State extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {		
		driveStraight(650, true, 200);
		turn(480, false, 200);
		driveStraight(500, true, 200);
		return State.FOLLOW_TOP;
	}

}
