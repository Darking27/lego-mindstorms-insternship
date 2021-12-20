package bridgeFollower.states;

import driving.DriveParallelTask;
import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lineFollower.colorSensor.RGBColorSensor;

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
			
			if (RGBColorSensor.getInstance().isFinishLine()) {
				RobotUtils.stopMotors();
				Ports.ULTRASONIC_MOTOR.setSpeed(80);
				Ports.ULTRASONIC_MOTOR.rotateTo(0);
				return State.FINISH;
			}
		}
	}

}
