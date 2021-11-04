package lineFollower.v2;


public enum LineFollowerState {
    
	// When the robot did not find the line before the end of a timeout,
	// it lost the line
	
	/**
	 * When the robot normally follows the line and is on the
	 * currently background (before end of the timeout)
	 * 
	 * Slow mode
	 * 
	 * drives right to go back on the line
	 */
	DRIVE_FORWARD_RIGHT_SLOW,
	
	/**
	 * When the robot normally follows the line and is currently
	 * on the line (before end of the timeout)
	 * 
	 * Slow mode
	 * 
	 * drives left to go back on the background
	 */
    DRIVE_FORWARD_LEFT_SLOW,
    
    /**
     * When the robot is on the line for too long it stops and
     * turns on the current point to the left to find the background
     */
    SEARCH_BACKGROUND_LEFT,
    
    /**
     * When the robot lost the line it tries to find it to its
     * right and left by stopping and turning on its current
     * position
     */
    SEARCH_LINE_RIGHT,
    
    /**
     * When the robot lost the line it tries to find it to its
     * right and left by stopping and turning on its current
     * position
     */
    SEARCH_LINE_LEFT,
    
    /**
     * When the robot lost the line and did not find the line neither
     * to its right, nor to its left it assumes to be on a gap and
     * drives forward before searching left and right again
     */
    DRIVE_FORWARD_GAP,
    
    /**
     * When the button on the front gets pressed, the robot has to drive
     * around the obstacle
     */
    DRIVE_AROUND_OBSTACLE,
    
    /**
     * When the robot detects the blue line, the line following part is done
     */
    DONE,
    
    ERROR
    
}
