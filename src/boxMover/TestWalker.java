package boxMover;

import framework.ParcoursWalkable;
import framework.WalkableStatus;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.utility.Delay;

public class TestWalker implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() {
		Delay.msDelay(500);
		while(true) {
			Brick brick = BrickFinder.getDefault();
			if(brick.getKey("Enter").isDown()) {
				return WalkableStatus.MENU;
			}
			
			if(brick.getKey("Escape").isDown()) {
				return WalkableStatus.STOP;
			}
			
			System.out.print(".");
		}
	}

}
