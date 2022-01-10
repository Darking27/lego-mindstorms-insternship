package new_bridge;

import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.SampleProvider;

public class FullBridgeController implements ParcoursWalkable{
	
	private float[] uSample;
	private float[] lTouchSample;
	private float[] rTouchSample;
	private float[] colorSample;
	private SampleProvider ultrasonicSampleProvider;
	private SampleProvider leftTouchSampleProvider;
	private SampleProvider rightTouchSampleProvider;
	private SampleProvider colorSampleProvider;

	public FullBridgeController() {
		uSample = new float[1];
		rTouchSample = new float[1];
		lTouchSample = new float[1];
		colorSample = new float[1];
		ultrasonicSampleProvider = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		leftTouchSampleProvider = Ports.LEFT_TOUCH_SENSOR.getTouchMode();
		rightTouchSampleProvider = Ports.RIGHT_TOUCH_SENSOR.getTouchMode();
		colorSampleProvider = Ports.COLOR_SENSOR.getColorIDMode();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		
		
		
		
		return null;
	}
	
	private boolean onBridge() {
		ultrasonicSampleProvider.fetchSample(uSample, 0);
		return uSample[0] < 0.13f;
	}
}
