package boxMover;


import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class WallFollower implements ParcoursWalkable {

	private float[] uSample;
	private float[] lTouchSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;

	public WallFollower() {
		uSample = new float[1];
		lTouchSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException{
		Delay.msDelay(300);  // TODO remove delay
		
		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.RIGHT_MOTOR.setSpeed(310); // different speed: slowly drift to the left wall

		boolean nearTheBackWall = false;
		
		while (!nearTheBackWall) {
			
			if (Ports.ESCAPE.isDown())
				throw new StopException();
			if (Ports.ENTER.isDown())
				throw new MenuException();
			
			leftTouchSampleProvider.fetchSample(lTouchSample, 0);
			if (lTouchSample[0] > 0.5f) {  // turn a bit right when the left touch sensor is pressed
				System.out.println("touch pressed");
				
				Ports.LEFT_MOTOR.flt(true);
				Ports.RIGHT_MOTOR.rotate(-40, false);
			}
			
			Ports.LEFT_MOTOR.rotate(100, true);
			Ports.RIGHT_MOTOR.rotate(100, true);
			
			ultrasonicSampleProvider.fetchSample(uSample, 0);
			float backWallDistanceThreshold = 0.60f;
			nearTheBackWall = uSample[0] < backWallDistanceThreshold;
			
			System.out.println("distance= " + uSample[0]);
			//System.out.println("end= " + nearTheBackWall);
		}
		
		Delay.msDelay(1000);
		
		return WalkableStatus.FINISHED;
	}
}
