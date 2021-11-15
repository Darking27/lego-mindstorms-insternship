package lineFollower.walker.stateMachine;

import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;
import lineFollower.walker.stateMachine.exceptions.RobotErrorException;
import lineFollower.walker.stateMachine.states.*;

public enum StateName {
    START(new StartState()),
    OBSTACLE(new ObstacleState()),
    SEARCH_LINE(new SearchLineState()),
    FOLLOW_LINE(new FollowLineState()),
    SEARCH_BACKGROUND_LEFT(new SearchBackgroundLeftState()),
    GAP(new GapState());
	
	private final BaseState state;
	
	private StateName(BaseState state) {
		this.state = state;
	}
	
	public StateName handleState()
    		throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException, RobotErrorException {
	    this.state.logCurrentState();
		return this.state.handleState();
	}
}
