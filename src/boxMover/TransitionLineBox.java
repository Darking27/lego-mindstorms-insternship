package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;

public class TransitionLineBox implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		RobotUtils.soundSignal();
		/*
		 * The Line Follower is a bit tilted when exiting the parcours
		 */
		RobotUtils.setSpeed(500);
		Ports.RIGHT_MOTOR.rotate(500);
		return WalkableStatus.FINISHED;
	}

}
