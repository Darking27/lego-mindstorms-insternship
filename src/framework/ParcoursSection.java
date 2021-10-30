package framework;

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
	COLOR_SEARCH  (new TestWalker()),
	BRIDGE  (new TestWalker());
	
	private final ParcoursWalker walker;
	
	private ParcoursSection(ParcoursWalker walker) {
		this.walker = walker;
	}
	
	public void start_walking() {
		this.walker.start_walking();
	}
}
