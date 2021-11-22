package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class BoxMoverWrapper implements ParcoursWalkable {
	
	private RightAngleBoxFinder boxFinder;
	private WallFollower wallFollower;
	private ExitFinder exitFinder;
	
	public BoxMoverWrapper() {
		boxFinder = new RightAngleBoxFinder();
		wallFollower = new WallFollower();
		exitFinder = new ExitFinder();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		wallFollower.start_walking();
		
		boxFinder.start_walking();
		
		exitFinder.start_walking();
		
		return WalkableStatus.FINISHED;
	}
}
