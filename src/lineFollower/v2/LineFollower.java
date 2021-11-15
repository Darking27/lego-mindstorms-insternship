package lineFollower.v2;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LineFollower {
	
	private enum SensorState {
		BACKGROUND, LINE, OBSTACLE, FINISH_LINE, NONE
	}
	
	int SAMPLE_SPEED = 2;            	// in ms
	int ON_LINE_TIMEOUT = 75;			// in ms
	int ON_BACKGROUND_TIMEOUT = 200;	// in ms
    
    ColorID LINE_COLOR = ColorID.WHITE;
    ColorID BACKGROUND_COLOR = ColorID.BLACK;
    ColorID FINISH_COLOR = ColorID.BLUE;
    
    Brick brick;
    ColorSensor colorSensor;
    SampleProvider touchSensorLeft;
    SampleProvider touchSensorRight;

    RegulatedMotor leftMotor;
    RegulatedMotor rightMotor;
    
    LineFollowerState state;
    
    Key escape;
    
    public LineFollower(Brick brick, String colorSensorPort, String leftMotorPort, String rightMotorPort, String touchSensorRightPort, String touchSensorLeftPort) {
        this.brick = brick;

        this.colorSensor = new ColorSensor(brick, colorSensorPort);
        this.colorSensor.start();
        
        this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
        this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
        
        this.touchSensorLeft = new EV3TouchSensor(brick.getPort(touchSensorLeftPort));
        this.touchSensorRight = new EV3TouchSensor(brick.getPort(touchSensorRightPort));
        
        this.escape = brick.getKey("Escape");
        
        this.state = LineFollowerState.DRIVE_FORWARD_GAP;
    }
    
    public void followLine() {
        while (!this.state.equals(LineFollowerState.DONE)) {
        	switch (this.state) {
	        	case DRIVE_FORWARD_GAP:
	        		logCurrentState();
	        		handleDriveForwardGap();
	        		break;
	        	case DRIVE_FORWARD_LEFT_SLOW:
	        		logCurrentState();
	        		handleDriveForwardLeftSlow();
	        		break;
	        	case DRIVE_FORWARD_RIGHT_SLOW:
	        		logCurrentState();
	        		handleDriveForwardRightSlow();
	        		break;
	        	case SEARCH_LINE_RIGHT:
	        		logCurrentState();
	        		handleSearchLineRight();
	        		break;
	        	case SEARCH_LINE_LEFT:
	        		logCurrentState();
	        		handleSearchLineLeft();
	        		break;
	        	case SEARCH_BACKGROUND_LEFT:
	        		logCurrentState();
	        		handleSearchBackgroundLeft();
	        		break;
	        	case DRIVE_AROUND_OBSTACLE:
	        		logCurrentState();
	        		robotDelay(1000, LINE_COLOR);
	        		break;
	        	case DONE:
	        		logCurrentState();
	        		Delay.msDelay(5000);
	        		return;
	        	case ERROR:
	        		logCurrentState();
	        		Delay.msDelay(5000);
	        		return;
        	}
        }
    }
    
    private SensorState robotDelay(int duration, ColorID toCheck) {
    	int count = duration / SAMPLE_SPEED;
    	
    	for (int i = 0; i < count; i++) {
    		if (touchSensorPressed(this.touchSensorLeft) || touchSensorPressed(this.touchSensorRight)) {
    			this.state = LineFollowerState.DRIVE_AROUND_OBSTACLE;
    			return SensorState.OBSTACLE;
    		}
    		if (this.colorSensor.getColorId().equals(FINISH_COLOR)) {
    			this.state = LineFollowerState.DONE;
    			return SensorState.FINISH_LINE;
    		}
    		if (this.colorSensor.getColorId().equals(toCheck)) {
    			if (toCheck.equals(BACKGROUND_COLOR)) {
    				return SensorState.BACKGROUND;
    			} else if (toCheck.equals(LINE_COLOR)) {
    				return SensorState.LINE;
    			}
    		}
    		if (escape.isDown()) {
    		    System.exit(0);
    		}
    		Delay.msDelay(SAMPLE_SPEED);
    	}
    	return SensorState.NONE;
    }
    
    private boolean touchSensorPressed(SampleProvider sampleProvider) {
    	float[] sample = new float[sampleProvider.sampleSize()];
    	
    	sampleProvider.fetchSample(sample, 0);
    	
    	return sample[0] == 1;
    }
    
    private void logCurrentState() {
    	System.out.println(this.state.toString());
    }
    
    private void handleDriveForwardGap() {
    	if (!driveForward(10)) {
    		this.state = LineFollowerState.SEARCH_LINE_RIGHT;
    	}
    }
    
    private void handleSearchLineRight() {
    	if (!checkForLine(true)) {
    		this.state = LineFollowerState.SEARCH_LINE_LEFT;
    	}
    }
    
    private void handleSearchLineLeft() {
    	if (checkForLine(false)) {
    		return;
    	}
    	if (!checkForLine(false)) {
    		this.state = LineFollowerState.DRIVE_FORWARD_GAP;
    		checkForLine(true);
    	}
    }
    
    private void handleSearchBackgroundLeft() {
    	this.leftMotor.setSpeed(150);
    	this.rightMotor.setSpeed(150);
    	
    	this.rightMotor.forward();
    	this.leftMotor.backward();
    	
    	SensorState sensorState = robotDelay(10000, BACKGROUND_COLOR);
    	
    	stopMotor();
    	
    	switch (sensorState) {
	    	case OBSTACLE:
	    		break;
	    	case FINISH_LINE:
	    		break;
	    	case BACKGROUND:
	    		this.state = LineFollowerState.DRIVE_FORWARD_RIGHT_SLOW;
	    		break;
    		default:
    			this.state = LineFollowerState.ERROR;
    			break;
    	}
    }
    
    private void handleDriveForwardLeftSlow() {
    	this.rightMotor.setSpeed(300);
    	this.rightMotor.forward();
    	this.leftMotor.stop();
    	
    	SensorState sensorState = robotDelay(ON_LINE_TIMEOUT, BACKGROUND_COLOR);
    	
    	stopMotor();
    	
    	switch (sensorState) {
	    	case OBSTACLE:
	    		break;
	    	case FINISH_LINE:
	    		break;
	    	case BACKGROUND:
	    		this.state = LineFollowerState.DRIVE_FORWARD_RIGHT_SLOW;
	    		break;
    		default:
    			this.state = LineFollowerState.SEARCH_BACKGROUND_LEFT;
    			break;
    	}
    }
    
    private void handleDriveForwardRightSlow() {
    	this.leftMotor.setSpeed(300);
    	this.leftMotor.forward();
    	this.rightMotor.stop();
    	
    	SensorState sensorState = robotDelay(ON_BACKGROUND_TIMEOUT, LINE_COLOR);
    	
    	stopMotor();
    	
    	switch (sensorState) {
	    	case OBSTACLE:
	    		break;
	    	case FINISH_LINE:
	    		break;
	    	case LINE:
	    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
	    		break;
    		default:
    			this.state = LineFollowerState.SEARCH_LINE_RIGHT;
    			break;
    	}
    }
    
    
    //Motor functions
    
    //TODO meassure
    int ROTATE_100_DEG_TIME = 1500;        	// time it takes the robot to turn 100 degrees (in ms)
    int DRIVE_1CM_TIME = 2500;				// time it takes the robot to drive 1 cm (in ms)
    
    public boolean checkForLine(boolean right) {
        
        RegulatedMotor m1;
        RegulatedMotor m2;
        
        if (right) {
            m1 = this.rightMotor;
            m2 = this.leftMotor;
        } else {
            m1 = this.leftMotor;
            m2 = this.rightMotor;
        }
        
        m1.setSpeed(120);
        m2.setSpeed(120);
        
        m1.backward();
        m2.forward();
        
        SensorState sensorState = robotDelay(ROTATE_100_DEG_TIME, LINE_COLOR);
    	
    	stopMotor();
    	
    	switch (sensorState) {
	    	case OBSTACLE:
	    		return true;
	    	case FINISH_LINE:
	    		return true;
	    	case LINE:
	    		this.state = LineFollowerState.SEARCH_BACKGROUND_LEFT;
	    		return true;
    		default:
    			return false;
    	}
    }
    
    public boolean driveForward(int distance) {
    	this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
    	this.rightMotor.forward();
    	this.leftMotor.forward();
    	
        SensorState sensorState = robotDelay(DRIVE_1CM_TIME, LINE_COLOR);
    	
    	stopMotor();
    	
    	switch (sensorState) {
	    	case OBSTACLE:
	    		return true;
	    	case FINISH_LINE:
	    		return true;
	    	case LINE:
	    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
	    		return true;
    		default:
    			return false;
    	}
    }
    
    public void stopMotor() {
    	this.rightMotor.stop(true);
    	this.leftMotor.stop(true);
    }
}