package framework;

import exceptions.KeyPressedException;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		RobotUtils.setSpeed(400);
		Ports.LEFT_MOTOR.rotate(300, false);		//gain some distance to the wall
		Ports.RIGHT_MOTOR.rotate(300, false);
		return WalkableStatus.FINISHED;
	}
}
