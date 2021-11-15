package bridgeFollower.tests;

import bridgeFollower.BridgeFollower;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.Delay;

public class BrigdeFollowerTest {
    
    static String US_SENSOR_PORT = "S3";
    static String LEFT_MOTOR_PORT = "B";
    static String RIGHT_MOTOR_PORT = "A";

    public static void main(String[] args) {
        Brick brick = BrickFinder.getDefault();
        BridgeFollower lineFollower = new BridgeFollower(brick, US_SENSOR_PORT, LEFT_MOTOR_PORT, RIGHT_MOTOR_PORT);
        
        System.out.println("Following Line");
        lineFollower.start_walking();

    }

}
