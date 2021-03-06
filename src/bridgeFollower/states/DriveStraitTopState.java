package bridgeFollower.states;

import driving.DriveParallelTask;
import exceptions.KeyPressedException;
import framework.RobotUtils;

public class DriveStraitTopState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		RobotUtils.stopMotors();
		DriveParallelTask driveTask = new DriveParallelTask(300);
		System.out.println("Start Regler");
		while(true) {
			driveTask.run();
			
			handleKeyPressed();
			
			if (seeingEdge()) {
				driveTask.cancel();
				return State.TURN_LEFT_2;
			}
		}
	}

}
