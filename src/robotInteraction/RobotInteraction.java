package robotInteraction;

import java.util.List;

import framework.Ports;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lineFollower.v2.LineFollowerState;

public class RobotInteraction {

	/**
	 * TODO: Needs to return the found event
	 */
	public static void msDelay(int ms) {
		//check for enter button press to exit Walker
	}
	
	public static void msDelay(int ms, List<Color> toCheck) {
		//check for enter button press to exit Walker
		//check if one of the colors was found
	}
	
	// TODO Maybe add listen to multiple Enput events
	public static InputEvents encoderDelayForward(int encoderValue) {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		
		int leftTachoCount = 0;
		int rightTachoCount = 0;
		
		Ports.LEFT_MOTOR.setSpeed(400);
		Ports.RIGHT_MOTOR.setSpeed(400);
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		while (leftTachoCount <= encoderValue || rightTachoCount <= encoderValue) {
			if (rightTachoCount >= encoderValue) {
				Ports.RIGHT_MOTOR.stop(true);
			}
			if (leftTachoCount >= encoderValue) {
				Ports.LEFT_MOTOR.stop(true);
			}
			if (Ports.ENTER.isDown()) {
				return InputEvents.ENTER;
			}
			leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
			rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
		}
		
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(true);
		
		return InputEvents.NONE;
	}
	
	// TODO Maybe add listen to multiple Enput events
		public static InputEvents encoderDelayTurn(int encoderValue, boolean rightTurn, SampleProvider colorSensorFilter) {
			float[] sample = new float[colorSensorFilter.sampleSize()];
			
			Ports.LEFT_MOTOR.resetTachoCount();
			Ports.RIGHT_MOTOR.resetTachoCount();
			
			RegulatedMotor m1;
			RegulatedMotor m2;
			
			if (rightTurn) {
				m1 = Ports.LEFT_MOTOR;
				m2 = Ports.RIGHT_MOTOR;
			} else {
				m1 = Ports.RIGHT_MOTOR;
				m2 = Ports.LEFT_MOTOR;
			}
			
			int m1TachoCount = 0;
			int m2TachoCount = 0;
			
			Ports.LEFT_MOTOR.setSpeed(400);
			Ports.RIGHT_MOTOR.setSpeed(400);
			
			m1.forward();
			m2.backward();
			
			while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
				if (m1TachoCount >= encoderValue) {
					m1.stop(true);
				}
				if (m2TachoCount >= encoderValue) {
					m2.stop(true);
				}
				if (Ports.ENTER.isDown()) {
					return InputEvents.ENTER;
				}
				
				m1TachoCount = Math.abs(m1.getTachoCount());
				m2TachoCount = Math.abs(m2.getTachoCount());
				
				colorSensorFilter.fetchSample(sample, 0);
			}
			
			m1.stop(true);
			m2.stop(true);
			
			return InputEvents.NONE;
		}
}
