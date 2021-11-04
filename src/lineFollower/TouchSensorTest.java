package lineFollower;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class TouchSensorTest {
	
	static String TOUCH_SENSOR_LEFT_PORT = "S2";
	static String TOUCH_SENSOR_RIGHT_PORT = "S3";
	static Brick brick;

    public static void main(String[] args) {
    	System.out.println("Starting Touch Sensor Test:");
        brick = BrickFinder.getDefault();
        
        testTouchSensor();

    }
    
    public static void testTouchSensor() {
    	SampleProvider s1 = new EV3TouchSensor(brick.getPort(TOUCH_SENSOR_RIGHT_PORT));
    	SampleProvider s2 = new EV3TouchSensor(brick.getPort(TOUCH_SENSOR_LEFT_PORT));
    	
    	Key escape = brick.getKey("Enter");
    	
    	while (!escape.isDown()) {
            if (touchSensorPressed(s1)) {
            	System.out.print("s1 pressed");
            }
            if (touchSensorPressed(s2)) {
            	System.out.print("s2 pressed");
            }
    		Delay.msDelay(200);
        }
    }
    
    private static boolean touchSensorPressed(SampleProvider sampleProvider) {
    	float[] sample = new float[sampleProvider.sampleSize()];
    	
    	sampleProvider.fetchSample(sample, 0);
    	
    	return sample[0] == 1;
    }
}
