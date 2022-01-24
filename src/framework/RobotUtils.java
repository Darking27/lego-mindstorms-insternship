package framework;

import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import lejos.robotics.RegulatedMotor;

public final class RobotUtils {

	public static void turn90DegreesRight() throws KeyPressedException {
//		Ports.LEFT_MOTOR.rotate((int) (0.98*455), true);
//		Ports.RIGHT_MOTOR.rotate(-455, false);
		
		Ports.LEFT_MOTOR.rotate(500, true);
		Ports.RIGHT_MOTOR.rotate(-500, false);
	}

	public static void turn90DegreesLeft() throws KeyPressedException {
//		Ports.LEFT_MOTOR.rotate((int) (0.98*-455), true);
//		Ports.RIGHT_MOTOR.rotate(465, false);
		
		Ports.LEFT_MOTOR.rotate(-510, true);
		Ports.RIGHT_MOTOR.rotate(510, false);
	}

	public static void turnDegreesRight(int degrees) throws KeyPressedException {
		resetTachos();

		int rotation = 465 * degrees / 90;

		if (rotation < 0)
			turn(Math.abs(rotation), false);
		else
			turn(rotation, true);
	}

	public static void turnToNeutralTacho() throws KeyPressedException {
		Ports.LEFT_MOTOR.rotateTo(0, true);
		Ports.RIGHT_MOTOR.rotateTo(0, false);
	}

	public static void driveStraight(int distance) throws KeyPressedException {
		if (distance < 0)
			driveStraight(Math.abs(distance), false);
		else
			driveStraight(distance, true);
	}

	public static void checkForKeyPress() throws KeyPressedException {
		if (Ports.ESCAPE.isDown() || Ports.ENTER.isDown()) {
			Ports.ULTRASONIC_MOTOR.setSpeed(80);
			Ports.ULTRASONIC_MOTOR.rotateTo(0);
			RobotUtils.stopMotors();
		}

		if (Ports.ESCAPE.isDown())
			throw new StopException();
		if (Ports.ENTER.isDown())
			throw new MenuException();
	}

	public static void stopMotors() {
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);
	}

	public static void floatMotors() {
		Ports.RIGHT_MOTOR.flt(true);
		Ports.LEFT_MOTOR.flt(true);
	}
	
	public static void resetTachos() {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
	}
	
	public static int getAverageTacho() {
		return (Ports.LEFT_MOTOR.getTachoCount()+Ports.LEFT_MOTOR.getTachoCount())/2;
	}

	public static void setMaxSpeed() {
		setSpeed((int) Ports.LEFT_MOTOR.getMaxSpeed());
	}

	public static void setSpeed(int speed) {
		Ports.LEFT_MOTOR.setSpeed(speed);
		Ports.RIGHT_MOTOR.setSpeed(speed);
	}

	public static void setSpeed(int left, int right) {
		Ports.LEFT_MOTOR.setSpeed(left);
		Ports.RIGHT_MOTOR.setSpeed(right);
	}

	public static void forward() {
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
	}
	
	public static void backward() {
		Ports.LEFT_MOTOR.backward();
		Ports.RIGHT_MOTOR.backward();
	}

	public static void rotate(int distance) {
		Ports.LEFT_MOTOR.rotate(distance, true);
		Ports.RIGHT_MOTOR.rotate(distance);
	}
	
	public static void driveStraight(int encoderValue, boolean forward) throws KeyPressedException {
		resetTachos();

		int leftTachoCount = 0;
		int rightTachoCount = 0;

		if (forward) {
			Ports.LEFT_MOTOR.forward();
			Ports.RIGHT_MOTOR.forward();
		} else {
			Ports.LEFT_MOTOR.backward();
			Ports.RIGHT_MOTOR.backward();
		}

		while (Math.abs(rightTachoCount) <= encoderValue || Math.abs(rightTachoCount) <= encoderValue) {
			if (Math.abs(rightTachoCount) >= encoderValue) {
				Ports.RIGHT_MOTOR.stop(true);
			}
			if (Math.abs(leftTachoCount) >= encoderValue) {
				Ports.LEFT_MOTOR.stop(true);
			}

			checkForKeyPress();

			leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
			rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
		}

		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop();
	}

	public static void turn(int encoderValue, boolean right) throws KeyPressedException {
		resetTachos();

		RegulatedMotor m1;
		RegulatedMotor m2;

		if (right) {
			m1 = Ports.LEFT_MOTOR;
			m2 = Ports.RIGHT_MOTOR;
		} else {
			m1 = Ports.RIGHT_MOTOR;
			m2 = Ports.LEFT_MOTOR;
		}

		int m1TachoCount = 0;
		int m2TachoCount = 0;

		m1.forward();
		m2.backward();

		while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
			if (m1TachoCount >= encoderValue) {
				m1.stop(true);
			}
			if (m2TachoCount >= encoderValue) {
				m2.stop(true);
			}

			checkForKeyPress();

			m1TachoCount = Math.abs(m1.getTachoCount());
			m2TachoCount = Math.abs(m2.getTachoCount());
		}

		m1.stop(true);
		m2.stop();
	}
	
	public static void straight(int distance) {
		Ports.LEFT_MOTOR.rotate((int) (0.98*distance), true);
		Ports.RIGHT_MOTOR.rotate(distance, false);
	}
}
