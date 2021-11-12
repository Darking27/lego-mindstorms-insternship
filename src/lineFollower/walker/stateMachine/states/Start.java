package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.FinishLineException;
import lineFollower.walker.stateMachine.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.RobotCollisionException;
import lineFollower.walker.stateMachine.StateName;

public class Start extends BaseState {
    
    Start() {
        super();
        this.stateName = StateName.START;
    }

    @Override
    public StateName handleState()
    		throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
        
        logCurrentState();
        
        driveForwardStraight(ENCODER_GAP_DISTANCE, false);
        
        turnRight(ENCODER_TURN_45, false);
        turnRight(2 * ENCODER_TURN_45, false);
        turnRight(ENCODER_TURN_45, false);
        
        return StateName.SEARCH_LINE;
    }
}
