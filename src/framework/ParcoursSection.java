package framework;

import boxMover.BoxMoverWrapper;
import boxMover.ExitFinder;
import boxMover.RightAngleBoxFinder;
import boxMover.WallFollower;
import bridgeFollower.BridgeFollower;
import exceptions.KeyPressedException;
import lineFollower.stateMachine.LineFollowerController;
import markerSearcher.MarkerSearcher;

/**
 * Defines the different parcours Section
 * Each section calls its own Walker on Creation
 * 
 * You can call Section.start_walking() which delegates the action
 * to the Walker defined after the ENUM name
 *
 * @author Samuel Born
 *
 */
public enum ParcoursSection {
	EXIT_FINER (new ExitFinder()),
	BOX_FINDER (new RightAngleBoxFinder()),
	WALL_FOLLOWER (new WallFollower()),
	TEST_WALKER (new BoxMoverWrapper()),
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new BoxMoverWrapper()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new MarkerSearcher());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
