package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import framework.Ports;
import lineFollower.colorSensor.AutoAdjustFilter;
import lineFollower.stateMachine.StateName;

public class FollowLineState extends BaseState {
	
	public FollowLineState() {
		super();
		this.stateName = "Follow line";
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
		
		double Kp = 950;  //900
		double Kd = 500;  //700
		
		double targetValue = 0.5;
        double defaultSpeed = 400;  //400
        
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
        	checkRobotInputs(sample, true);
        	
        	double diff = currentValue - targetValue;
        	double speed = defaultSpeed - Math.abs(diff * Kd);
            if (diff >= 0) {
                leftSpeed = (int) (speed - Math.abs(diff * Kp));
                rightSpeed = (int) (speed);
            } else {
                leftSpeed = (int) (speed);
                rightSpeed = (int) (speed - Math.abs(diff * Kp));
            }
            
//            if (leftSpeed < 0) {
//                rightSpeed = rightSpeed / 2;
//            }
//            if (rightSpeed < 0) {
//                leftSpeed = leftSpeed / 2;
//            }
            
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
        	checkRobotInputs(sample, true);
        	
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
		return grayValue < 0.15;
	}
}
