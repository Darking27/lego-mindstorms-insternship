package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class LineFollowerOld {

	Brick brick;
	SampleProvider redMode;
	SampleProvider reflectedLight;
	EV3ColorSensor colorSensor;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;

	public LineFollowerOld(Brick brick, String colorSensorPort, String leftMotorPort, String rightMotorPort) {
		this.brick = brick;
		Port sensorPort = brick.getPort(colorSensorPort);
		this.colorSensor = new EV3ColorSensor(sensorPort);
		redMode = colorSensor.getRedMode();
		reflectedLight = new autoAdjustFilter(redMode);

		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
	}

	public void followLineSimple() {
		Key escape = brick.getKey("Enter");

		rightMotor.setSpeed(300);
		leftMotor.setSpeed(300);

		boolean onLine = false;
		
		int print_var = 0;

		int timeSinceLastTurn = 0; // normal ~200-4000, sharp edge ~ 14000

		while (!escape.isDown()) {
			timeSinceLastTurn++;
			if (isOnLine() && !onLine) {
				leftMotor.stop(true);
				rightMotor.rotate(1000, true);
				onLine = true;
				System.out.println(timeSinceLastTurn + " ");
				timeSinceLastTurn = 0;
			}
			if (!isOnLine() && onLine) {
				rightMotor.stop(true);
				leftMotor.rotate(1000, true);
				onLine = false;
				System.out.println(timeSinceLastTurn + " ");
				timeSinceLastTurn = 0;
			}
		}
	}

	/**
	 * Checks whether the robot is on the line or not
	 * 
	 * @return
	 */
	public boolean isOnLine() {
		int sampleSize = reflectedLight.sampleSize();
		float[] sample = new float[sampleSize];
		reflectedLight.fetchSample(sample, 0);
		boolean onLine = (sample[0] > 0.5);
		return onLine;
	}

	public class autoAdjustFilter extends AbstractFilter {
		/* These arrays hold the smallest and biggest values that have been "seen: */
		private float[] minimum;
		private float[] maximum;

		public autoAdjustFilter(SampleProvider source) {
			super(source);
			/* Now the source and sampleSize are known. The arrays can be initialized */
			minimum = new float[sampleSize];
			maximum = new float[sampleSize];
			reset();
		}

		public void reset() {
			/* Set the arrays to their initial value */
			for (int i = 0; i < sampleSize; i++) {
				minimum[i] = Float.POSITIVE_INFINITY;
				maximum[i] = Float.NEGATIVE_INFINITY;
			}
		}

		/*
		 * To create a filter one overwrites the fetchSample method. A sample must first
		 * be fetched from the source (a sensor or other filter). Then it is processed
		 * according to the function of the filter
		 */
		public void fetchSample(float[] sample, int offset) {
			super.fetchSample(sample, offset);
			for (int i = 0; i < sampleSize; i++) {
				if (minimum[i] > sample[offset + i])
					minimum[i] = sample[offset + i];
				if (maximum[i] < sample[offset + i])
					maximum[i] = sample[offset + i];
				sample[offset + i] = (sample[offset + i] - minimum[i]) / (maximum[i] - minimum[i]);
			}
		}
	}
}
