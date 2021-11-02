package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class LineFollower {
    
    ColorID LINE_COLOR = ColorID.WHITE;
    
    Brick brick;
    ColorSensor colorSensor;

    RegulatedMotor leftMotor;
    RegulatedMotor rightMotor;
    
    public LineFollower(Brick brick, String colorSensorPort, String leftMotorPort, String rightMotorPort) {
        this.brick = brick;

        this.colorSensor = new ColorSensor(brick, colorSensorPort);
        
        this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
        this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
    }
    
    public void followLine() {
        LineFollowerState state = LineFollowerState.LINE_LOST;
        colorSensor.start();
        //Drive 10cm forward
        
        while (!state.equals(LineFollowerState.DONE)) {
            if (state.equals(LineFollowerState.LINE_LOST)) {
                while (state.equals(LineFollowerState.LINE_LOST)) {
                    
                }
            }
        }
    }
    
    
    //Motor functions
    
    int ROTATE_100_DEG_TIME = 5;        // time it takes the robot to turn 100 degrees (in seconds) 
    
    int SAMPLE_SPEED = 50;              // in ms
    
    public boolean checkForLine(ColorSensor colorSensor, int degree, boolean right) {
        int count = ROTATE_100_DEG_TIME * 1000 / SAMPLE_SPEED;
        
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
}
