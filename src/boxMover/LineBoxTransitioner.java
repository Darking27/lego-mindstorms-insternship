package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;

public class LineBoxTransitioner implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		/*
		 * The Line Follower is a bit tilted when exiting the parcours
		 */
		Ports.RIGHT_MOTOR.rotate(600);
		return WalkableStatus.FINISHED;
	}

}
