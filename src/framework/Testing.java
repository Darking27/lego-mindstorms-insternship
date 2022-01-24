package framework;

import exceptions.KeyPressedException;
import lejos.utility.Delay;

public class Testing {

	public static void main(String[] args) {
		RobotUtils.setSpeed(500);
		
		try {
//			Delay.msDelay(500);
//			RobotUtils.turn90DegreesRight();
//			Delay.msDelay(500);
//			RobotUtils.turn90DegreesRight();
//			Delay.msDelay(500);
//			RobotUtils.turn90DegreesRight();
//			Delay.msDelay(500);
//			RobotUtils.turn90DegreesRight();
//			Delay.msDelay(500);
			
			Delay.msDelay(500);
			RobotUtils.turn90DegreesLeft();
			Delay.msDelay(500);
			RobotUtils.turn90DegreesLeft();
			Delay.msDelay(500);
			RobotUtils.turn90DegreesLeft();
			Delay.msDelay(500);
			RobotUtils.turn90DegreesLeft();
			Delay.msDelay(500);		
		} catch (KeyPressedException e) {}
			
		

	}

}
