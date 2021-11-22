package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class BoxMoverWrapper implements ParcoursWalkable {
	
	private RightAngleBoxFinder boxFinder;
	private WallFollower wallFollower;
	
	public BoxMoverWrapper() {
		boxFinder = new RightAngleBoxFinder();
		wallFollower = new WallFollower();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		wallFollower.start_walking();
		
		boxFinder.start_walking();
		
		return WalkableStatus.FINISHED;
	}
}
