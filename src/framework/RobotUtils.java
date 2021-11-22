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
		
		int rotation = 440 * degrees / 90;
		
		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(rotation, true);
		Ports.RIGHT_MOTOR.rotate(-rotation, true);

		checkForKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void turnToNeutralTacho() throws KeyPressedException {
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotateTo(0, true);
		Ports.RIGHT_MOTOR.rotateTo(0, false);
	}

	public static void driveStraight(int distance) throws KeyPressedException {
		int rotation = distance;
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(rotation, true);
		Ports.RIGHT_MOTOR.rotate(rotation, true);

		checkForKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void checkForKeyPress() throws KeyPressedException {
		while (Ports.LEFT_MOTOR.isMoving() || Ports.RIGHT_MOTOR.isMoving()) {
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
		}
	}
	
	public static void stopMotors() {
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}
	
	public static void resetTachos() {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
	}
}
