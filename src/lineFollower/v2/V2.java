package lineFollower.v2;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class V2 {
	
	static String COLOR_SENSOR_PORT = "S1";
	static String TOUCH_SENSOR_LEFT_PORT = "S2";
	static String TOUCH_SENSOR_RIGHT_PORT = "S3";
    static String LEFT_MOTOR_PORT = "A";
    static String RIGHT_MOTOR_PORT = "B";

    public static void main(String[] args) {
    	System.out.println("Starting Line Follower V2");
        Brick brick = BrickFinder.getDefault();
        LineFollower lineFollower = new LineFollower(brick, COLOR_SENSOR_PORT, LEFT_MOTOR_PORT, RIGHT_MOTOR_PORT, TOUCH_SENSOR_RIGHT_PORT, TOUCH_SENSOR_LEFT_PORT);
        
        Key escape = brick.getKey("Enter");
        
        while (!escape.isDown()) {
            Delay.msDelay(10);
        }
        lineFollower.followLine();

    }
}
