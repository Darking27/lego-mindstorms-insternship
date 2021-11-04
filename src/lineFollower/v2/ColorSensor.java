package lineFollower.v2;

import lejos.hardware.Brick;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor extends Thread {

    private SampleProvider sampleProvider;
    private EV3ColorSensor colorSensor;
    private ColorID colorId;
    
    public ColorSensor(Brick brick, String colorSensorPort) {
        Port sensorPort = brick.getPort(colorSensorPort);
        this.colorSensor = new EV3ColorSensor(sensorPort);
        this.sampleProvider = colorSensor.getColorIDMode();
        this.colorId = null;
    }
    
    public void run() {
        while(true) {
            float[] sample = new float[sampleProvider.sampleSize()];
            sampleProvider.fetchSample(sample, 0);
            this.colorId = ColorID.getColorID((int) sample[0]);
            //System.out.println(this.colorId.toString());
        }
    }
    
    public ColorID getColorId( ) {
        return this.colorId;
    }
    
}