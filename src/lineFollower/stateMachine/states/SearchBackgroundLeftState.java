package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import exceptions.RobotErrorException;
import framework.Ports;
import lejos.robotics.RegulatedMotor;
import lineFollower.colorSensor.AutoAdjustFilter;
import lineFollower.stateMachine.StateName;

public class SearchBackgroundLeftState extends BaseState {
    
    public SearchBackgroundLeftState() {
        super();
        this.stateName = "Search Background left";
    }
    
    @Override
    public StateName handleState()
            throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException, RobotErrorException {
        if (searchBackground(ENCODER_TURN_100)) {
            return StateName.FOLLOW_LINE;
        }
        throw new RobotErrorException("cannot find background");
    }
    
    private boolean searchBackground(int encoderValue)
            throws RobotCollisionException, ProcessInteruptedEnterException, FinishLineException {
        
        float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
        
        Ports.LEFT_MOTOR.resetTachoCount();
        Ports.RIGHT_MOTOR.resetTachoCount();
        
        RegulatedMotor m1;
        RegulatedMotor m2;
    
        m1 = Ports.RIGHT_MOTOR;
        m2 = Ports.LEFT_MOTOR;
        
        int m1TachoCount = 0;
        int m2TachoCount = 0;
        
        Ports.LEFT_MOTOR.setSpeed(150);
        Ports.RIGHT_MOTOR.setSpeed(150);
        
        m1.forward();
        m2.backward();
        
        while (Math.abs(m2TachoCount) <= encoderValue || Math.abs(m2TachoCount) <= encoderValue) {
            autoAdjustRGBFilter.fetchSample(sample, 0);
            
            if (Math.abs(m1TachoCount) >= encoderValue) {
                m1.stop(true);
            }
            if (Math.abs(m2TachoCount) >= encoderValue) {
                m2.stop(true);
            }
            
            checkRobotInputs(sample, true);
            
            double gray = AutoAdjustFilter.getGrayValue(sample);
            if (gray <= 0.4) {
                System.out.println("Found background, gray value:" + gray);
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
