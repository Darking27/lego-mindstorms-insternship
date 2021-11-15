package framework;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public class Ports {
	public static final Brick BRICK = BrickFinder.getDefault();
	public static final RegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(BRICK.getPort("A"));
	public static final RegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(BRICK.getPort("B"));
	public static final EV3TouchSensor LEFT_TOUCH_SENSOR = new EV3TouchSensor(BRICK.getPort("S4"));
	public static final EV3TouchSensor RIGHT_TOUCH_SENSOR = new EV3TouchSensor(BRICK.getPort("S2"));
	public static final EV3UltrasonicSensor ULTRASONIC_SENSOR = new EV3UltrasonicSensor(BRICK.getPort("S3"));
	public static final EV3ColorSensor COLOR_SENSOR = new EV3ColorSensor(BRICK.getPort("S1"));
	public static final Key ENTER = BRICK.getKey("Enter");
	public static final Key ESCAPE = BRICK.getKey("Escape");	
}
