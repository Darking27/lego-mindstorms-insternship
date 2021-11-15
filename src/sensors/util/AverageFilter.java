package sensors.util;

import java.util.LinkedList;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * Auto-adjusting filter for ultrasonic that automatically adjusts the threshold value
 * to determine whether the sensor sees the ground or bridge as the robot passes the bridge.
 * 
 * The resulting sample has size 1 and contains a value that is mapped onto [0,1] with the following meaning:
 * 1: certainly seeing ground
 * 0: certainly seeing bridge
 * 
 * @author Niklas Arlt
 */
public class AverageFilter extends AbstractFilter {
	
	private final int size;
	private LinkedList<Float> values;

	public AverageFilter(SampleProvider source, int size) {
		super(source);
		this.size = size;
		reset();
	}

	public void reset() {
		values = new LinkedList<Float>();
	}


	/*
	 * To create a filter one overwrites the fetchSample method. A sample must first
	 * be fetched from the source (a sensor or other filter). Then it is processed
	 * according to the function of the filter
	 */
	public void fetchSample(float[] sample, int offset) {
		super.fetchSample(sample, offset);
		values.add(sample[0]);
		if (values.size() > size) {
			values.removeFirst();
		}
		
		float sum = 0f;
		for (Float e : values) {
			sum += e;
		}
		float avg = sum / size;
				
		sample[0] = avg;
	}
}
