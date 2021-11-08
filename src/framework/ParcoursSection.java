package framework;

import boxMover.TestWalker;
import boxMover.WallFollower;
import lejos.hardware.BrickFinder;

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
	LINE_FOLLOW (new TestWalker()),
	BOX_MOVE (new TestWalker()),
	BRIDGE  (new TestWalker()),
	COLOR_SEARCH  (new TestWalker());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public WalkableStatus start_walking() {
		return this.walker.start_walking();
	}
}
