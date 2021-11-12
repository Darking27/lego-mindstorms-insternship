package lineFollower.walker.stateMachine.states;

import framework.Ports;
import lejos.robotics.RegulatedMotor;
import lineFollower.walker.colorSensor.AutoAdjustFilter;
import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;

public class SearchLine extends BaseState {

    public SearchLine() {
        super();
        this.stateName = StateName.SEARCH_LINE;
    }
    
    @Override
    public StateName handleState() throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
        if (searchLine(ENCODER_TURN_100, true)) {
            return StateName.FOLLOW_LINE;
        }
        // Maybe needs to search background first before follow line state
        if (searchLine(2 * ENCODER_TURN_100, false)) {
            return StateName.FOLLOW_LINE;
        }
        if (searchLine(ENCODER_TURN_100, true)) {
            return StateName.FOLLOW_LINE;
        }
        return StateName.GAP;
    }
    
    private boolean searchLine(int encoderValue, boolean rightTurn) throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
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
        
        Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
        Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
        
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
            
            checkRobotInputs(sample);
            
            double gray = AutoAdjustFilter.getGrayValue(sample);
            if (lineFound(gray)) {
                return true;
            }
            m1TachoCount = Math.abs(m1.getTachoCount());
            m2TachoCount = Math.abs(m2.getTachoCount());
        }
        
        m1.stop(true);
        m2.stop(true);
        
        return false;
    }
}
