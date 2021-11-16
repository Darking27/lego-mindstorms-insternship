package markerSearcher;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class MarkerSearcher implements ParcoursWalkable {
	
	private final int TURNING_VALUE = 200;
	private final int BACKWARD_VALUE = 200;

	private final int MARKER_ONE_COLOR_ID = 0;	// white
	private final int MARKER_TWO_COLOR_ID = 1;	// red
	
	private boolean markerOneFound;
	private boolean markerTwoFound;
	
	SampleProvider colorIdSensor;
	
	public MarkerSearcher() {
		colorIdSensor = Ports.COLOR_SENSOR.getColorIDMode();
		
		markerOneFound = false;
		markerTwoFound = false;
	}
	
	@Override
	public WalkableStatus start_walking() {
		float[] colorSensorSample = new float[colorIdSensor.sampleSize()];
		
		Ports.LEFT_MOTOR.setSpeed(300);
		Ports.RIGHT_MOTOR.setSpeed(300);
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		reset();
		while (!(markerOneFound && markerTwoFound)) {
			colorIdSensor.fetchSample(colorSensorSample, 0);
			if (!markerOneFound && isMarkerOne(colorSensorSample)) {
				System.out.println("Marker 1  Found");
				markerOneFound = true;
			}
			if (!markerTwoFound && isMarkerTwo(colorSensorSample)) {
				System.out.println("Marker 2  Found");
				markerTwoFound = true;
			}
			
			// TODO: change so the program can be exited while turning
			if (leftButtonPressed()) {
				Ports.LEFT_MOTOR.rotate(-BACKWARD_VALUE, true);
				Ports.RIGHT_MOTOR.rotate(-BACKWARD_VALUE);
				
				Ports.LEFT_MOTOR.rotate(TURNING_VALUE, true);
				Ports.RIGHT_MOTOR.rotate(-TURNING_VALUE);
			}
			if (rightButtonPressed()) {
				Ports.LEFT_MOTOR.rotate(-BACKWARD_VALUE, true);
				Ports.RIGHT_MOTOR.rotate(-BACKWARD_VALUE);
				
				Ports.RIGHT_MOTOR.rotate(TURNING_VALUE, true);
				Ports.LEFT_MOTOR.rotate(-TURNING_VALUE);
			}
			
			if (enterPressed()) {
				return WalkableStatus.MENU;
			}
		}
		
		Ports.LEFT_MOTOR.stop();
		Ports.RIGHT_MOTOR.stop();
		
		return WalkableStatus.FINISHED;
	}
	
	private boolean isMarkerOne(float[] colorSensorSample) {
		return colorSensorSample[0] == MARKER_ONE_COLOR_ID;
	}
	
	private boolean isMarkerTwo(float[] colorSensorSample) {
		return colorSensorSample[0] == MARKER_TWO_COLOR_ID;
	}
	
	public void reset() {
		markerOneFound = false;
		markerTwoFound = false;
	}
	
	private boolean leftButtonPressed() {
		float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
		
		Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
		
		return touchLeft[0] == 1;
	}
	
	private boolean rightButtonPressed() {
		float[] touchRight = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
		
		Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
		
		return touchRight[0] == 1;
	}
	
	private boolean enterPressed() {
    	return Ports.ENTER.isDown();
    }
}
