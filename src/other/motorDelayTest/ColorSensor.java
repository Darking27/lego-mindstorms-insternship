package other.motorDelayTest;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColorSensor {
	Brick brick;
	EV3ColorSensor colorSensor;
	
	public ColorSensor(Brick brick, String colorSensorPort) {
		this.brick = brick;
		Port sensorPort = brick.getPort(colorSensorPort);
		this.colorSensor = new EV3ColorSensor(sensorPort);
	}
	
	public void sample() {
		SampleProvider redMode = this.colorSensor.getRedMode();
		int sampleSize = redMode.sampleSize();
		float[] sample = new float[sampleSize];
		
		Key escape = brick.getKey("Escape");
		
		while (!escape.isDown()) {
			redMode.fetchSample(sample, 0);
			for (int i = 0; i < sampleSize; i++) {
				System.out.print(sample[i] + " ");
			}
			System.out.println();
			Delay.msDelay(50);
		}
	}
}
