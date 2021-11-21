package boxMover;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() {
		Delay.msDelay(500);
		while(true) {
			if (Ports.ESCAPE.isDown())
				return WalkableStatus.STOP;
			if (Ports.ENTER.isDown())
				return WalkableStatus.MENU;
			
			System.out.println("Testwalker is walking");
		}
	}
}
