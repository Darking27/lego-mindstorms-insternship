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
	LINE_FOLLOW (new LineFollower()),
	BOX_MOVE (new BoxMove()),
	BRIDGE  (new BridgeFollower()),
	COLOR_SEARCH  (new ColorSearch());
	
	private final ParcoursWalkable walker;
	
	private ParcoursSection(ParcoursWalkable walker) {
		this.walker = walker;
	}
	
	public void start_walking() {
		this.walker.start_walking();
	}
}
