package other;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColorSensorReader {
	
	private String COLOR_SENOR_PORT = "S4";
	
	private Brick brick;
	private EV3ColorSensor colorSensor;

	public static void main(String[] args) {
		new ColorSensorReader();
	}
	
	public ColorSensorReader() {
		this.brick = BrickFinder.getDefault();
		Port colorSensorPort = brick.getPort(COLOR_SENOR_PORT);
		this.colorSensor = new EV3ColorSensor(colorSensorPort);
		
		SampleProvider redMode = this.colorSensor.getRedMode();
		runSampleProvider(redMode);
		
		SampleProvider rgbMode = this.colorSensor.getRedMode();
		runSampleProvider(rgbMode);
		
		this.colorSensor.close();
	}
	
	public void runSampleProvider(SampleProvider sampleProvider) {
		int sampleSize = sampleProvider.sampleSize();
		float[] sample = new float[sampleSize];
		
		Key escape = brick.getKey("Escape");
		
		while (!escape.isDown()) {
			sampleProvider.fetchSample(sample, 0);
			for (int i = 0; i < sampleSize; i++) {
				System.out.print(sample[i] + " ");
			}
			System.out.println();
			Delay.msDelay(50);
		}
	}

}
