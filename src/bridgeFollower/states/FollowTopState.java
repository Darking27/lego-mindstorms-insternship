package bridgeFollower.states;

import exceptions.KeyPressedException;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.utility.Delay;

public class FollowTopState extends FindLeftState {
	private boolean onBridge = true;
	private long time = System.nanoTime();
	int defaultSpeed = 300;
	int varSpeed = 250;
	float Kp = 0.0000000010f;
	public static final int DISTANCE = 2300;

	@Override
	public State handleState() throws KeyPressedException {
		System.out.println("Follow top");
		
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		
		// First find left side of bridge
		super.handleState();
		
		System.out.println("Found line");
		time = System.nanoTime();
		
		while (true) {
			handleKeyPressed();
			
			if (Ports.LEFT_MOTOR.getTachoCount() > DISTANCE
					|| Ports.RIGHT_MOTOR.getTachoCount() > DISTANCE) {
				RobotUtils.stopMotors();
				System.out.println("Geradeaus");
				return State.DRIVE_STRAIT_TOP;
			}
			
			if (isOverBridge() && !onBridge) {
				onBridge = true;
				time = System.nanoTime();
				//System.out.println("bridge");
			}
			if (!isOverBridge() && onBridge) {
				onBridge = false;
				time = System.nanoTime();
				//System.out.println("no bridge");
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
	}

}
