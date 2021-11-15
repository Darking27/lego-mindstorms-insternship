package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;

public class GapState extends BaseState {
	
	public GapState() {
		super();
		this.stateName = "Gap";
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
	    // TODO should search line true
	    turnLeft(50, false, true);
		if (driveForwardStraight(ENCODER_GAP_DISTANCE, true, true)) {
			// Line found
		    System.out.println("Line found while driving gap");
			return StateName.FOLLOW_LINE;
		}
		// Line not found
		return StateName.SEARCH_LINE;
	}
}
