package framework;

import boxMover.BoxMoverWrapper;
import boxMover.ExitFinder;
import boxMover.NewBoxFinder;
import boxMover.TransitionLineBox;
import boxMover.TurnBoxFinder;
import boxMover.tests.DistanceTimePlotter;
import bridgeFollower.BridgeFollower;
import bridgeFollower.BridgeFollowerV2;
import bridgeFollower.BridgeFollowerV3;
import bridgeFollower.BridgeFollowerV4;
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
	TURN_BOX_MOVE(new TurnBoxFinder()),
	EXIT_FINDER (new ExitFinder()),
	NEW_BOX_MOVER (new NewBoxFinder()),
	TEST_WALKER (new DistanceTimePlotter()),
	LINE_FOLLOW (new LineFollowerController()),
	BOX_MOVE (new BoxMoverWrapper()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new MarkerSearcherV2()),
	LINE_BOX_TRANSITIONER(new TransitionLineBox()),
	BRIDGE_V2(new BridgeFollowerV2()),
	BRIDGE_V3(new BridgeFollowerV3()),
	BRIDGE_V4(new BridgeFollowerV4());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
