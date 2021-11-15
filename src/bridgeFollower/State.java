package bridgeFollower;

/**
 * States of BridgeFollower
 * 
 * @author Niklas Arlt
 *
 */
public enum State {
	/*
	 * We are driving up the ramp, a bit to the left to get to the left edge.
	 */
	DRIVING_STRAIT,
	
	/*
	 * We are driving on a relatively big circle to the left. We are on the bridge / ramp.
	 * If the sensor sees the ground then we drive right again.
	 */
	DRIVING_LEFT,
	
	/*
	 * We are turning right without moving forward.
	 * We are on the left edge of the bridge / ramp.
	 */
	ROTATE_RIGHT,
	
	/*
	 * We are driving on a smaller circle to the left until we meet the left edge again.
	 */
	TURN_LEFT
}