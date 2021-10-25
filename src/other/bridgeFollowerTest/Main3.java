package other.bridgeFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class Main3 {
    
    static String US_SENSOR_PORT = "S3";
    static String LEFT_MOTOR_PORT = "A";
    static String RIGHT_MOTOR_PORT = "B";

    public static void main(String[] args) {
        Brick brick = BrickFinder.getDefault();
        BridgeFollower lineFollower = new BridgeFollower(brick, US_SENSOR_PORT, LEFT_MOTOR_PORT, RIGHT_MOTOR_PORT);
        
        Key enter = brick.getKey("Enter");
        
        while (!enter.isDown()) {
            lineFollower.isOnLine();
            Delay.msDelay(50);
        }
        
        lineFollower.followLineSimple();

    }

}
