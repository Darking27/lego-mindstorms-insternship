package lineFollower.colorSensor;

import framework.Ports;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class RGBColorSensor {
    private static RGBColorSensor INSTANCE;
    
    private SampleProvider rgbMode;
    
    private RGBColorSensor() {
        rgbMode = Ports.COLOR_SENSOR.getColorIDMode();
    }
    
    public static RGBColorSensor getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RGBColorSensor();
        }
        
        return INSTANCE;
    }
    
    public boolean isFinishLine() {
        float[] sample = new float[rgbMode.sampleSize()];
        
        rgbMode.fetchSample(sample, 0);
        
        // System.out.println(sample[0]);
        
//        System.out.println("RED:   " + (int) (1000 * sample[0]));
//        System.out.println("GREEN: " + (int) (1000 * sample[1]));
//        System.out.println("BLUE:  " + (int) (1000 * sample[2]));
        
        return sample[0] == 1;
    }
    
    private void getColorId() {
        float[] sample = new float[rgbMode.sampleSize()];
        
        rgbMode.fetchSample(sample, 0);
        System.out.println(sample[0]);
    }
    
    public static void main(String[] args) {
        while (!Ports.ENTER.isDown()) {
            RGBColorSensor cd = RGBColorSensor.getInstance();
            cd.getColorId();
            Delay.msDelay(500);
        }
    }
}
