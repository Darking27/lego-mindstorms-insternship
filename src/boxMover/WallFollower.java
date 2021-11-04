package boxMover;

import framework.ParcoursWalkable;
import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public class WallFollower implements ParcoursWalkable {
	
	Brick brick;
	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;
	EV3TouchSensor touchSensor;
	EV3UltrasonicSensor ultrasonicSensor;

	public WallFollower(Brick brick, String leftTouchSensorPortName, String ultrasonicSensorPortName, String leftMotorPort, String rightMotorPort) {
		this.brick = brick;
		
		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
		
		Port touchSensorPort = brick.getPort(leftTouchSensorPortName);
		this.touchSensor = new EV3TouchSensor(touchSensorPort);
		this.touchSensor.setCurrentMode("Touch");
		
		Port ultrasonicSensorPort = brick.getPort(ultrasonicSensorPortName);
		this.ultrasonicSensor = new EV3UltrasonicSensor(ultrasonicSensorPort);
		this.ultrasonicSensor.setCurrentMode("Distance");
	}

	/**
	 * follows the left side of a wall -> bumps into the wall, turns right and leaves the wall, drive slowly towards the left wall again, bump into it...
	 * uses the left touch sensor
	 * stops when a wall is less then _x_ Centimeters away
	 */
	@Override
	public void start_walking() {
		
		float[] touchSample = new float[touchSensor.sampleSize()];
		float[] ultrasonicSample = new float[ultrasonicSensor.sampleSize()];
		
		while(!closeToBackWall(ultrasonicSample)) {
			touchSensor.fetchSample(touchSample, 0);
			if(touchSample[0]>0.5) { // sensor is pressed
				// turn right
				rightMotor.flt(true);
				leftMotor.rotate(200, false);
				
				// drive away from the wall
				rightMotor.setSpeed(300);
				leftMotor.setSpeed(300);
				rightMotor.rotate(500, true);
				leftMotor.rotate(500, false);
				
				// go back to the else part and slowly drive towards the wall
				
			} else { // sensor is not pressed
				
				// makes a left turn
				rightMotor.setSpeed(400);
				leftMotor.setSpeed(300);
				
				rightMotor.rotate(1000, true);
				leftMotor.rotate(1000, true);
			}
		}
	}	
	
	/**
	 * Checks if the Sensor is close to the back wall
	 *                          
	 *                          |
	 * ROBO->                   |    FALSE
	 *                          |
	 *                          
	 *              |
	 * ROBO->       |    TRUE
	 *              |
	 * 
	 * @param ultrasonicSample the array in which to save the measured values from the ultrasonic sensor
	 */
	private boolean closeToBackWall(float[] ultrasonicSample) {
		double closeToWallInMeter = 0.80;
		touchSensor.fetchSample(ultrasonicSample, 0);
		return ultrasonicSample[0] > closeToWallInMeter;
	}
}
