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
        BridgeFollower lineFollower = new BridgeFollower();
        
        System.out.println("Following Line");
        lineFollower.start_walking();
    }

}
