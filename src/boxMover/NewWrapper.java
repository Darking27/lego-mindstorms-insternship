package boxMover;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class NewWrapper implements ParcoursWalkable {
	
	private NewBoxFinder boxFinder;
	private WallFollower wallFollower;
	
	public NewWrapper() {
		boxFinder = new NewBoxFinder();
		wallFollower = new WallFollower();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		wallFollower.start_walking();
		
		boxFinder.start_walking();
				
		return WalkableStatus.FINISHED;
	}
}
