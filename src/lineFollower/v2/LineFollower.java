package lineFollower.v2;

import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class LineFollower {
	
	 int SAMPLE_SPEED = 50;            	// in ms
	 int ON_LINE_TIMEOUT = 150;			// in ms
	 int ON_BACKGROUND_TIMEOUT = 250;	// in ms
    
    ColorID LINE_COLOR = ColorID.WHITE;
    ColorID BACKGROUND_COLOR = ColorID.BLACK;
    ColorID FINISH_COLOR = ColorID.BLUE;
    
    Brick brick;
    ColorSensor colorSensor;

    RegulatedMotor leftMotor;
    RegulatedMotor rightMotor;
    
    LineFollowerState state;
    
    public LineFollower(Brick brick, String colorSensorPort, String leftMotorPort, String rightMotorPort) {
        this.brick = brick;

        this.colorSensor = new ColorSensor(brick, colorSensorPort);
        
        this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
        this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
        
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
	        		break;
	        	case DONE:
	        		logCurrentState();
	        		break;
        	}
        }
    }
    
    private void logCurrentState() {
    	System.out.println(this.state.toString());
    }
    
    private void handleDriveForwardGap() {
    	if (driveForward(this.colorSensor, 10)) {
    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
    	} else {
    		this.state = LineFollowerState.SEARCH_LINE_RIGHT;
    	}
    }
    
    private void handleSearchLineRight() {
    	if (checkForLine(colorSensor, true)) {
    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
    	} else {
    		this.state = LineFollowerState.SEARCH_LINE_LEFT;
    	}
    }
    
    private void handleSearchLineLeft() {
    	if (checkForLine(colorSensor, false)) {
    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
    	}
    	if (checkForLine(colorSensor, false)) {
    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
    	} else {
    		this.state = LineFollowerState.DRIVE_FORWARD_GAP;
    		checkForLine(colorSensor, true);
    	}
    }
    
    private void handleSearchBackgroundLeft() {
    	ColorID color = this.colorSensor.getColorId();
    	
    	this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
    	this.rightMotor.forward();
    	this.leftMotor.backward();
    	
    	while (!color.equals(BACKGROUND_COLOR)) {
            Delay.msDelay(SAMPLE_SPEED);
            color = colorSensor.getColorId();
    	}
    	
    	this.rightMotor.stop();
    	this.leftMotor.stop();
    	
    	this.state = LineFollowerState.DRIVE_FORWARD_RIGHT_SLOW;
    }
    
    private void handleDriveForwardLeftSlow() {
    	ColorID color;
    	boolean backgoundFound = false;
    	
    	this.rightMotor.setSpeed(300);
    	this.rightMotor.forward();
    	this.leftMotor.stop();
    	
    	int count = ON_LINE_TIMEOUT / SAMPLE_SPEED;
    	
    	for (int i = 0; i < count; i++) {
    		color = this.colorSensor.getColorId();
    		if (color.equals(BACKGROUND_COLOR)) {
    			backgoundFound = true;
    			break;
    		}
    	}
    	
    	this.rightMotor.stop();
    	
    	if (backgoundFound) {
    		this.state = LineFollowerState.DRIVE_FORWARD_RIGHT_SLOW;
    	} else {
    		this.state = LineFollowerState.SEARCH_BACKGROUND_LEFT;
    	}
    }
    
    private void handleDriveForwardRightSlow() {
    	ColorID color;
    	boolean lineFound = false;
    	
    	this.leftMotor.setSpeed(300);
    	this.leftMotor.forward();
    	this.rightMotor.stop();
    	
    	int count = ON_BACKGROUND_TIMEOUT / SAMPLE_SPEED;
    	
    	for (int i = 0; i < count; i++) {
    		color = this.colorSensor.getColorId();
    		if (color.equals(LINE_COLOR)) {
    			lineFound = true;
    			break;
    		}
    	}
    	
    	this.leftMotor.stop();
    	
    	if (lineFound) {
    		this.state = LineFollowerState.DRIVE_FORWARD_LEFT_SLOW;
    	} else {
    		this.state = LineFollowerState.SEARCH_LINE_RIGHT;
    	}
    }
    
    
    //Motor functions
    
    //TODO meassure
    int ROTATE_100_DEG_TIME = 2000;        // time it takes the robot to turn 100 degrees (in ms)
    int DRIVE_1CM_TIME = 500;		// time it takes the robot to drive 1 cm (in ms)
    
    public boolean checkForLine(ColorSensor colorSensor, boolean right) {
        int count = ROTATE_100_DEG_TIME / SAMPLE_SPEED;
        
        RegulatedMotor m1;
        RegulatedMotor m2;
        
        if (right) {
            m1 = this.rightMotor;
            m2 = this.leftMotor;
        } else {
            m1 = this.leftMotor;
            m2 = this.rightMotor;
        }
        
        m1.setSpeed(300);
        m2.setSpeed(300);
        
        m1.backward();
        m2.forward();
        
        for (int i = 0; i < count; i++) {
            ColorID color = colorSensor.getColorId();
            if (color.equals(LINE_COLOR)) {
                m1.stop();
                m2.stop();
                return true;
            }
            Delay.msDelay(SAMPLE_SPEED);
        }
        m1.stop();
        m2.stop();
        
        return false;
    }
    
    public boolean driveForward(ColorSensor colorSensor, int distance) {
    	int count = DRIVE_1CM_TIME / SAMPLE_SPEED;
    	
    	this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
    	this.rightMotor.forward();
    	this.leftMotor.forward();
    	
    	for (int i = 0; i < count; i++) {
            ColorID color = colorSensor.getColorId();
            if (color.equals(LINE_COLOR)) {
                this.leftMotor.stop();
                this.rightMotor.stop();
                return true;
            }
            Delay.msDelay(SAMPLE_SPEED);
        }
    	
    	this.leftMotor.stop();
        this.rightMotor.stop();
    	
    	return false;
    }
}