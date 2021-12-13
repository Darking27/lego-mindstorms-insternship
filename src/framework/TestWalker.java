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
		RobotUtils.setSpeed(360);
		
		RobotUtils.turn90DegreesLeft();
		RobotUtils.turn90DegreesLeft();
		RobotUtils.turn90DegreesLeft();
		RobotUtils.turn90DegreesLeft();
//		
//		RobotUtils.turn90DegreesRight();
//		RobotUtils.turn90DegreesRight();
//		RobotUtils.turn90DegreesRight();
//		RobotUtils.turn90DegreesRight();
	}
}
