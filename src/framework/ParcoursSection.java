package framework;

import boxMover.BoxMoverWrapper;
import boxMover.ExitFinder;
import boxMover.TransitionLineBox;
import exceptions.KeyPressedException;
import lineFollower.stateMachine.LineFollowerController;
import markerSearcher.MarkerSearcherV2;
import new_bridge.FullBridgeController;

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
	BOX_MOVE (new BoxMoverWrapper()),
	BRIDGE(new FullBridgeController()),
	MARKER_SEARCHER  (new MarkerSearcherV2()),
	LINE_BOX_TRANSITIONER(new TransitionLineBox());
	
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() throws KeyPressedException{
		return this.walker.start_walking();
	}
}
