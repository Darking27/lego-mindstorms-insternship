package bridgeFollower.states;

import driving.DriveParallelTask;
import exceptions.KeyPressedException;
import framework.RobotUtils;

public class DriveStraitDownState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		RobotUtils.stopMotors();
		DriveParallelTask driveTask = new DriveParallelTask(300);
		while(true) {
			driveTask.run();
			
			handleKeyPressed();
			
			if (touchLeft() || touchRight()) {
				driveTask.cancel();
				System.out.println("Start tunnelfinder");
				return State.TUNNEL_FINDER;
			}
		}
	}

}
