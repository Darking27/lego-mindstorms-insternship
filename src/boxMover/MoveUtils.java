package boxMover;

import exception.MenuException;
import exception.KeyPressedException;
import exception.StopException;
import framework.Ports;

public final class MoveUtils {

	public static void turn90DegreesRight() throws KeyPressedException {
		int rotation = 300;
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(rotation, true);
		Ports.RIGHT_MOTOR.rotate(-rotation, true);

		checkKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void turn90DegreesLeft() throws KeyPressedException {
		int rotation = 300;
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(-rotation, true);
		Ports.RIGHT_MOTOR.rotate(rotation, true);

		checkKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void turnToNeutralTacho() throws KeyPressedException {
		int rotationLeft = Ports.LEFT_MOTOR.getTachoCount();
		int rotationRight = Ports.RIGHT_MOTOR.getTachoCount();
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(-rotationLeft, true);
		Ports.RIGHT_MOTOR.rotate(-rotationRight, true);

		checkKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void driveStraight(int distance) throws KeyPressedException {
		int rotation = distance;
		int speed = 360;

		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);

		Ports.LEFT_MOTOR.rotate(rotation, true);
		Ports.RIGHT_MOTOR.rotate(rotation, true);

		checkKeyPress();

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	private static void checkKeyPress() throws KeyPressedException {
		while (Ports.LEFT_MOTOR.isMoving() || Ports.RIGHT_MOTOR.isMoving()) {
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
		}
	}
}
