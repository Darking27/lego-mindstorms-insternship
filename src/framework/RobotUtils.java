package framework;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;

public final class RobotUtils {

	public static void turn90DegreesRight() throws KeyPressedException {
		turnDegreesRight(90, 360);
	}
	
	public static void turn90DegreesLeft() throws KeyPressedException {
		turnDegreesRight(-90, 360);
	}

	
	public static void turnDegreesRight(int degrees, int speed) throws KeyPressedException {
		resetTachos();
		
		int rotation = 465 * degrees / 90;
		
		setSpeed(speed);

		Ports.LEFT_MOTOR.rotate((int) (0.98*rotation), true);
		Ports.RIGHT_MOTOR.rotate(-rotation, false);

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void turnToNeutralTacho() throws KeyPressedException {
		setMaxSpeed();

		Ports.LEFT_MOTOR.rotateTo(0, true);
		Ports.RIGHT_MOTOR.rotateTo(0, false);
	}

	public static void driveStraight(int distance) throws KeyPressedException {
		setMaxSpeed();
		
		int rotation = distance;
		Ports.LEFT_MOTOR.rotate(rotation, true);
		Ports.RIGHT_MOTOR.rotate(rotation, true);

		checkForKeyPressAsLongAsMoving();

		stopMotors();
	}

	public static void checkForKeyPressAsLongAsMoving() throws KeyPressedException {
		while (Ports.LEFT_MOTOR.isMoving() || Ports.RIGHT_MOTOR.isMoving()) {
			checkForKeyPress();
		}
	}
	
	public static void checkForKeyPress() throws KeyPressedException{
		if (Ports.ESCAPE.isDown())
			throw new StopException();
		if (Ports.ENTER.isDown())
			throw new MenuException();
	}
	
	public static void stopMotors() {
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}
	
	public static void resetTachos() {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
	}
	
	public static void setMaxSpeed() {
		setSpeed((int) Ports.LEFT_MOTOR.getMaxSpeed());
	}
	
	public static void setSpeed(int speed) {
		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);
	}
	
	public static void forward() {
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
	}
}

