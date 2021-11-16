package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import lineFollower.stateMachine.StateName;

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
