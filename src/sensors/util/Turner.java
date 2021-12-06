package sensors.util;

import framework.Ports;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Turner {
    
    private EV3GyroSensor gyroSensor;
    
    private SampleProvider gyroAngle;
    
    public Turner(Brick brick) {
        Port gyroPort = brick.getPort("S2");
        
        gyroSensor = new EV3GyroSensor(gyroPort);
        gyroSensor.reset();
        gyroAngle = gyroSensor.getAngleMode();
    }
    
    public void getValue() {
        float sample[] = new float[gyroAngle.sampleSize()];
        gyroAngle.fetchSample(sample, 0);
        System.out.println(sample[0]);
    }
    
    public void rotateTo(int targetAngle) {
        int Kd = 5;
        
        while (!Ports.ENTER.isDown()) {
            float sample[] = new float[gyroAngle.sampleSize()];
            gyroAngle.fetchSample(sample, 0);
            int currentAngle = (int) sample[0];
            int diff = targetAngle - currentAngle;
            if (diff == 0) break;
            
            int speed = diff * Kd;
            System.out.println(sample[0]);
            
            Ports.LEFT_MOTOR.setSpeed(diff * Kd);
            Ports.RIGHT_MOTOR.setSpeed(diff * Kd);
            
            if (diff >= 0) {
                Ports.LEFT_MOTOR.forward();
                Ports.RIGHT_MOTOR.backward();
            } else {
                Ports.LEFT_MOTOR.backward();
                Ports.RIGHT_MOTOR.forward();
            }
        }
    }
    
    public static void main(String[] args) {
        Turner turner = new Turner(BrickFinder.getDefault());
        Ports.LEFT_MOTOR.setSpeed(300);
        Ports.RIGHT_MOTOR.setSpeed(300);
        turner.getValue();
        Ports.LEFT_MOTOR.rotate(400);
        turner.getValue();
        Delay.msDelay(3000);
    }
}
