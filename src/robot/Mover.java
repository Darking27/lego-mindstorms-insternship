package robot;

/**
 * Is responsible for all moving parts in the robot
 * @author felix
 *
 */
public class Mover {
	
	//TODO work with event queue??
	
	/**
	 * Tilts the ultrasonic sensor to eighther face forwards (tilt up) or
	 * towards the ground (tilt down)
	 * @param direction Allowed direction: Up and Down
	 */
	public void tiltUltrasonicSensor(Direction direction) {
		
	}
	
	/**
	 * 
	 * @param direction Forwards or Backwards
	 * @param speed Range from 1-100
	 */
	void drive(Direction direction, int speed) {
		
	}
	
	/**
	 * 
	 * @param direction Forwards or Backwards
	 * @param distance in mm
	 */
	public void moveDistance(Direction direction, int distance) {
		
	}
	
	/**
	 * 
	 * @param direction Right or Left
	 * @param speed Range from 1-100
	 * @param degree how much it turns
	 * @param bothMotors 	true: 	the robot turns with both motors spinning in
	 * 								opposite directions (turns on a point)
	 * 						false:	the robot turns with on motor spinning and one
	 * 								blocking (turn while driving)
	 */
	public void turn(Direction direction, int speed, int degree, boolean bothMotors) {
		
	}
}
