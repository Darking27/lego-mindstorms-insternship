package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import exceptions.RobotErrorException;
import framework.Ports;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lineFollower.colorSensor.AutoAdjustFilter;
import lineFollower.colorSensor.RGBColorSensor;
import lineFollower.stateMachine.StateName;
import lineFollower.stateMachine.TextRescources;

public abstract class BaseState {
    
    protected int DEFAULT_SPEED = 400;
    
    protected int ENCODER_GAP_DISTANCE = 600;
    protected int ENCODER_TURN_100 = 400;
    protected int ENCODER_TURN_45 = 200;
    
    protected SampleProvider autoAdjustRGBFilter;
    protected RGBColorSensor rgbColorSensor;
    
    protected String stateName;
    
    public BaseState() {
        SampleProvider rgbMode = Ports.COLOR_SENSOR.getRGBMode();
        autoAdjustRGBFilter = AutoAdjustFilter.getInstance(rgbMode);
        rgbColorSensor = RGBColorSensor.getInstance();
        this.stateName = "Initialized";
    }
    
    abstract public StateName handleState()
    		throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException, RobotErrorException;
    
    
    // TODO: Graphics Display logging
    public void logCurrentState() {
        if (stateName != null) {
        System.out.println(this.stateName);
        } else {
            System.out.println("State not defined");
        }
    }
    
    protected boolean driveForwardStraight(int encoderValue, boolean searchLine, boolean checkFinishLine)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	return driveStraight(encoderValue, DEFAULT_SPEED, searchLine,checkFinishLine, true);
    }
    
    protected boolean driveForwardStraight(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine)
            throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
        
        return driveStraight(encoderValue, speed, searchLine,checkFinishLine, true);
    }
    
    protected boolean driveBackwardStraight(int encoderValue, boolean searchLine, boolean checkFinishLine)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	return driveStraight(encoderValue, DEFAULT_SPEED, searchLine,checkFinishLine, false);
    }
    
    protected boolean driveBackwardStraight(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine)
            throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
        
        return driveStraight(encoderValue, speed, searchLine,checkFinishLine, false);
    }
    
    private boolean driveStraight(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine, boolean forward)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
    	
    	Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
        int leftTachoCount = 0;
        int rightTachoCount = 0;
        
        Ports.LEFT_MOTOR.setSpeed(speed);
        Ports.RIGHT_MOTOR.setSpeed(speed);
        
        if (forward) {
	        Ports.LEFT_MOTOR.forward();
	        Ports.RIGHT_MOTOR.forward();
        } else {
        	Ports.LEFT_MOTOR.backward();
	        Ports.RIGHT_MOTOR.backward();
        }
        
        while (Math.abs(rightTachoCount) <= encoderValue || Math.abs(rightTachoCount) <= encoderValue) {
        	autoAdjustRGBFilter.fetchSample(sample, 0);
        	
            if (Math.abs(rightTachoCount) >= encoderValue) {
                Ports.RIGHT_MOTOR.stop(true);
            }
            if (Math.abs(leftTachoCount) >= encoderValue) {
                Ports.LEFT_MOTOR.stop(true);
            }
            checkRobotInputs(sample, checkFinishLine);
            
            double gray = AutoAdjustFilter.getGrayValue(sample);
            if (searchLine && lineFound(gray)) {

                System.out.println(gray);
            	return true;
            }
            
            leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
            rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
        }
        
        Ports.LEFT_MOTOR.stop(true);
        Ports.RIGHT_MOTOR.stop();
        
        return false;
    }
    
    protected boolean turnRight(int encoderValue, boolean searchLine, boolean checkFinishLine)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	return turn(encoderValue, DEFAULT_SPEED, searchLine,checkFinishLine, true);
    }
    
    protected boolean turnLeft(int encoderValue, boolean searchLine, boolean checkFinishLine)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	return turn(encoderValue, DEFAULT_SPEED, searchLine, checkFinishLine, false);
    }
    
    protected boolean turnRight(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine)
            throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
        
        return turn(encoderValue, speed, searchLine,checkFinishLine, true);
    }
    
    protected boolean turnLeft(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine)
            throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
        
        return turn(encoderValue, speed, searchLine, checkFinishLine, false);
    }
    
    private boolean turn(int encoderValue, int speed, boolean searchLine, boolean checkFinishLine, boolean right)
    		throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	
    	float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
        
        Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
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
        
        Ports.LEFT_MOTOR.setSpeed(speed);
        Ports.RIGHT_MOTOR.setSpeed(speed);
        
        m1.forward();
        m2.backward();
        
        while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
        	autoAdjustRGBFilter.fetchSample(sample, 0);
        	
        	if (m1TachoCount >= encoderValue) {
                m1.stop(true);
            }
            if (m2TachoCount >= encoderValue) {
                m2.stop(true);
            }
            
            checkRobotInputs(sample, checkFinishLine);
            
            double gray = AutoAdjustFilter.getGrayValue(sample);
            if (searchLine && lineFound(gray)) {
                return true;
            }
            m1TachoCount = Math.abs(m1.getTachoCount());
            m2TachoCount = Math.abs(m2.getTachoCount());
        }
        
        m1.stop(true);
        m2.stop();
        
    	return false;
    }
    
    protected void checkRobotInputs(float[] sample, boolean checkFinishLine)
    		throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
    	
    	if (buttonPressed()) {
    	    Ports.LEFT_MOTOR.stop(true);
            Ports.RIGHT_MOTOR.stop(true);
            throw new RobotCollisionException(TextRescources.COLLOSION_EXCEPTION_TEXT.getText());
        }
    	if (sample != null) {
	        if (checkFinishLine && isFinishLine(sample)) {
	            Ports.LEFT_MOTOR.stop(true);
	            Ports.RIGHT_MOTOR.stop(true);
	            System.out.println("Found line");
	            throw new FinishLineException(TextRescources.FINISH_LINE_EXCEPTION.getText());
	        }
    	}
        if (enterPressed()) {
            Ports.LEFT_MOTOR.stop(true);
            Ports.RIGHT_MOTOR.stop(true);
        	throw new ProcessInteruptedEnterException(TextRescources.ENTER_EXCEPTION.getText());
        }
    }
    
    protected boolean buttonPressed() {
        float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
        float[] touchRight = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
        
        Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
        Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
        
        return touchLeft[0] == 1 || touchRight[0] == 1;
    }
    
    protected boolean isFinishLine(float[] sample) {
		return rgbColorSensor.isFinishLine();
	}
    
    protected boolean lineFound(double grayValue) {
    	return grayValue > 0.7;
    }
    
    protected boolean enterPressed() {
    	return Ports.ENTER.isDown();
    }
}
