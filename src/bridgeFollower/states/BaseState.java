package bridgeFollower.states;

import bridgeFollower.SimpleUltrasonic;
import exceptions.FinishLineException;
import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import exceptions.StopException;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lineFollower.colorSensor.AutoAdjustFilter;

public abstract class BaseState {
	protected SampleProvider distanceMode = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	protected SampleProvider onTrackMode = new SimpleUltrasonic(distanceMode);
	protected SampleProvider leftTouch = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	protected SampleProvider rightTouch = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
	protected SampleProvider colorId = Ports.COLOR_SENSOR.getColorIDMode();
	protected Key escape = Ports.BRICK.getKey("Escape");
	protected Key enter = Ports.BRICK.getKey("Enter");
	
	/**
	 * Run until the state should be switched
	 * @return next state
	 */
	public abstract State handleState() throws KeyPressedException;
	
	protected void driveStraight(int encoderValue, boolean forward, int speed)
    		throws KeyPressedException {
		driveStraight(encoderValue, forward, speed, false);
	}
	
	protected void driveStraight(int encoderValue, boolean forward, int speed, boolean dontStop)
    		throws KeyPressedException {    	
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
            if (Math.abs(rightTachoCount) >= encoderValue) {
                Ports.RIGHT_MOTOR.stop(true);
            }
            if (Math.abs(leftTachoCount) >= encoderValue) {
                Ports.LEFT_MOTOR.stop(true);
            }
            
            this.handleKeyPressed();
            
            leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
            rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
        }
        
        if (dontStop) {
        	Ports.LEFT_MOTOR.flt(true);
        	Ports.RIGHT_MOTOR.flt();
        } else {
	        Ports.LEFT_MOTOR.stop(true);
	        Ports.RIGHT_MOTOR.stop();
        }
    }
	
    protected void turn(int encoderValue, boolean right, int speed)
    		throws KeyPressedException {        
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
        	if (m1TachoCount >= encoderValue) {
                m1.stop(true);
            }
            if (m2TachoCount >= encoderValue) {
                m2.stop(true);
            }
            
            handleKeyPressed();
            
            m1TachoCount = Math.abs(m1.getTachoCount());
            m2TachoCount = Math.abs(m2.getTachoCount());
        }
        
        m1.stop(true);
        m2.stop();
    }
	
	protected void handleKeyPressed() throws KeyPressedException {
		if (escape.isDown()) {
			throw new StopException();
		}
		if (enter.isDown()) {
			Ports.ULTRASONIC_MOTOR.setSpeed(80);
			Ports.ULTRASONIC_MOTOR.rotateTo(0);
			RobotUtils.stopMotors();
			throw new MenuException();
		}
	}
	
	protected boolean isOverBridge() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean onLine = sample[0] < 0.5f;
		return onLine;
	}
	
	protected boolean seeingEndRamp() {
		int sampleSize = 1;
		float[] sample = new float[sampleSize];
		onTrackMode.fetchSample(sample, 0);
		boolean endRamp = sample[0] < -0.5f;
		return endRamp;
	}
	
	protected boolean touchLeft() {
		float[] sample = new float[1];
		leftTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
	
	protected boolean touchRight() {
		float[] sample = new float[1];
		rightTouch.fetchSample(sample, 0);
		return sample[0] > .5f;
	}
	
	protected boolean seeingEdge() {
		float[] sample = new float[colorId.sampleSize()];
		colorId.fetchSample(sample, 0);
		if (sample[0] == -1f) {
			System.out.println("-1");
		}
		return sample[0] == -1f;
	}
	
	protected boolean seeingLightBrown() {
		float[] sample = new float[colorId.sampleSize()];
		colorId.fetchSample(sample, 0);
		if (sample[0] == -1f) {
			System.out.println("-1");
		}
		return sample[0] == -1f;
	}
}
