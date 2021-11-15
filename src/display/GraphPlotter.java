package display;

import java.util.List;
import lejos.hardware.Brick;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * This class translates a list of values to a graphical representation
 * as a plot for debugging sensor values.
 * 
 * @author Niklas Arlt
 *
 */
public class GraphPlotter {
	private Brick brick;
	private GraphicsLCD g;
	final int SW;
    final int SH;
	
	public GraphPlotter(Brick brick) {
		this.brick = brick;
		this.g = brick.getGraphicsLCD();
		SW = g.getWidth();
		SH = g.getHeight();
	}
	
	/**
	 * Plot the first (SW-31) values of all lists contained in valueLists
	 * 
	 * @param valueLists
	 * @param lowerBound	Lower end of y-axis
	 * @param upperBound	Upper end of y-axis
	 */
	public void plotValues(List<List<Float>> valueLists, int lowerBound, int upperBound) {
		Logger.INSTANCE.disable();
		g.clear();
		g.drawLine(30, 0, 30, SH);
		g.setFont(Font.getSmallFont());
		g.drawString(String.valueOf(lowerBound), 29, 1, GraphicsLCD.TOP | GraphicsLCD.RIGHT);
		g.drawString(String.valueOf(upperBound), 29, SH -1, GraphicsLCD.BOTTOM | GraphicsLCD.RIGHT);
		for (List<Float> list : valueLists) {
			for (int i = 31; i < SW; i++) {
				Float f = list.get(i-31);
				int y = Math.max(0, Math.min(SW, (int) ((f-lowerBound) * SH / upperBound)));
				g.setPixel(i, y, 1);
			}
		}
		g.refresh();
	}
	
	public void stop() {
		Logger.INSTANCE.enable();
	}
}
