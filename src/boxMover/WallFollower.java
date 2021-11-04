package boxMover;

import framework.ParcoursWalkable;
import lejos.hardware.Brick;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;

public class WallFollower implements ParcoursWalkable {
	
	Brick brick;
	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;
	EV3TouchSensor touchSensor;

	public WallFollower(Brick brick, String leftTouchSensorPort, String leftMotorPort, String rightMotorPort) {
		this.brick = brick;
		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
		Port sensorPort = brick.getPort(leftTouchSensorPort);
		this.touchSensor = new EV3TouchSensor(sensorPort);
		this.touchSensor.setCurrentMode("Touch");
	}

	/**
	 * follows the left side of a wall -> bumps into the wall, turns right and leaves the wall, drive slowly towards the left wall again, bump into it...
	 * uses the left touch sensor
	 * stops when a wall is less then _x_ Centimeters away
	 */
	@Override
	public void start_walking() {
		
		float[] touchSample = new float[touchSensor.sampleSize()];
		
		// TODO: Detect wall distance to stop the start_walking method
		while(true) {
			touchSensor.fetchSample(touchSample, 0);
			if(touchSample[0]>0.5) { // sensor is pressed
				rightMotor.flt(true);
				// turn right
				leftMotor.rotate(300, false);
				
				// drive away from the wall
				rightMotor.setSpeed(300);
				leftMotor.setSpeed(300);
				rightMotor.rotate(1000, true);
				leftMotor.rotate(1000, false);
				
			} else { // sensor is not pressed
				
				// makes a left turn
				rightMotor.setSpeed(400);
				leftMotor.setSpeed(300);
				
				rightMotor.rotate(1000, true);
				leftMotor.rotate(1000, true);
			}
		}
	}

}
