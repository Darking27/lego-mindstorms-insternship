package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class Main {
    
    static String COLOR_SENSOR_PORT = "S1";
    static String LEFT_MOTOR_PORT = "A";
    static String RIGHT_MOTOR_PORT = "B";

    public static void main(String[] args) {
        Brick brick = BrickFinder.getDefault();
        LineFollower lineFollower = new LineFollower(brick, COLOR_SENSOR_PORT, LEFT_MOTOR_PORT, RIGHT_MOTOR_PORT);
        
        Key escape = brick.getKey("Escape");
        
        while (!escape.isDown()) {
            lineFollower.isOnLine();
            Delay.msDelay(50);
        }
        
        lineFollower.followLineSimple();

    }

}
