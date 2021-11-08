package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class Main2 {
    
    static String COLOR_SENSOR_PORT = "S1";
    static String LEFT_MOTOR_PORT = "A";
    static String RIGHT_MOTOR_PORT = "B";

    public static void main(String[] args) {
    	System.out.println("Test Print");
        Brick brick = BrickFinder.getDefault();
        LineFollower lineFollower = new LineFollower(brick, COLOR_SENSOR_PORT, LEFT_MOTOR_PORT, RIGHT_MOTOR_PORT);
        
        Key escape = brick.getKey("Enter");
        
        while (!escape.isDown()) {
            lineFollower.isOnLine();
            Delay.msDelay(50);
        }
        Delay.msDelay(1000);
        lineFollower.followLineSimple();

    }

}