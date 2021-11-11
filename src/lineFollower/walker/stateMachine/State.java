package lineFollower.walker.stateMachine;

import framework.Ports;
import lejos.robotics.SampleProvider;
import lineFollower.walker.colorSensor.AutoAdjustFilter;

public abstract class State {
    
    protected int DEFAULT_SPEED = 300;
    
    protected int ENCODER_GAP_DISTANCE = 600;
    protected int ENCODER_TURN_100 = 600;
    protected int ENCODER_TURN_45 = 280;
    
    protected SampleProvider autoAdjustRGBFilter;
    
    protected StateName stateName;
    
    public State() {
        SampleProvider rgbMode = Ports.COLOR_SENSOR.getRGBMode();
        autoAdjustRGBFilter = new AutoAdjustFilter(rgbMode);
    }
    
    abstract public StateName handleState() throws ProcessInteruptedEnterException, RobotCollisionException;
    
    protected void logCurrentState() {
        if (stateName != null) {
        System.out.println(this.stateName.toString());
        } else {
            System.out.println("State not defined");
        }
    }
    
    protected void driveForwardStraight(int encoderValue) throws RobotCollisionException, ProcessInteruptedEnterException {
        Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
        int leftTachoCount = 0;
        int rightTachoCount = 0;
        
        Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
        Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        while (leftTachoCount <= encoderValue || rightTachoCount <= encoderValue) {
            if (rightTachoCount >= encoderValue) {
                Ports.RIGHT_MOTOR.stop(true);
            }
            if (leftTachoCount >= encoderValue) {
                Ports.LEFT_MOTOR.stop(true);
            }
            if (Ports.ENTER.isDown()) {
                throw new ProcessInteruptedEnterException("Enter pressed: Walker terminated");
            }
            if (buttonPressed()) {
                throw new RobotCollisionException("Robot detected collision with fron button press");
            }
            leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
            rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
        }
        
        Ports.LEFT_MOTOR.stop(true);
        Ports.RIGHT_MOTOR.stop(true);
    }
    
    protected boolean buttonPressed() {
        float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
        float[] touchRight = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
        
        Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
        Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
        
        return touchLeft[0] == 1 || touchRight[0] == 1;
    }
}
