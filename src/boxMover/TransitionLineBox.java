package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;

public class TransitionLineBox implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		/*
		 * The Line Follower is a bit tilted when exiting the parcours
		 */
		RobotUtils.setMaxSpeed();
		Ports.RIGHT_MOTOR.rotate(600);
		return WalkableStatus.FINISHED;
	}

}
