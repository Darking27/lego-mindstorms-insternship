package bridgeFollower;

public enum TunnelFinderState {
	
	/**
	 * Robot is driving back to then rotate left
	 */
	DRIVNG_BACK_LEFT,
	
	/**
	 * Robot is rotating left to then drive forward again
	 */
	ROTATE_LEFT,
	
	/**
	 * Robot is driving back to then rotate right
	 */
	DRIVING_BACK_RIGHT,
	
	/**
	 * Robot is rotating right to then drive forward again
	 */
	ROTATE_RIGHT,
	
	/**
	 * Robot is driving forward and searches blue line
	 */
	DRIVING_STRAIT

}
