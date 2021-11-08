package other;

import javax.swing.text.StyleContext.SmallAttributeSet;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;
import lejos.utility.Delay;

public class ColorSensorReader {
	
	private String COLOR_SENOR_PORT = "S1";
	
	private Brick brick;
	private EV3ColorSensor colorSensor;

	public static void main(String[] args) {
		new ColorSensorReader();
	}
	
	public ColorSensorReader() {
		this.brick = BrickFinder.getDefault();
		Port colorSensorPort = brick.getPort(COLOR_SENOR_PORT);
		this.colorSensor = new EV3ColorSensor(colorSensorPort);
		
		//SampleProvider redMode = this.colorSensor.getRedMode();
		//runSampleProvider(redMode);
		
		SampleProvider rgbMode = this.colorSensor.getRGBMode();
		SampleProvider auto = new autoAdjustFilter(rgbMode);
		runRGB(auto);
		
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

	public void runRGB(SampleProvider sampleProvider) {

	    Key enter = brick.getKey("Enter");
        
        while (!enter.isDown()) {
            float[] sample = new float[sampleProvider.sampleSize()];
            sampleProvider.fetchSample(sample, 0);
            
            //int index = getMaxIndex(sample);
            
            //float factor = (float) 1.0 / sample[index];
            
            /*
	        for (int i = 0; i < sampleProvider.sampleSize(); i++) {
	            System.out.println((i+1) + ": " + sample[i] + ";  ");
	        }
	        */
	        
	        
            System.out.println("Red:    " + sample[0]);
            System.out.println("Green:  " + sample[1]);
            System.out.println("Blue:   " + sample[2]);
            System.out.println("Gray:   " + getGray(sample));
	        
	        System.out.println();
	        Delay.msDelay(3000);
        }
        Delay.msDelay(5000);
	}
	
	public double getGray(float[] sample) {
	    return 0.33 * sample[0] + 0.33 * sample[1] + 0.33 * sample[2];
	}
	
	public int getMaxIndex(float[] sample) {
	    int maxIndex = 0;
	    float maxValue = 0;
	    for (int i = 0; i < sample.length; i++) {
	        if (sample[i] > maxValue) {
	            maxValue = sample[i];
	            maxIndex = i;
	        }
	    }
	    return maxIndex;
	}
	
	/**
	   * This filter dynamicaly adjust the samples value to a range of 0-1. The
	   * purpose of this filter is to autocalibrate a light Sensor to return values
	   * between 0 and 1 no matter what the light conditions. Once the light sensor
	   * has "seen" both white and black it is calibrated and ready for use.
	   * 
	   * The filter could be used in a line following robot. The robot could rotate
	   * to calibrate the sensor.
	   * 
	   * @author Aswin
	   * 
	   */
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
}
