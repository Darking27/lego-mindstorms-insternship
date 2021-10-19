package robot;

import lejos.hardware.Brick;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class LineFinder {
	
	Brick brick;
	EV3ColorSensor colorSensor;
	Mover mover;
	
	public LineFinder(Brick brick, Mover mover, String colorSensorPort) {
		this.brick = brick;
		this.mover = mover;
		Port sensorPort = brick.getPort(colorSensorPort);
		this.colorSensor = new EV3ColorSensor(sensorPort);
	}
	
}
