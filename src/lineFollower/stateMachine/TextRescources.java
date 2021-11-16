package lineFollower.stateMachine;

public enum TextRescources {

	FINISH_LINE_EXCEPTION("Finish line found, exiting line follower"),
	ENTER_EXCEPTION("Enter pressed: Walker terminated"),
	COLLOSION_EXCEPTION_TEXT("Robot detected collision from front button press");
	
	private String text;
	
	private TextRescources(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
