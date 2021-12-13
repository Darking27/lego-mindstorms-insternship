package sensors.util;

import framework.Ports;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColorIdSensor {

    SampleProvider mode;
    
    public ColorIdSensor() {
        mode = Ports.COLOR_SENSOR.getColorIDMode();
    }
    
    public void run() {
        System.out.println(mode.sampleSize());
        Delay.msDelay(2000);
        while (Ports.ENTER.isUp()) {
            float sample[] = new float[mode.sampleSize()];
            mode.fetchSample(sample, 0);
            
            System.out.println(sample[0]);
            Delay.msDelay(50);
        }
    }
    
    public static void main(String[] args) {
        ColorIdSensor s = new ColorIdSensor();
        s.run();
    }
}
