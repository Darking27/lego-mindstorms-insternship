package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import framework.Ports;
import lejos.robotics.RegulatedMotor;
import lineFollower.colorSensor.AutoAdjustFilter;
import lineFollower.stateMachine.StateName;

public class SearchLineState extends BaseState {

    public SearchLineState() {
        super();
        this.stateName = "Search line";
    }
    
    @Override
    public StateName handleState() throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
        if (driveForwardStraight(50, false, true)) {
            return StateName.FOLLOW_LINE;
        }
        if (searchLine(ENCODER_TURN_100, true)) {
            return StateName.FOLLOW_LINE;
        }
        // Maybe needs to search background first before follow line state
        if (searchLine(2 * ENCODER_TURN_100, false)) {
            return StateName.SEARCH_BACKGROUND_LEFT;
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
            
            checkRobotInputs(sample, true);
            
            double gray = AutoAdjustFilter.getGrayValue(sample);
            if (lineFound(gray)) {
                m1.stop(true);
                m2.stop();
                return true;
            }
            m1TachoCount = Math.abs(m1.getTachoCount());
            m2TachoCount = Math.abs(m2.getTachoCount());
        }
        
        m1.stop(true);
        m2.stop();
        
        return false;
    }
}
