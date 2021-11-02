package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class LineFollower {
	
	 int SAMPLE_SPEED = 50;              // in ms
    
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
        
        state = LineFollowerState.DRIVE_FORWARD_GAP;
    }
    
    public void followLine() {
        
    }
    
    
    //Motor functions
    
    int ROTATE_100_DEG_TIME = 2000;        // time it takes the robot to turn 100 degrees (in ms) 
    
    public boolean checkForLine(ColorSensor colorSensor, int degree, boolean right) {
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
    
    int DRIVE_1CM_TIME = 2000;		// time it takes the robot to drive 1 cm (in ms)
    
    public boolean driveForward(ColorSensor colorSensor, int distance) {
    	int count = DRIVE_1CM_TIME / SAMPLE_SPEED;
    	
    	this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
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