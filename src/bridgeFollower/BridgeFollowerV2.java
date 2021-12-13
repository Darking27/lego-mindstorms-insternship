package bridgeFollower;

import display.Logger;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * ParcoursWalkable for the bridge
 * only using a controller
 * 
 * @author Niklas Arlt
 *
 */
public class BridgeFollowerV2 implements ParcoursWalkable {
	SampleProvider distanceMode;
	SampleProvider onTrackMode;
	//SampleProvider leftTouch = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	//SampleProvider rightTouch = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	// SampleProvider angleMode = new EV3GyroSensor(Ports.BRICK.getPort("S2")).getAngleMode();
	
	private boolean onBridge = true;
	private long time = System.nanoTime();
	int defaultSpeed = 200;
	int varSpeed = 200;
	float Kp = 0.0000000015f;	// ..0002f
	// float angle;

	private BridgeFollowerState state;

	public BridgeFollowerV2() {
		this.distanceMode = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		this.onTrackMode = new SimpleUltrasonic(distanceMode);
	}
	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Key escape = Ports.BRICK.getKey("Escape");
		Key enter = Ports.BRICK.getKey("Enter");
		
		Ports.ULTRASONIC_MOTOR.setSpeed(80);
		Ports.ULTRASONIC_MOTOR.rotateTo(-80);
		
		//setState(BridgeFollowerState.DRIVING_STRAIT);
		
		System.out.println("follow bridge");

		Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
		
		while (true) {
			// Handle keys
			if (escape.isDown()) {
				return WalkableStatus.STOP;
			}
			if (enter.isDown()) {
				Ports.ULTRASONIC_MOTOR.setSpeed(80);
				Ports.ULTRASONIC_MOTOR.rotateTo(0);
				RobotUtils.stopMotors();
				Delay.msDelay(2000);
				return WalkableStatus.MENU;
			}
			
			// Handle obstacle
			/*
			 * if (touchLeft() || touchRight()) { System.out.println("Start tunnelfinder");
			 * return new TunnelFinder().start_walking(); }
			 */
			
			if (isOnLine() && !onBridge) {
				onBridge = true;
				time = System.nanoTime();
				System.out.println("bridge");
				// angle = getAngle();
			}
			if (!isOnLine() && onBridge) {
				onBridge = false;
				time = System.nanoTime();
				System.out.println("no bridge");
				// angle = getAngle();
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
	        // System.out.println("left: " + leftSpeed + " right: " + rightSpeed);
	        
	        leftSpeed = Math.max(0, leftSpeed);
	        rightSpeed = Math.max(0, rightSpeed);
	        
	        Ports.LEFT_MOTOR.setSpeed(leftSpeed);
	        Ports.RIGHT_MOTOR.setSpeed(rightSpeed);
	        
	        Ports.LEFT_MOTOR.forward();
	        Ports.RIGHT_MOTOR.forward();
		}
		
		// End - currently not implemented
		// return WalkableStatus.FINISHED;
	}
	
	/**
	 * Checks whether the robot is on the line or not
	 * 
	 * @return
	 */
	public boolean isOnLine() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		return onLine;
	}
	
	/*
	 * public float getAngle() { float[] sample = new float[angleMode.sampleSize()];
	 * angleMode.fetchSample(sample, 0); return sample[0]; }
	 */
	
	public boolean seeingEndRamp() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean endRamp = sample[0] < -0.5f;
		return endRamp;
	}
}
