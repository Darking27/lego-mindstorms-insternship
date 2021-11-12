package lineFollower.walker.stateMachine.states;

import framework.Ports;
import lejos.robotics.RegulatedMotor;
import lineFollower.walker.stateMachine.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.RobotCollisionException;
import lineFollower.walker.stateMachine.StateName;

public class Start extends BaseState {
    
    Start() {
        super();
        this.stateName = StateName.START;
    }

    @Override
    public StateName handleState() throws ProcessInteruptedEnterException, RobotCollisionException {
        
        logCurrentState();
        
        driveForwardStraight(ENCODER_GAP_DISTANCE);
        
        calibrateFilter(ENCODER_TURN_45, true);
        calibrateFilter(2 * ENCODER_TURN_45, false);
        calibrateFilter(ENCODER_TURN_45, true);
        
        return StateName.SEARCH_LINE;
    }
    
    private void calibrateFilter(int encoderValue, boolean rightTurn) throws ProcessInteruptedEnterException {
        float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
        
        Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
        RegulatedMotor m1;
        RegulatedMotor m2;
        
        if (rightTurn) {
            m1 = Ports.LEFT_MOTOR;
            m2 = Ports.RIGHT_MOTOR;
        } else {
            m1 = Ports.RIGHT_MOTOR;
            m2 = Ports.LEFT_MOTOR;
        }
        
        int m1TachoCount = 0;
        int m2TachoCount = 0;
        
        Ports.LEFT_MOTOR.setSpeed(400);
        Ports.RIGHT_MOTOR.setSpeed(400);
        
        m1.forward();
        m2.backward();
        
        while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
            if (m1TachoCount >= encoderValue) {
                m1.stop(true);
            }
            if (m2TachoCount >= encoderValue) {
                m2.stop(true);
            }
            if (Ports.ENTER.isDown()) {
                throw new ProcessInteruptedEnterException("Enter pressed: Walker terminated");
            }
            m1TachoCount = Math.abs(m1.getTachoCount());
            m2TachoCount = Math.abs(m2.getTachoCount());
            
            autoAdjustRGBFilter.fetchSample(sample, 0);
        }
        
        m1.stop(true);
        m2.stop(true);
    }
}
