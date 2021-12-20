package bridgeFollower.tests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import display.GraphPlotter;
import framework.Ports;
import lejos.hardware.Key;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;
import lejos.utility.Delay;

public class DistancePlotter {

	public static void main(String[] args) {
		GraphPlotter plotter = new GraphPlotter(Ports.BRICK);
		SampleProvider s = new MedianFilter(Ports.ULTRASONIC_SENSOR.getDistanceMode(), 3);
		float[] sample = new float[1];
		Key escape = Ports.BRICK.getKey("Escape");
		
		while (!escape.isDown()) {
//			List<Float> values = new ArrayList<Float>(500);
//			for (int i = 0; i < 200; i++) {
//				s.fetchSample(sample, 0);
//				values.add(sample[0] * 100);
//			}
//			LinkedList<List<Float>> lists = new LinkedList<>();
//			lists.add(values);
//			plotter.plotValues(lists, 5, 60);
			s.fetchSample(sample, 0);
			System.out.println(sample[0]);
			Delay.msDelay(1000);
		}

	}

}
