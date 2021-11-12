package lineFollower.walker.stateMachine.states;

import framework.Ports;
import lineFollower.walker.colorSensor.AutoAdjustFilter;
import lineFollower.walker.stateMachine.FinishLineException;
import lineFollower.walker.stateMachine.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.RobotCollisionException;
import lineFollower.walker.stateMachine.StateName;

public class FollowLine extends BaseState {
	
	public FollowLine() {
		super();
		this.stateName = StateName.FOLLOW_LINE;
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
		slowDownRegulator();
		return StateName.SEARCH_LINE;
	}
	
	/**
	 * Runs until line lost or exception occurred
	 * @throws ProcessInteruptedEnterException
	 * @throws RobotCollisionException
	 * @throws FinishLineException
	 */
	private void slowDownRegulator()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
		double Kp = 1500;
		
		double targetValue = 0.5;
        double defaultSpeed = 300;
        
        float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
        autoAdjustRGBFilter.fetchSample(sample, 0);
        
        double currentValue = AutoAdjustFilter.getGrayValue(sample);;
        int leftSpeed;
        int rightSpeed;
        
        Ports.LEFT_MOTOR.setSpeed((int) defaultSpeed);
        Ports.RIGHT_MOTOR.setSpeed((int) defaultSpeed);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        
        while (!lineLost(currentValue)) {
        	checkRobotInputs(sample);
        	
        	double diff = currentValue - targetValue;
            if (diff >= 0) {
                leftSpeed = (int) (defaultSpeed - Math.abs(diff * Kp));
                rightSpeed = (int) (defaultSpeed);
            } else {
                leftSpeed = (int) (defaultSpeed);
                rightSpeed = (int) (defaultSpeed - Math.abs(diff * Kp));
            }
            
            if (leftSpeed < 0) {
                rightSpeed = rightSpeed / 2;
            }
            if (rightSpeed < 0) {
                leftSpeed = leftSpeed / 2;
            }
            
            Ports.LEFT_MOTOR.setSpeed(Math.abs(leftSpeed));
            Ports.RIGHT_MOTOR.setSpeed(Math.abs(rightSpeed));
            
            if (leftSpeed >= 0) {
                Ports.LEFT_MOTOR.forward();
            } else {
                Ports.LEFT_MOTOR.backward();
            }
            if (rightSpeed >= 0) {
                Ports.RIGHT_MOTOR.forward();
            } else {
                Ports.RIGHT_MOTOR.backward();
            }
        	
        	autoAdjustRGBFilter.fetchSample(sample, 0);
        	currentValue = AutoAdjustFilter.getGrayValue(sample);
        }
        
        Ports.RIGHT_MOTOR.rotate(50);
	}
	
	@SuppressWarnings("unused")
	private void normalRegulator()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
		double KpLeft = 600;			//To turn away from the line (should be greater than KpRight)
		double KpRight = 400;
		
		double targetValue = 0.4;
		double defaultSpeed = 300;
		int speedCap = 400;
		
		float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
		autoAdjustRGBFilter.fetchSample(sample, 0);
		
		double currentValue = AutoAdjustFilter.getGrayValue(sample);
	    int leftSpeed;
	    int rightSpeed;
		
		Ports.LEFT_MOTOR.setSpeed((int) defaultSpeed);
		Ports.RIGHT_MOTOR.setSpeed((int) defaultSpeed);
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		while (!lineLost(currentValue)) {
        	checkRobotInputs(sample);
        	
        	double diff = currentValue - targetValue;
            if (diff >= 0) {
	            leftSpeed = (int) (defaultSpeed - Math.abs(diff * KpLeft));
	            rightSpeed = (int) (defaultSpeed + Math.abs(diff * KpLeft));
            } else {
            	leftSpeed = (int) (defaultSpeed + Math.abs(diff * KpRight));
	            rightSpeed = (int) (defaultSpeed - Math.abs(diff * KpRight));
            }
            
            leftSpeed = limitSpeed(leftSpeed, speedCap);
            rightSpeed = limitSpeed(rightSpeed, speedCap);
            
            Ports.LEFT_MOTOR.setSpeed(Math.abs(leftSpeed));
            Ports.RIGHT_MOTOR.setSpeed(Math.abs(rightSpeed));
            
            if (leftSpeed >= 0) {
            	Ports.LEFT_MOTOR.forward();
            } else {
            	Ports.LEFT_MOTOR.backward();
            }
            if (rightSpeed >= 0) {
            	Ports.RIGHT_MOTOR.forward();
            } else {
            	Ports.RIGHT_MOTOR.backward();
            }
        	
        	autoAdjustRGBFilter.fetchSample(sample, 0);
        	currentValue = AutoAdjustFilter.getGrayValue(sample);
		}
		
		Ports.RIGHT_MOTOR.rotate(50);
	}
	
	private int limitSpeed(int targetSpeed, int speedCap) {
		if (Math.abs(targetSpeed) > speedCap) {
			if (targetSpeed >= 0) {
				return speedCap;
			} else {
				return -speedCap;
			}
		}
		return targetSpeed;
	}
	
	/**
	 * Checks if robot lost the line
	 * TODO maybe change decision parameter
	 * @param grayValue
	 * @return
	 */
	private boolean lineLost(double grayValue) {
		return grayValue < 0.1;
	}
}
