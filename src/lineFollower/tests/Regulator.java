package lineFollower.tests;


import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Regulator {

    double sollWert;
    double istWert;
    
    RegulatedMotor right;
    RegulatedMotor left;
    
    SampleProvider filter;
    
    Brick brick;
    
    int DEFAULT_SPEED = 200;
    
    
    public static void main(String[] args) {
        Regulator regulator = new Regulator();
        
        regulator.runRegulator();
    }
    
    public Regulator() {
        this.brick = BrickFinder.getDefault();
        
        this.left = new EV3LargeRegulatedMotor(brick.getPort("A"));
        this.right = new EV3LargeRegulatedMotor(brick.getPort("B"));
        
        sollWert = 0.5;
        
        EV3ColorSensor cs = new EV3ColorSensor(brick.getPort("S1"));
        SampleProvider rgbMode = cs.getRGBMode();
        filter = new AutoAdjustFilter(rgbMode);
        
    }
    
    public void runRegulator() {
        double Kp = 500;
        
        Key enter = brick.getKey("Enter");
        
        right.setSpeed(DEFAULT_SPEED);
        left.setSpeed(DEFAULT_SPEED);
        
        right.forward();
        left.forward();
        
        while (!enter.isDown()) {
            fetchValue(filter);
            double diff = istWert - sollWert;
            double y = diff * Kp;
            System.out.println(y);
            
            right.setSpeed(DEFAULT_SPEED + (int) y);
            left.setSpeed(DEFAULT_SPEED - (int) y);
            right.forward();
            left.forward();
        }
        
        right.stop();
        left.stop();
        
        Delay.msDelay(3000);
    }

    public void fetchValue(SampleProvider sampleProvider) {
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        this.istWert = getGrayValue(sample);
    }
    
    public double getGrayValue(float[] sample) {
        return 0.33 * sample[0] + 0.33 * sample[1] + 0.33 * sample[2];
    }
}
