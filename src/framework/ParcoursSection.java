package framework;

import boxMover.BoxMover;
import boxMover.BoxMoverWrapper;
import boxMover.CurveBoxMover;
import boxMover.ExitFinder;
import boxMover.TransitionLineBox;
import bridgeFollower.BridgeFollower;
import bridgeFollower.BridgeFollowerV2;
import bridgeFollower.BridgeFollowerV3;
import bridgeFollower.BridgeFollowerV4;
import exceptions.KeyPressedException;
import lineFollower.stateMachine.LineFollowerController;
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
	TEST_WALKER (new TestWalker()),
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new BoxMoverWrapper()),
	MARKER_SEARCHER  (new MarkerSearcherV2()),
	LINE_BOX_TRANSITIONER(new TransitionLineBox()),
	BRIDGE(new BridgeFollowerV4());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
