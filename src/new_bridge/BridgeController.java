package new_bridge;

import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;
import lejos.robotics.SampleProvider;

public class BridgeController {
	
	static float[] uSample = new float[1];
	static float[] colorSample = new float[1];
	static SampleProvider ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	static SampleProvider colorSampleProvider = Ports.COLOR_SENSOR.getColorIDMode();
	
	public static void run(int DISTANCE) throws KeyPressedException{		
		
		long time = System.nanoTime();
		int varSpeed = 250;
		int defaultSpeed = 300;
		float Kp = 0.000000001f;

		boolean onBridge = true;
		while (true) {
			RobotUtils.checkForKeyPress();

			if (Ports.LEFT_MOTOR.getTachoCount() > DISTANCE || Ports.RIGHT_MOTOR.getTachoCount() > DISTANCE) {
				RobotUtils.stopMotors();
				System.out.println("distance returned");
				return;
			}
			if (isOverBridge() && !onBridge) {
				onBridge = true;
				time = System.nanoTime();
			}
			if (!isOverBridge() && onBridge) {
				onBridge = false;
				time = System.nanoTime();
			}

			int leftSpeed;
			int rightSpeed;

			float fac = onBridge ? Kp : -Kp;

			float diff = System.nanoTime() - time;

			float change;
			change = varSpeed * diff * fac;
			change = Math.min(varSpeed, change);
			change = Math.max(-varSpeed, change);

			leftSpeed = (int) (defaultSpeed - change);
			rightSpeed = (int) (defaultSpeed + change);

			leftSpeed = Math.max(0, leftSpeed);
			rightSpeed = Math.max(0, rightSpeed);

			RobotUtils.setSpeed(leftSpeed, rightSpeed);
			RobotUtils.forward();

		}
	}
	
	public static boolean isOverBridge() {
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < 0.13f;
	}

}
