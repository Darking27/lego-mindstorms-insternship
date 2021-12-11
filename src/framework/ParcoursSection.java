package framework;

import boxMover.BoxMoverWrapper;
import boxMover.ExitFinder;
import boxMover.TransitionLineBox;
import boxMover.BoxMover;
import boxMover.WallFollower;
import bridgeFollower.BridgeFollower;
import bridgeFollower.BridgeFollowerV2;
import bridgeFollower.BridgeFollowerV3;
import exceptions.KeyPressedException;
import lineFollower.stateMachine.LineFollowerController;
import markerSearcher.MarkerSearcher;
import markerSearcher.MarkerSearcherV2;

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
	EXIT_FINDER (new ExitFinder()),
	BOX_FINDER (new BoxMover()),
	WALL_FOLLOWER (new WallFollower()),
	TEST_WALKER (new TestWalker()),
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new BoxMoverWrapper()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new MarkerSearcherV2()),
	LINE_BOX_TRANSITIONER(new TransitionLineBox()),
	BRIDGE_V2(new BridgeFollowerV2()),
	BRIDGE_V3(new BridgeFollowerV3());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
