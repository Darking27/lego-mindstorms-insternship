package framework;

import exceptions.KeyPressedException;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		Delay.msDelay(500);
		//RobotUtils.turn90DegreesRight();
		RobotUtils.turn90DegreesLeft();
		return WalkableStatus.FINISHED;
	}
}
