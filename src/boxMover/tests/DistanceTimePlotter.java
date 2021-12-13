package boxMover.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import display.GraphPlotter;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.hardware.Key;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MedianFilter;
import lejos.utility.Delay;

public class DistanceTimePlotter implements ParcoursWalkable {
	
	private GraphPlotter plotter;
	private SampleProvider s;
	private float[] sample;
	
	public DistanceTimePlotter() {
		plotter = new GraphPlotter(Ports.BRICK);
		s = Ports.ULTRASONIC_SENSOR.getDistanceMode();
		sample = new float[1];
	}

	
	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Ports.RIGHT_MOTOR.setSpeed(360);
		Ports.RIGHT_MOTOR.rotate(-720, true);
		
		List<Float> values = new ArrayList<Float>(Collections.nCopies(200, 1f));
		
		for (Float value : values) {
			s.fetchSample(sample, 0);
			value = sample[0];
			
			LinkedList<List<Float>> lists = new LinkedList<>();
			lists.add(values);
			plotter.plotValues(lists, 0, 2);
			Delay.msDelay(2);
		}
		
		Delay.msDelay(3000);
		
		return WalkableStatus.FINISHED;
	}
	
}
