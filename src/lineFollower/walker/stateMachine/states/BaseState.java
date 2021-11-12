package lineFollower.walker.stateMachine.states;

import framework.Ports;
import lejos.robotics.SampleProvider;
import lineFollower.walker.colorSensor.AutoAdjustFilter;
import lineFollower.walker.stateMachine.FinishLineException;
import lineFollower.walker.stateMachine.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.RobotCollisionException;
import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.TextRescources;

public abstract class BaseState {
    
    protected int DEFAULT_SPEED = 300;
    
    protected int ENCODER_GAP_DISTANCE = 600;
    protected int ENCODER_TURN_100 = 600;
    protected int ENCODER_TURN_45 = 280;
    
    protected SampleProvider autoAdjustRGBFilter;
    
    protected StateName stateName;
    
    public BaseState() {
        SampleProvider rgbMode = Ports.COLOR_SENSOR.getRGBMode();
        autoAdjustRGBFilter = new AutoAdjustFilter(rgbMode);
    }
    
    abstract public StateName handleState() throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException;
    
    
    // TODO: Graphics Display logging
    protected void logCurrentState() {
        if (stateName != null) {
        System.out.println(this.stateName.toString());
        } else {
            System.out.println("State not defined");
        }
    }
    
    protected void driveForwardStraight(int encoderValue) throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
    	float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
    	
    	Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
        int leftTachoCount = 0;
        int rightTachoCount = 0;
        
        Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
        Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        while (leftTachoCount <= encoderValue || rightTachoCount <= encoderValue) {
        	autoAdjustRGBFilter.fetchSample(sample, 0);
        	
            if (rightTachoCount >= encoderValue) {
                Ports.RIGHT_MOTOR.stop(true);
            }
            if (leftTachoCount >= encoderValue) {
                Ports.LEFT_MOTOR.stop(true);
            }
            checkRobotInputs(sample);
            
            leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
            rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
        }
        
        Ports.LEFT_MOTOR.stop(true);
        Ports.RIGHT_MOTOR.stop(true);
    }
    
    protected void checkRobotInputs(float[] sample) throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
    	if (buttonPressed()) {
            throw new RobotCollisionException(TextRescources.COLLOSION_EXCEPTION_TEXT.getText());
        }
    	if (sample != null) {
	        if (isFinishLine(sample)) {
	            throw new FinishLineException(TextRescources.FINISH_LINE_EXCEPTION.getText());
	        }
    	}
        if (enterPressed()) {
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
		return (sample[0] * 3 < sample [2]) && (sample[1] * 4 < sample[2]);
	}
    
    protected boolean lineFound(double grayValue) {
    	return grayValue > 0.7;
    }
    
    protected boolean enterPressed() {
    	return Ports.ENTER.isDown();
    }
}
