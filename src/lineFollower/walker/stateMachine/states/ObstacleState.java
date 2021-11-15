package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;
import lineFollower.walker.stateMachine.exceptions.RobotErrorException;

public class ObstacleState extends BaseState {
	
	public ObstacleState() {
		super();
		this.stateName = "Obstacle";
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException, RobotErrorException {
		
		driveBackwardStraight(400, false, true);
		turnLeft(200, false, true);
		driveForwardStraight(800, false, true);
		turnRight(200, false, true);
		driveForwardStraight(900, false, true);
		turnRight(200, false, true);
		if (driveForwardStraight(1000, true, true)) {
			return StateName.FOLLOW_LINE;
		} else {
			throw new RobotErrorException("Robot can not find line after obstacle");
		}
	}

}