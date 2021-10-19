package other.motorDelayTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;

public class Main {

	public static void main(String[] args) {
		
		Brick brick = BrickFinder.getDefault();
		
		Mover mover = new Mover(brick, "B", "C");
		mover.moveMotors();
		
//		ColorSensor sensor = new ColorSensor(brick, "S1");
//		sensor.sample();

	}

}
