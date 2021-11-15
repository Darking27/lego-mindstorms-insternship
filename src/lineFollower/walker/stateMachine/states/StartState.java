package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;

public class StartState extends BaseState {
    
    public StartState() {
        super();
        this.stateName = "Start";
    }

    @Override
    public StateName handleState()
    		throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
        
        driveForwardStraight(ENCODER_GAP_DISTANCE, false, false);
        
        turnRight(ENCODER_TURN_45, false, false);
        turnLeft(2 * ENCODER_TURN_45, false, false);
        turnRight(ENCODER_TURN_45, false, false);
        
        return StateName.SEARCH_LINE;
    }
}
