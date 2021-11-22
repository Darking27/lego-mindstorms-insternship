package framework;

import exceptions.KeyPressedException;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		Delay.msDelay(500);
		RobotUtils.turn90DegreesRight();
		return WalkableStatus.FINISHED;
	}
}
