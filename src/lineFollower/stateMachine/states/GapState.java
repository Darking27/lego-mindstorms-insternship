package lineFollower.stateMachine.states;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import lineFollower.stateMachine.StateName;

public class GapState extends BaseState {
	
	public GapState() {
		super();
		this.stateName = "Gap";
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException {
		
	    // TODO should search line true
	    turnLeft(60, false, true);
		if (driveForwardStraight(ENCODER_GAP_DISTANCE, true, true)) {
			// Line found
		    System.out.println("Line found while driving gap");
			return StateName.FOLLOW_LINE;
		}
		// Line not found
		return StateName.SEARCH_LINE;
	}
}
