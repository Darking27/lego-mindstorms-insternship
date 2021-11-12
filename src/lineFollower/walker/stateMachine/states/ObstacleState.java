package lineFollower.walker.stateMachine.states;

import lineFollower.walker.stateMachine.StateName;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;
import lineFollower.walker.stateMachine.exceptions.RobotErrorException;

public class ObstacleState extends BaseState {
	
	public ObstacleState() {
		super();
		this.stateName = StateName.OBSTACLE;
	}
	
	@Override
	public StateName handleState()
			throws ProcessInteruptedEnterException, RobotCollisionException, FinishLineException, RobotErrorException {
		
		driveBackwardStraight(300, false);
		turnLeft(150, false);
		driveForwardStraight(600, false);
		turnRight(150, false);
		driveForwardStraight(800, false);
		turnRight(150, false);
		if (driveForwardStraight(1000, true)) {
			return StateName.FOLLOW_LINE;
		} else {
			throw new RobotErrorException("Robot can not find line after obstacle");
		}
	}

}
