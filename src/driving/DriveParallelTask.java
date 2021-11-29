package driving;

import framework.Ports;

public class DriveParallelTask implements Task {
	
	private final int defaultSpeed;
	private boolean firstStart;
	
	float Kp = 1;
	
	public DriveParallelTask(int speed) {
		this.defaultSpeed = speed;
		firstStart = true;
		Ports.LEFT_MOTOR.setSpeed((int) defaultSpeed);
        Ports.RIGHT_MOTOR.setSpeed((int) defaultSpeed);
	}

	@Override
	public boolean run() {
		if (firstStart) {
			firstStart = false;
	        Ports.LEFT_MOTOR.resetTachoCount();
	        Ports.RIGHT_MOTOR.resetTachoCount();
	        Ports.LEFT_MOTOR.forward();
	        Ports.RIGHT_MOTOR.forward();
		}
		
		int leftSpeed;
        int rightSpeed;
    	
    	float diff = tachoDiff();
    	leftSpeed = (int) (defaultSpeed - diff * Kp);
        rightSpeed = (int) (defaultSpeed + diff * Kp);
        
        Ports.LEFT_MOTOR.setSpeed(Math.abs(leftSpeed));
        Ports.RIGHT_MOTOR.setSpeed(Math.abs(rightSpeed));
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
       
		return false;
	}
	
	private float tachoDiff() {
		float leftTacho = Ports.LEFT_MOTOR.getTachoCount();
		float rightTacho = 0.98f * Ports.RIGHT_MOTOR.getTachoCount();
		return leftTacho - rightTacho;
	}

	@Override
	public void cancel() {
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(false);

	}

}
