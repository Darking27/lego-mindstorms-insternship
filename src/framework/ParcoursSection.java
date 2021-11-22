package framework;

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
	BOX_FINDER (new RightAngleBoxFinder()),
	//TEST_FOLLOW (new TestWalker()),
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new WallFollower()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new MarkerSearcher()),
	WALL_FOLLOWER (new WallFollower());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
