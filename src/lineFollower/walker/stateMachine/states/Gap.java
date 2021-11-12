package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.FinishLineException;
import lineFollower.walker.stateMachine.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.RobotCollisionException;
import lineFollower.walker.stateMachine.StateName;

public class Gap extends BaseState {
	
	public Gap() {
		super();
		this.stateName = StateName.GAP;
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
		if (driveForwardStraight(ENCODER_GAP_DISTANCE, true)) {
			// Line found
			return StateName.FOLLOW_LINE;
		}
		// Line not found
		return StateName.SEARCH_LINE;
	}
}
