package boxMover;

import framework.Ports;
import lejos.utility.Delay;

public class ParallelDriver {
    
    public static void main(String[] args) {
        ParallelDriver.drive();
    }
    
    public static void drive() {
        boolean wallFound = false;
        int time = 0;
        
        Ports.LEFT_MOTOR.setSpeed(300);
        Ports.RIGHT_MOTOR.setSpeed(300);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        
        
        while (!Ports.ENTER.isDown()) {
            if (leftButtonPressed()) {
                wallFound = true;
                
                Ports.LEFT_MOTOR.stop(true);
                Ports.RIGHT_MOTOR.stop();
                
                Ports.RIGHT_MOTOR.setSpeed(50);
                Ports.RIGHT_MOTOR.backward();
                while (leftButtonPressed()) {
                    
                }
                Ports.RIGHT_MOTOR.stop();
                Ports.RIGHT_MOTOR.setSpeed(300);
                
                Ports.LEFT_MOTOR.forward();
                Ports.RIGHT_MOTOR.forward();
                time = 0;
            }
            Delay.msDelay(1);
            time++;
            if (wallFound && time >= 1000) {
                System.out.println("Parallel");
                Ports.LEFT_MOTOR.stop(true);
                Ports.RIGHT_MOTOR.stop();
                break;
            }
        }
    }
    
    private static boolean leftButtonPressed() {
        float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
        
        Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
        
        return touchLeft[0] == 1;
    }

}
