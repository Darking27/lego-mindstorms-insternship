package bridgeFollower;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;
import lejos.robotics.filter.MedianFilter;

/**
 * Simple filter for ultrasonic that performs a median filter on the data and maps it onto [0,1]
 * to determine whether the sensor sees the ground or bridge as the robot passes the bridge.
 * 
 * The resulting sample has size 1 and contains a value that is mapped onto [0,1] with the following meaning:
 * 1: certainly seeing ground
 * 0: certainly seeing bridge
 * 
 * @author Niklas Arlt
 */
public class SimpleUltrasonic extends AbstractFilter {
	/*
	 * Bridge value
	 */
	private static final float bridgeDist = 0.15f;

	public SimpleUltrasonic(SampleProvider source) {
		super(new MedianFilter(source, 3));
	}

	/*
	 * To create a filter one overwrites the fetchSample method. A sample must first
	 * be fetched from the source (a sensor or other filter). Then it is processed
	 * according to the function of the filter
	 */
	public void fetchSample(float[] sample, int offset) {
		super.fetchSample(sample, offset);
		
		// Sensor outputs invalid values - this usually happens on the bridge
		if (sample[offset] == Float.POSITIVE_INFINITY) {
			sample[offset] = 0;
		} else if (sample[offset] < bridgeDist) {
			sample[offset] = 0;
		} else {
			sample[offset] = 1;
		}
		
		
	}
}
