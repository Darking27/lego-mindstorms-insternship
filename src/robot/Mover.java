package robot;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Is responsible for all moving parts in the robot
 * @author felix
 *
 */
public class Mover {
	private RegulatedMotor tiltMotor;
	
	public static void main(String[] args) {
		Mover m = new Mover(BrickFinder.getDefault(), "C");
		m.tiltUltrasonicSensor(null);
	}
	
	public Mover(Brick brick, String tiltMotorPort) {
		tiltMotor = new EV3MediumRegulatedMotor(brick.getPort(tiltMotorPort));
	}
	
	//TODO work with event queue??
	
	/**
	 * Tilts the ultrasonic sensor to eighther face forwards (tilt up) or
	 * towards the ground (tilt down)
	 * @param direction Allowed direction: Up and Down
	 */
	public void tiltUltrasonicSensor(Direction direction) {
		tiltMotor.setSpeed(100);
		tiltMotor.rotateTo(0);
		Delay.msDelay(2000);
		tiltMotor.rotateTo(70);
		tiltMotor.stop();
		Delay.msDelay(4000);
		tiltMotor.rotateTo(0);
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
