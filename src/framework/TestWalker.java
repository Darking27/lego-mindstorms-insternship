package framework;

import exceptions.KeyPressedException;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		System.out.println("TEST WALKER");
		RobotUtils.setSpeed(360);
		
		RobotUtils.turn90DegreesRight();
		RobotUtils.turn90DegreesRight();
		RobotUtils.turn90DegreesRight();
		RobotUtils.turn90DegreesRight();
		
		return WalkableStatus.MENU;
	}
	
	public static void main(String[] args) throws KeyPressedException {
		System.out.println("TEST WALKER");
		RobotUtils.setSpeed(400, 500);
		Ports.RIGHT_MOTOR.rotate(-1500, true);
		Ports.LEFT_MOTOR.rotate(-1500);
		RobotUtils.turn90DegreesRight();
	}
}
