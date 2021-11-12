package lineFollower.walker;

import lineFollower.walker.stateMachine.LineFollowerController;

public class LineFollowerWalker {

	public static void main(String[] args) {
		LineFollowerController lineFollower = new LineFollowerController();
		lineFollower.start_walking();
	}
}
