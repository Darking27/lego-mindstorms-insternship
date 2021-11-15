package boxMover;

import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class BoxMover implements ParcoursWalkable {
	
	@Override
	public WalkableStatus start_walking() {
		WalkableStatus status;
		
		WallFollower wallFollower = new WallFollower();
		status = wallFollower.start_walking();
		if(status != WalkableStatus.FINISHED) {
			return status;
		}
		
		BoxFinder boxFinder = new BoxFinder();
		status = boxFinder.start_walking();
		if(status != WalkableStatus.FINISHED) {
			return status;
		}
		
		return WalkableStatus.FINISHED;
	}
}
