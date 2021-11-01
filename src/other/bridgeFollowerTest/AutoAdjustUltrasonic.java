package other.bridgeFollowerTest;

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
public class AutoAdjustUltrasonic extends AbstractFilter {
	/*
	 * Bridge average
	 */
	private float minimum = 0.10f;
	
	/*
	 * Ground Average
	 */
	private float maximum = 0.13f;

	public AutoAdjustUltrasonic(SampleProvider source) {
		super(source);
		reset();
	}

	public void reset() {
		/* Set the arrays to their initial value */
		minimum = 0.10f;
		maximum = 0.13f;
	}
	
	/**
	 * @return the input value that separates ground from bridge
	 */
	private float separator() {
		return minimum + 0.7f * (maximum - minimum);
	}

	/*
	 * To create a filter one overwrites the fetchSample method. A sample must first
	 * be fetched from the source (a sensor or other filter). Then it is processed
	 * according to the function of the filter
	 */
	public void fetchSample(float[] sample, int offset) {
		super.fetchSample(sample, offset);
		float confidentiality = 0f;
		
		// Sensor outputs invalid values - this usually happens on the bridge
		if (sample[offset + 1] == Float.POSITIVE_INFINITY) {
			sample[offset + 1] = -1;
		}
		// Normal cases
		else if (sample[offset + 1] > separator() && sample[offset + 1] < 1.3f * maximum) {
			confidentiality = (sample[offset + 1] - separator()) / maximum;
			sample[offset + 1] = 0.5f * (1f + confidentiality);
			
			// Adjust maximum if not too far off
			if (confidentiality > 0.5f) {
				maximum = 0.5f * (maximum + sample[offset + 1]);
			}
		} else if (sample[offset + 1] > minimum) {
			confidentiality = 1 - (sample[offset + 1] - minimum) / (separator() - minimum);
			sample[offset + 1] = 0.5f * (1f - confidentiality);
		}
	}
}
