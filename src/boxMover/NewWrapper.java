package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class NewWrapper implements ParcoursWalkable {
	
	private CurveBoxMover boxFinder;
	private WallFollower wallFollower;
	
	public NewWrapper() {
		wallFollower = new WallFollower();
		boxFinder = new CurveBoxMover();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		wallFollower.start_walking();
		
		boxFinder.start_walking();
				
		return WalkableStatus.FINISHED;
	}
}
