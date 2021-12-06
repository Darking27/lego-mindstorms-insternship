package sensors.util;

import framework.RobotUtils;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroSensor {

    Brick brick;
    EV3GyroSensor g;
    SampleProvider gyro;
    Key enter;
    
    public GyroSensor() {
        brick = BrickFinder.getDefault();
        Port port = brick.getPort("S4");
        enter = brick.getKey("Enter");
        g = new EV3GyroSensor(port);
        g.reset();
        gyro = g.getAngleMode();
    }
    
    public void getValue() {
        while (enter.isUp()) {
            float sample[] = new float[gyro.sampleSize()];
            gyro.fetchSample(sample, 0);
            for (int i = 0; i < sample.length; i++) { 
                System.out.print(sample[i] + ", ");
            }
            System.out.println();
        }
    }
    
    public void rotate() {
        RobotUtils.rotateTo(gyro, 90);
    }
    
    public static void main(String[] args) {
        GyroSensor gs = new GyroSensor();
        gs.getValue();
        gs.rotate();
    }

}
