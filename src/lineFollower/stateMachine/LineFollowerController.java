package lineFollower.stateMachine;

import exceptions.FinishLineException;
import exceptions.ProcessInteruptedEnterException;
import exceptions.RobotCollisionException;
import exceptions.RobotErrorException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;
import lejos.utility.Delay;

public class LineFollowerController implements ParcoursWalkable {
	
	StateName state = StateName.START;
	StateName[] orderState = {StateName.START, StateName.GAP};
	
	@Override
	public WalkableStatus start_walking() {
	    StateName state = StateName.START;
		while (true) {
			try {
				state = state.handleState();
			} catch (RobotErrorException e) {
				System.out.println(e);
				Delay.msDelay(2000);
				System.exit(1);
			} catch (ProcessInteruptedEnterException e) {
			    System.out.println("Menu");
				return WalkableStatus.MENU;
			} catch (RobotCollisionException e) {
				state = StateName.OBSTACLE;
			} catch (FinishLineException e) {
			    System.out.println("Finished");
				return WalkableStatus.FINISHED;
			}
		}
	}
}
