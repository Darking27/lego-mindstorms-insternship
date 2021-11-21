package boxMover;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;

public class BoxFinder2 implements ParcoursWalkable {
	
	public BoxFinder2() {
		Ports.LEFT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.RIGHT_TOUCH_SENSOR.setCurrentMode("Touch");
		Ports.ULTRASONIC_SENSOR.setCurrentMode("Distance");
		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.RIGHT_MOTOR.setSpeed(300);
	}

	@Override
	public WalkableStatus start_walking() {
		boolean boxFound = false;
		while(!boxFound) {
			if (Ports.ESCAPE.isDown()) {
				return WalkableStatus.STOP;
			}
			driveForward();
			boxFound = search();
		}
		return WalkableStatus.STOP;
	}
	
	private boolean search() {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.LEFT_MOTOR.rotate(600, true);
		
		SampleProvider s = new MedianFilter(Ports.ULTRASONIC_SENSOR.getDistanceMode(), 5);
		float[] sample = new float[1];
		
		while(Ports.LEFT_MOTOR.isMoving()) {
			s.fetchSample(sample, 0);	
			if (sample[0] < 0.35) return true;
		}
		
		while(Ports.LEFT_MOTOR.getTachoCount()>0) {
			Ports.LEFT_MOTOR.rotate(-100, true);
		}
		Ports.LEFT_MOTOR.stop();
		return false;
	}

	public void driveForward() {
		Ports.LEFT_MOTOR.rotate(400,true);
		Ports.RIGHT_MOTOR.rotate(400);
	}
}
