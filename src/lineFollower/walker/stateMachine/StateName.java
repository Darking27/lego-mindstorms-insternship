package lineFollower.walker.stateMachine;

public enum StateName {
    START,
    OBSTACLE,
    SEARCH_LINE,
    FOLLOW_LINE,
    GAP,
    // SEARCH_LEFT_LINE_BORDER		// Maybe not even needed
}
