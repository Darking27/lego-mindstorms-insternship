package other.motorDelayTest;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

public class Mover {
	
	EV3LargeRegulatedMotor leftMotor;
	EV3LargeRegulatedMotor rightMotor;
	Brick brick;
	
	Mover(Brick brick, String leftMotorPortName, String rightMotorPortName) {
		this.brick = brick;
		Port leftMotorPort = brick.getPort(leftMotorPortName);
		Port rightMotorPort = brick.getPort(rightMotorPortName);
		this.leftMotor = new EV3LargeRegulatedMotor(leftMotorPort);
		this.rightMotor = new EV3LargeRegulatedMotor(rightMotorPort);
	}
	
	public void moveMotors() {
		leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		
		Key escape = brick.getKey("Escape");
		
		while (!escape.isDown()) {
			leftMotor.forward();
			rightMotor.forward();
			Delay.msDelay(2000);
			leftMotor.stop();
			rightMotor.stop();
			Delay.msDelay(1000);
		}
	}
}
