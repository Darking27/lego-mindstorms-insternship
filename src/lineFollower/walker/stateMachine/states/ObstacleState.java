package lineFollower.walker.stateMachine.states;

import framework.Ports;
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
		
	    Ports.LEFT_MOTOR.setSpeed(300);
	    Ports.RIGHT_MOTOR.setSpeed(300);
	    
	    Ports.LEFT_MOTOR.rotate(-100, true);
	    Ports.RIGHT_MOTOR.rotate(-100, false);
	    
		driveBackwardStraight(400, false, true);
		turnLeft(200, false, true);
		driveForwardStraight(900, false, true);
		turnRight(200, false, true);
		driveForwardStraight(900, false, true);
		turnRight(200, false, true);
		if (driveForwardStraight(1500, true, true)) {
			return StateName.FOLLOW_LINE;
		} else {
			throw new RobotErrorException("Robot can not find line after obstacle");
		}
	}

}
