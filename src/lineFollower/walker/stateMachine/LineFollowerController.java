package lineFollower.walker.stateMachine;

import framework.ParcoursWalkable;
import framework.WalkableStatus;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;
import lineFollower.walker.stateMachine.exceptions.RobotErrorException;

public class LineFollowerController implements ParcoursWalkable {
	
	StateName state = StateName.START;
	
	@Override
	public WalkableStatus start_walking() {
		while (true) {
			try {
				state = state.handleState();
			} catch (RobotErrorException e) {
				System.out.println(e);
				System.exit(1);
			} catch (ProcessInteruptedEnterException e) {
				return WalkableStatus.MENU;
			} catch (RobotCollisionException e) {
				state = StateName.OBSTACLE;
			} catch (FinishLineException e) {
				return WalkableStatus.FINISHED;
			}
		}
	}
}
