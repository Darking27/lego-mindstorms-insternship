package driving.test;

import driving.DriveParallelTask;
import framework.Ports;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class DriveParallelTest {

	public static void main(String[] args) {
		Key escape = Ports.BRICK.getKey("Escape");
		Key enter = Ports.BRICK.getKey("Enter");
		
		DriveParallelTask task = new DriveParallelTask(300);
		
		while(escape.isUp() && enter.isUp()) {
			task.run();
			// Delay.msDelay(100);
		}
		
		task.cancel();
		Delay.msDelay(5000);
	}
}
