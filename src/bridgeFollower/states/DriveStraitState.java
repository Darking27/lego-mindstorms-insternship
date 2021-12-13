package bridgeFollower.states;

import driving.DriveParallelTask;
import exceptions.KeyPressedException;

public class DriveStraitState extends BaseState {

	@Override
	public State handleState() throws KeyPressedException {
		DriveParallelTask driveTask = new DriveParallelTask(300);
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
