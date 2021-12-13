package framework;

import exceptions.KeyPressedException;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		RobotUtils.setSpeed(360);
		
		RobotUtils.turn90DegreesRight();
		
		return WalkableStatus.FINISHED;
	}
}
