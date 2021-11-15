package lineFollower.walker.stateMachine;

import framework.ParcoursWalkable;
import framework.WalkableStatus;
import lejos.utility.Delay;
import lineFollower.walker.stateMachine.exceptions.FinishLineException;
import lineFollower.walker.stateMachine.exceptions.ProcessInteruptedEnterException;
import lineFollower.walker.stateMachine.exceptions.RobotCollisionException;
import lineFollower.walker.stateMachine.exceptions.RobotErrorException;

public class LineFollowerController implements ParcoursWalkable {
	
	StateName state = StateName.START;
	StateName[] orderState = {StateName.START, StateName.GAP};
	
	@Override
	public WalkableStatus start_walking() {
		while (true) {
			try {
				state = state.handleState();
			} catch (RobotErrorException e) {
				System.out.println(e);
				Delay.msDelay(2000);
				System.exit(1);
			} catch (ProcessInteruptedEnterException e) {
			    System.out.println("Menu");
                Delay.msDelay(2000);
				return WalkableStatus.MENU;
			} catch (RobotCollisionException e) {
				state = StateName.OBSTACLE;
			} catch (FinishLineException e) {
			    System.out.println("Finished");
			    Delay.msDelay(2000);
				return WalkableStatus.FINISHED;
			}
		}
	}
}
