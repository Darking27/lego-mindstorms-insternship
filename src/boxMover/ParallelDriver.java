package boxMover;

import framework.Ports;
import framework.RobotUtils;
import lejos.utility.Delay;

public class ParallelDriver {
    
    public static void main(String[] args) {
        ParallelDriver.drive();
    }
    
    public static void drive() {
        boolean wallFound = false;
        int time = 0;
        
        RobotUtils.setMaxSpeed();
        Ports.LEFT_MOTOR.forward();			
        Ports.RIGHT_MOTOR.forward();
        
        while (!Ports.ENTER.isDown()) {
            if (leftButtonPressed()) {
                wallFound = true;
                
                RobotUtils.stopMotors();		//drive away from wall
                Ports.RIGHT_MOTOR.setSpeed(300);
                Ports.RIGHT_MOTOR.backward();
                while (leftButtonPressed());
                RobotUtils.stopMotors();
                
                RobotUtils.setSpeed(600);
                RobotUtils.forward();
                time = 0;
            }
            Delay.msDelay(1);
            time++;
            if (wallFound && time >= 1000) {
                System.out.println("Parallel");
                RobotUtils.stopMotors();
                return;
            }
        }
    }
    
    private static boolean leftButtonPressed() {
        float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
        
        Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
        
        return touchLeft[0] == 1;
    }

}
