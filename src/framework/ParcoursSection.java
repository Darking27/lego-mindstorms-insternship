package framework;

import boxMover.BoxFinder;
import boxMover.TestWalker;
import boxMover.WallFollower;
import bridgeFollower.BridgeFollower;
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
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new WallFollower()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new MarkerSearcher()),
	BOX_FINDER (new BoxFinder());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() {
		return this.walker.start_walking();
	}
}
