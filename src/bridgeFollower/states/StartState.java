package bridgeFollower.states;

import driving.DriveParallelTask;
import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;

public class StartState extends BaseState {
	
	public static final int DISTANCE = 800;

	@Override
	public State handleState() throws KeyPressedException {
		RobotUtils.stopMotors();
		DriveParallelTask driveTask = new DriveParallelTask(400);
		while(true) {
			driveTask.run();
			
			handleKeyPressed();
			
			if (Ports.LEFT_MOTOR.getTachoCount() > DISTANCE
					|| Ports.RIGHT_MOTOR.getTachoCount() > DISTANCE) {
				driveTask.cancel();
				return State.FIND_LEFT;
			}
		}
	}

}
