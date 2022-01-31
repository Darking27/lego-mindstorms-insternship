package new_bridge;

import bridgeFollower.TunnelFinder;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lineFollower.colorSensor.RGBColorSensor;

public class FullBridgeController implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private float[] colorSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;
	private SampleProvider colorSampleProvider;

	public FullBridgeController() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		colorSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
		colorSampleProvider = Ports.COLOR_SENSOR.getColorIDMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		RobotUtils.setMaxSpeed();
		RobotUtils.rotate(400);

		Ports.ULTRASONIC_MOTOR.rotateTo(-90);
		RobotUtils.resetTachos();
		while (!onBridge())
			RobotUtils.checkForKeyPress();
		RobotUtils.setSpeed(350, 400);
		RobotUtils.forward();
		while (onBridge())
			RobotUtils.checkForKeyPress();
		RobotUtils.stopMotors();
		System.out.println("edge found: " + RobotUtils.getAverageTacho());
		
		if(RobotUtils.getAverageTacho()<1000) {
			Ports.LEFT_MOTOR.rotate(50);
		}else {
			Ports.LEFT_MOTOR.rotate(100);
		}
		

		RobotUtils.forward();
		int numOnBridge = 0;
		while (numOnBridge < 12) {
			RobotUtils.checkForKeyPress();
			Delay.msDelay(100);
			if (onBridge()) {
				RobotUtils.setSpeed(300, 400);
				numOnBridge++;
			} else {
				RobotUtils.setSpeed(400, 300);
				numOnBridge = 0;
			}
			RobotUtils.forward();
		}

		System.out.println("on top of bridge");
		RobotUtils.setSpeed(350);
		RobotUtils.rotate(350);

		RobotUtils.setSpeed(450);
		Ports.LEFT_MOTOR.rotate((int) (0.98 * -500), true);
		Ports.RIGHT_MOTOR.rotate(660, false);
		System.out.println("turned left");
		
		RobotUtils.setSpeed(300);
		RobotUtils.forward();
		while(onBridge()) {
			RobotUtils.checkForKeyPress();
		}
		RobotUtils.stopMotors();
		System.out.println("parrallel to top bridge");
		RobotUtils.setSpeed(300);
		Ports.LEFT_MOTOR.rotate(200);
		
		RobotUtils.setSpeed(600);
		Ports.LEFT_MOTOR.rotate(350);
		Ports.RIGHT_MOTOR.rotate(350);
		RobotUtils.forward();
		while(onBridgeColor()) {
			RobotUtils.checkForKeyPress();
		}
		
		RobotUtils.setSpeed(300);
		RobotUtils.rotate(-200);
		RobotUtils.turn90DegreesLeft();
		RobotUtils.forward();
		while(!oneTouchDown()) 
			if(RGBColorSensor.getInstance().isFinishLine()) {
				Ports.ULTRASONIC_MOTOR.rotateTo(0);
				return WalkableStatus.FINISHED;
			}
			RobotUtils.checkForKeyPress();
	

		return new TunnelFinder().start_walking();
	}

	private boolean oneTouchDown() {
		leftTouchSampleProvider.fetchSample(lTouchSample, 0);
		rightTouchSampleProvider.fetchSample(rTouchSample, 0);
		return lTouchSample[0] > 0.5f || rTouchSample[0] > 0.5f;
	}

	private boolean onBridge() {
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < 0.13f;
	}
	
	private boolean onBridgeColor() {
		colorSampleProvider.fetchSample(colorSample, 0);
		return colorSample[0] != -1f;
	}
}
