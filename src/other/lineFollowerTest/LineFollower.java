package other.lineFollowerTest;

import lejos.hardware.Brick;
import lejos.hardware.Key;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class LineFollower {
    
	Brick brick;
	SampleProvider redMode;
	SampleProvider reflectedLight;
	EV3ColorSensor colorSensor;
	
	
	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;
	
	public LineFollower(Brick brick, String colorSensorPort, String leftMotorPort,
	        String rightMotorPort) {
		this.brick = brick;
		Port sensorPort = brick.getPort(colorSensorPort);
		this.colorSensor = new EV3ColorSensor(sensorPort);
		redMode = colorSensor.getRedMode();
		reflectedLight = new autoAdjustFilter(redMode);
		
		this.rightMotor = new EV3LargeRegulatedMotor(brick.getPort(rightMotorPort));
		this.leftMotor = new EV3LargeRegulatedMotor(brick.getPort(leftMotorPort));
	}
	
	public void followLineSimple() {
	    Key escape = brick.getKey("Escape");

	    rightMotor.setSpeed(300);
	    leftMotor.setSpeed(300);
	    
	    boolean onLine = false;
	    
	    while (!escape.isDown()) {
	        if (isOnLine() && !onLine) {
	            leftMotor.flt(true);
	            rightMotor.rotate(360, true);
	            onLine = true;
	        }
	        if (!isOnLine() && onLine) {
	            rightMotor.flt(true);
	            leftMotor.rotate(360);
	        }
	    }
	}
	
	/**
	 * Checks whether the robot is on the line or not
	 * @return
	 */
	private boolean isOnLine() {
	    int sampleSize = reflectedLight.sampleSize();
	    float[] sample = new float[sampleSize];
	    reflectedLight.fetchSample(sample, 0);
	    boolean onLine = (sample[0] > 0.5);
	    System.out.println(onLine);
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
	     * To create a filter one overwrites the fetchSample method. A sample must
	     * first be fetched from the source (a sensor or other filter). Then it is
	     * processed according to the function of the filter
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
